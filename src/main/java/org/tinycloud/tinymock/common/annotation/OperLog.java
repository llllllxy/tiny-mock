package org.tinycloud.tinymock.common.annotation;


import org.tinycloud.tinymock.common.enums.BusinessType;
import org.tinycloud.tinymock.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * <p>
 *   自定义操作日志记录注解
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-04 14:57
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLog {

    /**
     * 操作码
     */
    public String code() default "0";

    /**
     * 操作名称
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.TENANT;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    public String[] excludeParamNames() default {};
}
