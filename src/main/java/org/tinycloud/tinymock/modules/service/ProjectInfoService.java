package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.TenantErrorCode;
import org.tinycloud.tinymock.common.exception.TenantException;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.modules.bean.dto.ProjectAddDto;
import org.tinycloud.tinymock.modules.bean.dto.ProjectEditDto;
import org.tinycloud.tinymock.modules.bean.entity.TProjectInfo;
import org.tinycloud.tinymock.modules.bean.vo.ProjectInfoVo;
import org.tinycloud.tinymock.modules.mapper.ProjectInfoMapper;

import java.util.List;
import java.util.Objects;

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

    public List<ProjectInfoVo> query() {
        List<TProjectInfo> projectInfos = this.projectInfoMapper.selectList(
                Wrappers.<TProjectInfo>lambdaQuery().eq(TProjectInfo::getTenantId, TenantHolder.getTenantId())
                        .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        return BeanConvertUtils.convertListTo(projectInfos, ProjectInfoVo::new);
    }

    public ProjectInfoVo detail(Long id) {
        TProjectInfo projectInfo = this.projectInfoMapper.selectOne(
                Wrappers.<TProjectInfo>lambdaQuery().eq(TProjectInfo::getTenantId, TenantHolder.getTenantId())
                        .eq(TProjectInfo::getId, id)
                        .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        return BeanConvertUtils.convertTo(projectInfo, ProjectInfoVo::new);
    }

    public Boolean add(ProjectAddDto dto) {
        TProjectInfo tProjectInfo = this.projectInfoMapper.selectOne(Wrappers.<TProjectInfo>lambdaQuery()
                .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TProjectInfo::getTenantId, TenantHolder.getTenantId())
                .and(i -> i.eq(TProjectInfo::getProjectName, dto.getProjectName())
                        .or()
                        .eq(TProjectInfo::getPath, dto.getPath()))
        );
        if (Objects.nonNull(tProjectInfo)) {
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
        TProjectInfo tProjectInfo = this.projectInfoMapper.selectOne(Wrappers.<TProjectInfo>lambdaQuery()
                .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TProjectInfo::getTenantId, TenantHolder.getTenantId())
                .and(i -> i.eq(TProjectInfo::getProjectName, dto.getProjectName())
                        .or()
                        .eq(TProjectInfo::getPath, dto.getPath()))
        );
        if (Objects.nonNull(tProjectInfo)) {
            throw new TenantException(TenantErrorCode.TENANT_PROJECT_NAME_OR_PATH_ALREADY_EXIST);
        }
        LambdaUpdateWrapper<TProjectInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TProjectInfo::getId, dto.getId());
        wrapper.set(TProjectInfo::getProjectName, dto.getProjectName());
        wrapper.set(TProjectInfo::getRemark, dto.getRemark());
        wrapper.set(TProjectInfo::getPath, dto.getPath());
        wrapper.set(TProjectInfo::getIntroduce, dto.getIntroduce());
        int rows = this.projectInfoMapper.update(null, wrapper);
        return rows > 0;
    }

    public Boolean delete(Long id) {
        // 逻辑删除
        LambdaUpdateWrapper<TProjectInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TProjectInfo::getId, id);
        wrapper.set(TProjectInfo::getDelFlag, GlobalConstant.DELETED);
        int rows = this.projectInfoMapper.update(null, wrapper);
        return rows > 0;
    }
}
