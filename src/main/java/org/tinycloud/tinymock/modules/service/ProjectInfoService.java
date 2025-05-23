package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.BusinessConstant;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.error.TenantErrorCode;
import org.tinycloud.tinymock.common.exception.TenantException;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.common.utils.RedisUtils;
import org.tinycloud.tinymock.modules.bean.dto.ProjectAddDto;
import org.tinycloud.tinymock.modules.bean.dto.ProjectDeleteDto;
import org.tinycloud.tinymock.modules.bean.dto.ProjectEditDto;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfo;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfoHistory;
import org.tinycloud.tinymock.modules.bean.entity.TProjectInfo;
import org.tinycloud.tinymock.modules.bean.entity.TProjectMember;
import org.tinycloud.tinymock.modules.bean.vo.ProjectInfoVo;
import org.tinycloud.tinymock.modules.mapper.MockInfoHistoryMapper;
import org.tinycloud.tinymock.modules.mapper.MockInfoMapper;
import org.tinycloud.tinymock.modules.mapper.ProjectInfoMapper;
import org.tinycloud.tinymock.modules.mapper.ProjectMemberMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/11 22:04
 */
@Service
public class ProjectInfoService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private MockInfoMapper mockInfoMapper;

    @Autowired
    private MockInfoHistoryMapper mockInfoHistoryMapper;

    @Autowired
    private RedisUtils redisUtils;

    public List<ProjectInfoVo> query() {
        List<ProjectInfoVo> resultList = new ArrayList<>();

        Long tenantId = TenantHolder.getTenantId();
        List<TProjectInfo> ownProjectInfos = this.projectInfoMapper.selectList(
                Wrappers.<TProjectInfo>lambdaQuery().eq(TProjectInfo::getTenantId, tenantId)
                        .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        ownProjectInfos.forEach(x -> {
            ProjectInfoVo vo = new ProjectInfoVo();
            BeanUtils.copyProperties(x, vo);
            vo.setMark("创建者");
            vo.setRemark(vo.getRemark() == null ? "" : vo.getRemark());
            resultList.add(vo);
        });
        List<TProjectMember> memberInfos = this.projectMemberMapper.selectList(
                Wrappers.<TProjectMember>lambdaQuery().eq(TProjectMember::getMemberTenantId, tenantId)
                        .eq(TProjectMember::getDelFlag, GlobalConstant.NOT_DELETED));
        if (CollectionUtils.isEmpty(memberInfos)) {
            return resultList;
        }

        List<Long> cooperateProjectIdList = memberInfos.stream().map(TProjectMember::getProjectId)
                .distinct()
                .collect(Collectors.toList());
        List<TProjectInfo> cooperateProjectInfos = this.projectInfoMapper.selectList(
                Wrappers.<TProjectInfo>lambdaQuery().in(TProjectInfo::getId, cooperateProjectIdList)
                        .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        if (!CollectionUtils.isEmpty(cooperateProjectInfos)) {
            cooperateProjectInfos.forEach(x -> {
                ProjectInfoVo vo = new ProjectInfoVo();
                BeanUtils.copyProperties(x, vo);
                vo.setMark("协作者");
                vo.setRemark(vo.getRemark() == null ? "" : vo.getRemark());
                resultList.add(vo);
            });
        }
        return resultList;
    }

    public ProjectInfoVo detail(Long id) {
        TProjectInfo projectInfo = this.projectInfoMapper.selectOne(
                Wrappers.<TProjectInfo>lambdaQuery()
                        .eq(TProjectInfo::getId, id)
                        .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        return BeanConvertUtils.convertTo(projectInfo, ProjectInfoVo::new);
    }

    public Boolean add(ProjectAddDto dto) {
        boolean exists = this.projectInfoMapper.exists(Wrappers.<TProjectInfo>lambdaQuery()
                .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TProjectInfo::getTenantId, TenantHolder.getTenantId())
                .and(i -> i.eq(TProjectInfo::getProjectName, dto.getProjectName())
                        .or()
                        .eq(TProjectInfo::getPath, dto.getPath()))
        );
        if (exists) {
            throw new TenantException(TenantErrorCode.TENANT_PROJECT_NAME_OR_PATH_ALREADY_EXIST);
        }
        TProjectInfo projectInfo = new TProjectInfo();
        projectInfo.setProjectName(dto.getProjectName());
        projectInfo.setPath(dto.getPath());
        projectInfo.setRemark(dto.getRemark());
        projectInfo.setIntroduce(dto.getIntroduce());
        projectInfo.setDelFlag(GlobalConstant.NOT_DELETED);
        projectInfo.setStatus(GlobalConstant.ENABLED);
        projectInfo.setTenantId(TenantHolder.getTenantId());
        int rows = this.projectInfoMapper.insert(projectInfo);
        return rows > 0;
    }

    public Boolean edit(ProjectEditDto dto) {
        TProjectInfo projectInfo = this.projectInfoMapper.selectOne(
                Wrappers.<TProjectInfo>lambdaQuery().eq(TProjectInfo::getId, dto.getId())
                        .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        if (projectInfo == null || !projectInfo.getTenantId().equals(TenantHolder.getTenantId())) {
            throw new TenantException(TenantErrorCode.ONLY_PROJECT_CREATE_TENANT_CAN_EDIT_PROJECT);
        }
        boolean exists = this.projectInfoMapper.exists(Wrappers.<TProjectInfo>lambdaQuery()
                .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TProjectInfo::getTenantId, TenantHolder.getTenantId())
                .ne(TProjectInfo::getId, dto.getId())
                .and(i -> i.eq(TProjectInfo::getProjectName, dto.getProjectName())
                        .or()
                        .eq(TProjectInfo::getPath, dto.getPath()))
        );
        if (exists) {
            throw new TenantException(TenantErrorCode.TENANT_PROJECT_NAME_OR_PATH_ALREADY_EXIST);
        }
        LambdaUpdateWrapper<TProjectInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TProjectInfo::getId, dto.getId());
        wrapper.set(TProjectInfo::getProjectName, dto.getProjectName());
        wrapper.set(TProjectInfo::getRemark, dto.getRemark());
        wrapper.set(TProjectInfo::getPath, dto.getPath());
        wrapper.set(TProjectInfo::getIntroduce, dto.getIntroduce());
        int rows = this.projectInfoMapper.update(wrapper);
        // 刷新缓存
        this.redisUtils.del(BusinessConstant.TENANT_PROJECT_REDIS_KEY + dto.getId());
        return rows > 0;
    }

    public Boolean delete(ProjectDeleteDto dto) {
        TProjectInfo projectInfo = this.projectInfoMapper.selectOne(
                Wrappers.<TProjectInfo>lambdaQuery().eq(TProjectInfo::getId, dto.getId())
                        .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        if (projectInfo == null || !projectInfo.getTenantId().equals(TenantHolder.getTenantId())) {
            throw new TenantException(TenantErrorCode.ONLY_PROJECT_CREATE_TENANT_CAN_DELETE_PROJECT);
        }
        if (!dto.getProjectName().equals(projectInfo.getProjectName())) {
            throw new TenantException(TenantErrorCode.PROJECT_NAME_NOT_MATCH_CANT_DELETE_PROJECT);
        }

        // 逻辑删除
        LambdaUpdateWrapper<TProjectInfo> wrapper1 = new LambdaUpdateWrapper<>();
        wrapper1.eq(TProjectInfo::getId, projectInfo.getId());
        wrapper1.eq(TProjectInfo::getTenantId, TenantHolder.getTenantId());
        wrapper1.set(TProjectInfo::getDelFlag, GlobalConstant.DELETED);
        int rows = this.projectInfoMapper.update(wrapper1);

        LambdaUpdateWrapper<TProjectMember> wrapper2 = new LambdaUpdateWrapper<>();
        wrapper2.eq(TProjectMember::getProjectId, projectInfo.getId());
        wrapper2.set(TProjectMember::getDelFlag, GlobalConstant.DELETED);
        this.projectMemberMapper.update(null, wrapper2);

        LambdaUpdateWrapper<TMockInfo> wrapper3 = new LambdaUpdateWrapper<>();
        wrapper3.eq(TMockInfo::getProjectId, projectInfo.getId());
        wrapper3.set(TMockInfo::getDelFlag, GlobalConstant.DELETED);
        this.mockInfoMapper.update(null, wrapper3);

        LambdaUpdateWrapper<TMockInfoHistory> wrapper4 = new LambdaUpdateWrapper<>();
        wrapper4.eq(TMockInfoHistory::getProjectId, projectInfo.getId());
        this.mockInfoHistoryMapper.delete(wrapper4);

        // 刷新缓存
        this.redisUtils.del(BusinessConstant.TENANT_PROJECT_REDIS_KEY + projectInfo.getId());
        return rows > 0;
    }
}
