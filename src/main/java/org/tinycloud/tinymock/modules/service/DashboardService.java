package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.modules.bean.entity.TInviteesInfo;
import org.tinycloud.tinymock.modules.bean.entity.TMockAccessLog;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfo;
import org.tinycloud.tinymock.modules.bean.entity.TProjectInfo;
import org.tinycloud.tinymock.modules.bean.vo.DashboardQuantityVo;
import org.tinycloud.tinymock.modules.mapper.InviteesInfoMapper;
import org.tinycloud.tinymock.modules.mapper.MockAccessLogMapper;
import org.tinycloud.tinymock.modules.mapper.MockInfoMapper;
import org.tinycloud.tinymock.modules.mapper.ProjectInfoMapper;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-22 15:32
 */
@Service
public class DashboardService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private MockInfoMapper mockInfoMapper;

    @Autowired
    private InviteesInfoMapper inviteesInfoMapper;

    @Autowired
    private MockAccessLogMapper mockAccessLogMapper;


    public DashboardQuantityVo quantity() {
        Long tenantId = TenantHolder.getTenantId();
        Long projectQuantity = this.projectInfoMapper.selectCount(Wrappers.<TProjectInfo>lambdaQuery()
                .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TProjectInfo::getTenantId, tenantId));
        Long mockQuantity = this.mockInfoMapper.selectCount(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getTenantId, tenantId));
        Long inviteesQuantity = this.inviteesInfoMapper.selectCount(Wrappers.<TInviteesInfo>lambdaQuery()
                .eq(TInviteesInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TInviteesInfo::getTenantId, tenantId));
        Long accessQuantity = this.mockAccessLogMapper.selectCount(Wrappers.<TMockAccessLog>lambdaQuery()
                .eq(TMockAccessLog::getTenantId, tenantId));
        DashboardQuantityVo vo = new DashboardQuantityVo();

        vo.setAccessQuantity(accessQuantity);
        vo.setMockQuantity(mockQuantity);
        vo.setInviteesQuantity(inviteesQuantity);
        vo.setProjectQuantity(projectQuantity);
        return vo;
    }
}
