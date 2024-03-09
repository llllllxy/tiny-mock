package org.tinycloud.tinymock.common.constant;

/**
 * <p>
 *  全局常量类
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-03-07 15:47:38
 */
public class GlobalConstant {

    /**
     * 已删除标记
     */
    public static final Integer DELETED = 1;

    /**
     * 未删除标记
     */
    public static final Integer NOT_DELETED = 0;

    /**
     * 正常在用
     */
    public static final Integer ENABLED = 0;

    /**
     * 已停用
     */
    public static final Integer DISABLED = 1;

    /**
     * 限流 redis key
     */
    public static final String LIMIT_REDIS_KEY = "tinymock:limit:";

    /**
     * 租户图形验证码 redis key
     */
    public static final String TENANT_CAPTCHA_CODE_REDIS_KEY = "tinymock:tenant:captcha:";

    /**
     * 租户注册邮箱验证码 redis key
     */
    public static final String TENANT_EMAIL_CODE_REDIS_KEY = "tinymock:tenant:email:";

    /**
     * 租户token redis key
     */
    public static final String TENANT_TOKEN_REDIS_KEY = "tinymock:tenant:token:";

    /**
     * 租户token key
     */
    public static final String TENANT_TOKEN_KEY = "token";

}
