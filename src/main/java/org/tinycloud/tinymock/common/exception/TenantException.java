package org.tinycloud.tinymock.common.exception;


import org.tinycloud.tinymock.common.enums.CommonCode;
import org.tinycloud.tinymock.common.enums.error.TenantErrorCode;

/**
 * <p>
 *  统一租户层异常封装
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-11-24 13:26
 */
public class TenantException extends BusinessException {
    public TenantException(Integer code, String message) {
        super(code, message);
    }

    public TenantException(String message) {
        this(CommonCode.UNKNOWN_ERROR.getCode(), message);
    }

    public TenantException(TenantErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getDesc());
    }
}
