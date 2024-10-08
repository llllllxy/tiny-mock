package org.tinycloud.tinymock.modules.bean.dto;


import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-04 15:03
 */
@Getter
@Setter
public class TenantRegisterDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    @NotEmpty(message = "邀请码不能为空")
    private String invitationCode;

    @NotEmpty(message = "登录账户不能为空")
    private String tenantAccount;

    @NotEmpty(message = "登录账户不能为空")
    private String tenantName;

    @NotEmpty(message = "登录密码不能为空")
    private String password;

    @NotEmpty(message = "邮箱不能为空")
    private String tenantEmail;

    @NotEmpty(message = "邮箱验证码不能为空")
    private String emailCode;

    @NotEmpty(message = "唯一键不能为空")
    private String uuid;
}
