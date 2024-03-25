package org.tinycloud.tinymock.modules.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-12-11 14:38
 */
@Data
public class TenantInfoVo implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;

    private String tenantAccount;

    private String tenantName;

    private String tenantPhone;

    private String tenantEmail;

    private Integer status;

    private Integer delFlag;

    private Date createdAt;

    private Date updatedAt;

    private String invitationCode;

    private Long tenantAvatar;

    private String tenantAvatarAddress;
}
