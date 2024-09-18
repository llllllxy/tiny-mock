package org.tinycloud.tinymock.common.config.interceptor;

import java.util.Objects;


/**
 * <p>
 * 本地线程变量-缓存租户会话信息
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-12-2023/12/3 21:20
 */
public class TenantHolder {
    private final static ThreadLocal<TenantAuthCache> tenant = new ThreadLocal<>();

    public static TenantAuthCache getTenant() {
        return tenant.get();
    }

    public static Long getTenantId() {
        TenantAuthCache tenant = getTenant();
        if (Objects.isNull(tenant)) {
            return null;
        } else {
            return tenant.getId();
        }
    }

    public static String getTenantAccount() {
        TenantAuthCache tenant = getTenant();
        if (Objects.isNull(tenant)) {
            return "";
        } else {
            return tenant.getTenantAccount();
        }
    }

    public static void setTenant(TenantAuthCache t) {
        tenant.set(t);
    }

    public static void clearTenant() {
        tenant.remove();
    }
}
