package org.tinycloud.tinymock.common.constant;

/**
 * <p>
 *     业务常量类
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-08-30 14:20
 */
public class BusinessConstant {

    /**
     *  默认业务缓存时效600秒
     */
    public static final Long CACHE_SESSION_TIMEOUT = 600L;

    /**
     * 租户项目 redis key
     */
    public static final String TENANT_PROJECT_REDIS_KEY = "tinymock:project:";

    /**
     * 租户接口 redis key
     */
    public static final String TENANT_MOCK_REDIS_KEY = "tinymock:mock:";
}
