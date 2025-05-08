package org.tinycloud.tinymock.common.exception;


import org.tinycloud.tinymock.common.enums.CommonCode;
import org.tinycloud.tinymock.common.enums.error.CoreErrorCode;

/**
 * <p>
 * 统一核心层异常封装
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-11-24 13:26
 */
public class CoreException extends BusinessException {
    public CoreException(Integer code, String message) {
        super(code, message);
    }

    public CoreException(String message) {
        this(CommonCode.UNKNOWN_ERROR.getCode(), message);
    }

    public CoreException(CoreErrorCode coreErrorCode) {
        this(coreErrorCode.getCode(), coreErrorCode.getDesc());
    }
}
