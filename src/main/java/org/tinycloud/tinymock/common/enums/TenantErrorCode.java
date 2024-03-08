package org.tinycloud.tinymock.common.enums;

public enum TenantErrorCode {
    TENANT_NOT_LOGIN(3001, "会话已过期，请重新登录！"),
    TENANT_USERNAME_OR_PASSWORD_MISMATCH(3002, "用户名或密码错误！"),
    TENANT_IS_DISABLE(3003, "租户已被禁用，请联系工作人员！"),
    CAPTCHA_IS_MISMATCH(3004, "验证码错误或已过期！"),
    EMAILCODE_IS_MISMATCH(3005, "邮箱验证码错误或已过期！"),
    TENANT_URL_NOT_EXIST(3100, "此短链不存在"),
    TENANT_URL_IS_ENABLE(3101, "只可启用'停用'状态的短链"),
    TENANT_URL_IS_DISABLE(3102, "只可停用'启用'状态的短链");

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private TenantErrorCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private TenantErrorCode() {
    }
}