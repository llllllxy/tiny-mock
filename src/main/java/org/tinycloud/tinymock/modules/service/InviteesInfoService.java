package org.tinycloud.tinymock.modules.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.modules.bean.entity.TInviteesInfo;
import org.tinycloud.tinymock.modules.mapper.InviteesInfoMapper;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/20 21:50
 */
@Service
public class InviteesInfoService {

    @Autowired
    private InviteesInfoMapper inviteesInfoMapper;

    public boolean add(Long tenantId, String invitationCode, Long inviteesTenantId) {
        TInviteesInfo tInviteesInfo = new TInviteesInfo();
        tInviteesInfo.setDelFlag(GlobalConstant.NOT_DELETED);
        tInviteesInfo.setTenantId(tenantId);
        tInviteesInfo.setInviteesTenantId(inviteesTenantId);
        tInviteesInfo.setInvitationCode(invitationCode);
        int num = this.inviteesInfoMapper.insert(tInviteesInfo);
        return num > 0;
    }
}
