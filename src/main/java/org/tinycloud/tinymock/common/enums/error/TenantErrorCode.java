package org.tinycloud.tinymock.common.enums.error;

public enum TenantErrorCode {
    TENANT_NOT_LOGIN(3001, "会话已过期，请重新登录！"),
    TENANT_USERNAME_OR_PASSWORD_MISMATCH(3002, "用户名或密码错误！"),
    MAXIMUM_LOGIN_CONCURRENCY_HAS_EXCEEDED_THE_LIMIT(3003, "同一账号最大登录数量已超过上限，无法登录！"),
    MAXIMUM_LOGIN_ATTEMPT_TIMES_HAS_EXCEEDED_THE_LIMIT(3004, "您已多次输错密码，账号已经被锁定，请五分钟后再试！"),
    TENANT_IS_DISABLE(3005, "租户已被禁用，请联系工作人员！"),
    CAPTCHA_IS_MISMATCH(3006, "验证码错误或已过期！"),
    EMAILCODE_IS_MISMATCH(3007, "邮箱验证码错误或已过期！"),

    INVITATIONCODE_IS_NOT_EXIST(3008, "邀请码不正确或不存在！"),
    TENANT_IS_NOT_EXIST(3009, "租户不存在！"),
    TENANT_PASSWORD_IS_ENTERED_INCONSISTENTLY(30010, "新密码前后输入不一致，请检查！"),
    TENANT_OLD_PASSWORD_IS_WRONG(3011, "旧密码不正确，请检查！"),


    TENANT_PROJECT_NAME_OR_PATH_ALREADY_EXIST(3100, "项目名称或项目路径已存在，请修改！"),
    TENANT_MOCKINFO_IS_ENABLE(3101, "只可启用'停用'状态的接口！"),
    TENANT_MOCKINFO_IS_DISABLE(3102, "只可停用'启用'状态的接口！"),
    TENANT_MOCKINFO_NOT_EXIST(3103, "接口不存在！"),

    TENANT_MOCKINFO_NAME_OR_URL_ALREADY_EXIST(3104, "接口名称或路径已存在，请修改！"),

    TENANT_MOCKINFO_BACK_FILE_NOT_BELONG_THIS_PROJECT(3105, "备份文件不属于这个项目，请修改！"),
    TENANT_MOCKINFO_IMPORT_PROJECT_ERROR(3106, "导入项目失败，请修改！"),


    ONLY_PROJECT_CREATE_TENANT_CAN_DELETE_MEMBER(3201, "只有项目创建者才可以删除协助者！"),
    ONLY_PROJECT_CREATE_TENANT_CAN_ADD_MEMBER(3202, "只有项目创建者才可以添加协助者！"),
    PROJECT_CREATOR_CAN_NOT_BE_DELETED(3203, "项目创建者无法被删除！"),

    ONLY_PROJECT_CREATE_TENANT_CAN_DELETE_PROJECT(3204, "只有项目创建者才可以删除！"),
    ONLY_PROJECT_CREATE_TENANT_CAN_EDIT_PROJECT(3205, "只有项目创建者才可以修改！"),

    PROJECT_NAME_NOT_MATCH_CANT_DELETE_PROJECT(3206, "项目名称不匹配，无法删除！"),

    ;

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
