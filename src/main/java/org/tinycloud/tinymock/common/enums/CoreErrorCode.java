package org.tinycloud.tinymock.common.enums;

public enum CoreErrorCode {
    USERNAME_OR_PASSWORD_MISMATCH(1001, "用户名或密码错误！"),
    USER_IS_DISABLED(1002, "用户已被停用！"),
    THE_ORIGINAL_PASSWORD_IS_WRONG(1003, "原始密码错误，请重新输入后再试！"),
    THE_NEWPASSWORD_ENTERED_TWICE_DOES_NOT_MATCH(1004, "两次输入的新密码不一致！"),

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

    private CoreErrorCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private CoreErrorCode() {
    }
}
