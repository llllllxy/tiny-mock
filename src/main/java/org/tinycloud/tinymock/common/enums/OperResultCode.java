package org.tinycloud.tinymock.common.enums;

/**
 * <p>
 * 操作状态
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-04 15:11
 */
public enum OperResultCode {
    /**
     * 成功
     */
    SUCCESS(0),

    /**
     * 失败
     */
    FAIL(1);

    private Integer code;

    private OperResultCode(Integer code) {
        this.code = code;
    }

    private OperResultCode() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
