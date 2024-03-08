package org.tinycloud.tinymock.common.exception;


import org.tinycloud.tinymock.common.enums.CommonCode;

/**
 * <p>
 *    统一业务异常封装
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-05-30 13:26
 */
public class BusinessException extends RuntimeException {

    private Integer code;

    private String message;

    private Object errT;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getErrT() {
        return errT;
    }

    public void setErrT(Object errT) {
        this.errT = errT;
    }

    private BusinessException(Integer code, String message, Object errT) {
        super(message);
        this.code = code;
        this.message = message;
        this.errT = errT;
    }

    public BusinessException(Integer code, String message) {
        this(code, message, null);
    }

    public BusinessException(CommonCode code) {
        this(code.getCode(), code.getDesc());
    }

    public BusinessException(CommonCode code, Object errT) {
        this(code.getCode(), code.getDesc(), errT);
    }
}
