package org.tinycloud.tinymock.common.enums;

public enum CoreErrorCode {
    MOCK_ADDRESS_RESOLUTION_ERROR(1001, "Mock地址解析错误！"),
    MOCK_PROJECT_PATH_IS_NOT_EXIST(1002, "Mock项目路径不存在！"),
    MOCK_URL_IS_NOT_EXIST(1003, "Mock地址不存在！"),
    MOCK_METHOD_IS_NOT_MATCHED(1004, "HTTP Method不匹配！"),
    MOCKJS_PARSING_ERROR(1005, "Mock.js解析错误！"),

    FILE_UPLOAD_FAILED(2001, "文件上传失败，请联系系统管理员！"),
    FILE_NOT_EXIST(2002, "文件不存在！"),
    FILE_DOWNLOAD_FAILED(2003, "文件下载失败，请联系系统管理员！"),
    FILE_DELETE_FAILED(2004, "文件删除失败，请联系系统管理员！"),
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
