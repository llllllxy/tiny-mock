package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.error.TenantErrorCode;
import org.tinycloud.tinymock.common.exception.TenantException;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.modules.bean.dto.ProjectMemberAddDto;
import org.tinycloud.tinymock.modules.bean.dto.ProjectMemberSearchDto;
import org.tinycloud.tinymock.modules.bean.entity.TProjectInfo;
import org.tinycloud.tinymock.modules.bean.entity.TProjectMember;
import org.tinycloud.tinymock.modules.bean.entity.TTenant;
import org.tinycloud.tinymock.modules.bean.vo.ProjectMemberVo;
import org.tinycloud.tinymock.modules.bean.vo.TenantInfoChooseVo;
import org.tinycloud.tinymock.modules.mapper.ProjectInfoMapper;
import org.tinycloud.tinymock.modules.mapper.ProjectMemberMapper;
import org.tinycloud.tinymock.modules.mapper.TenantMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-08-2024/8/21 23:23
 */
@Service
public class ProjectMemberService {

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;


    public List<ProjectMemberVo> list(Long projectId) {
        List<ProjectMemberVo> finallyList = new ArrayList<>();

        TProjectInfo projectInfo = this.projectInfoMapper.selectOne(
                Wrappers.<TProjectInfo>lambdaQuery().eq(TProjectInfo::getId, projectId)
                        .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        TTenant tenant = this.tenantMapper.selectOne(Wrappers.<TTenant>lambdaQuery()
                .select(TTenant::getId, TTenant::getTenantAccount, TTenant::getTenantName)
                .eq(TTenant::getDelFlag, GlobalConstant.NOT_DELETED)
                .in(TTenant::getId, projectInfo.getTenantId()));
        ProjectMemberVo myself = new ProjectMemberVo();
        myself.setMemberTenantAccount(tenant.getTenantAccount());
        myself.setMemberTenantName(tenant.getTenantName());
        myself.setCreatedAt(projectInfo.getCreatedAt());
        finallyList.add(0, myself);

        List<TProjectMember> memberInfos = this.projectMemberMapper.selectList(
                Wrappers.<TProjectMember>lambdaQuery().eq(TProjectMember::getProjectId, projectId)
                        .eq(TProjectMember::getDelFlag, GlobalConstant.NOT_DELETED));
        if (CollectionUtils.isEmpty(memberInfos)) {
            return finallyList;
        }

        List<Long> memberIdList = memberInfos.stream().map(TProjectMember::getMemberTenantId)
                .distinct()
                .collect(Collectors.toList());
        List<TTenant> tenantList = this.tenantMapper.selectList(Wrappers.<TTenant>lambdaQuery()
                .select(TTenant::getId, TTenant::getTenantAccount, TTenant::getTenantName)
                .eq(TTenant::getDelFlag, GlobalConstant.NOT_DELETED)
                .in(TTenant::getId, memberIdList));
        Map<Long, TTenant> tenantMap = tenantList.stream().collect(Collectors.toMap(TTenant::getId, Function.identity(), (oldValue, newValue) -> oldValue));
        List<ProjectMemberVo> list = memberInfos.stream().map(x -> {
            ProjectMemberVo vo = new ProjectMemberVo();
            BeanUtils.copyProperties(x, vo);
            vo.setMemberTenantAccount(tenantMap.get(vo.getMemberTenantId()).getTenantAccount());
            vo.setMemberTenantName(tenantMap.get(vo.getMemberTenantId()).getTenantName());
            vo.setRemark(vo.getRemark() == null ? "" : vo.getRemark());
            return vo;
        }).collect(Collectors.toList());
        finallyList.addAll(list);
        return finallyList;
    }


    public List<TenantInfoChooseVo> search(ProjectMemberSearchDto dto) {
        if (!StringUtils.hasText(dto.getKeyword())) {
            return new ArrayList<>();
        }
        List<TProjectMember> memberInfos = this.projectMemberMapper.selectList(
                Wrappers.<TProjectMember>lambdaQuery().eq(TProjectMember::getProjectId, dto.getProjectId())
                        .eq(TProjectMember::getDelFlag, GlobalConstant.NOT_DELETED));
        List<Long> memberIdList = memberInfos.stream().map(TProjectMember::getMemberTenantId)
                .distinct()
                .collect(Collectors.toList());
        // 加上他自己
        memberIdList.add(TenantHolder.getTenantId());

        List<TTenant> tenantList = this.tenantMapper.selectList(Wrappers.<TTenant>lambdaQuery()
                .select(TTenant::getId, TTenant::getTenantAccount, TTenant::getTenantName)
                .eq(TTenant::getDelFlag, GlobalConstant.NOT_DELETED)
                .notIn(TTenant::getId, memberIdList) // 排除掉已经添加的
                .and(i -> i.eq(TTenant::getTenantAccount, dto.getKeyword())
                        .or()
                        .eq(TTenant::getTenantName, dto.getKeyword())));
        List<TenantInfoChooseVo> list = BeanConvertUtils.convertListTo(tenantList, TenantInfoChooseVo::new);
        return list.stream().map(x -> {
            x.setTenantAccount(x.getTenantAccount() + "(" + x.getTenantName() + ")");
            return x;
        }).collect(Collectors.toList());
    }


    public boolean delete(Long id) {
        TProjectMember memberInfo = this.projectMemberMapper.selectOne(
                Wrappers.<TProjectMember>lambdaQuery().eq(TProjectMember::getId, id)
                        .eq(TProjectMember::getDelFlag, GlobalConstant.NOT_DELETED));
        if (memberInfo == null || !memberInfo.getCreateTenantId().equals(TenantHolder.getTenantId())) {
            throw new TenantException(TenantErrorCode.ONLY_PROJECT_CREATE_TENANT_CAN_DELETE_MEMBER);
        }
        // 逻辑删除
        LambdaUpdateWrapper<TProjectMember> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TProjectMember::getId, id);
        wrapper.eq(TProjectMember::getCreateTenantId, TenantHolder.getTenantId());
        wrapper.set(TProjectMember::getDelFlag, GlobalConstant.DELETED);
        int rows = this.projectMemberMapper.update(null, wrapper);
        return rows > 0;
    }


    public boolean add(ProjectMemberAddDto dto) {
        Long tenantId = TenantHolder.getTenantId();
        boolean exist = this.projectInfoMapper.exists(Wrappers.<TProjectInfo>lambdaQuery()
                .eq(TProjectInfo::getId, dto.getProjectId())
                .eq(TProjectInfo::getTenantId, tenantId)
                .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        if (!exist) {
            throw new TenantException(TenantErrorCode.ONLY_PROJECT_CREATE_TENANT_CAN_ADD_MEMBER);
        }
        TProjectMember member = new TProjectMember();
        member.setProjectId(dto.getProjectId());
        member.setMemberTenantId(dto.getMemberTenantId());
        member.setCreateTenantId(tenantId);
        member.setDelFlag(GlobalConstant.NOT_DELETED);
        member.setStatus(GlobalConstant.ENABLED);
        int rows = this.projectMemberMapper.insert(member);
        return rows > 0;
    }
}
