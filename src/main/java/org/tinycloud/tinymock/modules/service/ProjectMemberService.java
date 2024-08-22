package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.modules.bean.dto.ProjectMemberSearchDto;
import org.tinycloud.tinymock.modules.bean.entity.TProjectMember;
import org.tinycloud.tinymock.modules.bean.entity.TTenant;
import org.tinycloud.tinymock.modules.bean.vo.ProjectMemberVo;
import org.tinycloud.tinymock.modules.bean.vo.TenantInfoChooseVo;
import org.tinycloud.tinymock.modules.mapper.ProjectMemberMapper;
import org.tinycloud.tinymock.modules.mapper.TenantMapper;

import java.util.ArrayList;
import java.util.HashMap;
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


    public List<ProjectMemberVo> list(Long projectId) {
        List<TProjectMember> memberInfos = this.projectMemberMapper.selectList(
                Wrappers.<TProjectMember>lambdaQuery().eq(TProjectMember::getProjectId, projectId)
                        .eq(TProjectMember::getDelFlag, GlobalConstant.NOT_DELETED));

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
            return vo;
        }).collect(Collectors.toList());

        ProjectMemberVo myself = new ProjectMemberVo();
        myself.setMemberTenantAccount(TenantHolder.getTenantAccount());
        myself.setMemberTenantName(TenantHolder.getTenant().getTenantName());
        list.add(0, myself);
        return list;
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

        return BeanConvertUtils.convertListTo(tenantList, TenantInfoChooseVo::new);
    }
}
