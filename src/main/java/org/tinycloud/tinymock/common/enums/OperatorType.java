package org.tinycloud.tinymock.common.enums;

/**
 * <p>
 *     操作人类别
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-04 15:00
 */
public enum OperatorType {
    /**
     * 其它
     */
    OTHER(0),

    /**
     * 管理后台用户
     */
    MANAGE(1),

    /**
     * 租户端用户
     */
    TENANT(2);

    private Integer code;

    private OperatorType(Integer code) {
        this.code = code;
    }

    private OperatorType() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
