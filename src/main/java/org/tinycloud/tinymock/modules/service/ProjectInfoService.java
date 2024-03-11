package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.modules.bean.entity.TProjectInfo;
import org.tinycloud.tinymock.modules.bean.vo.ProjectInfoVo;
import org.tinycloud.tinymock.modules.mapper.ProjectInfoMapper;

import java.util.List;

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
        // 然后校验url是否存在
        List<TProjectInfo> projectInfos = this.projectInfoMapper.selectList(
                Wrappers.<TProjectInfo>lambdaQuery().eq(TProjectInfo::getTenantId, TenantHolder.getTenantId())
                        .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        return BeanConvertUtils.convertListTo(projectInfos, ProjectInfoVo::new);
    }
}
