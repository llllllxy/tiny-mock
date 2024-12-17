package org.tinycloud.tinymock.common.config.interceptor;


import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.utils.StrUtils;
import org.tinycloud.tinymock.common.utils.web.CookieUtils;
import org.tinycloud.tinymock.common.utils.web.ServletUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuxingyu01
 * @date 2022-09-06 10:27
 * @description 租户会话工具类
 **/
public class TenantTokenUtils {


    /**
     *
     * 获取租户会话token
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getToken(HttpServletRequest request) {
        String token = "";
        if (request != null) {
            // 从请求中获取token，先从Header里取，取不到的话再从cookie里取（适配前后端分离的模式）
            token = request.getHeader(GlobalConstant.TENANT_TOKEN_HEADER_KEY);
            if (StrUtils.isBlank(token)) {
                token = CookieUtils.getCookie(request, GlobalConstant.TENANT_TOKEN_HEADER_KEY);
            }
        }
        return token;
    }

    /**
     * 获取租户会话token
     *
     * @return String
     */
    public static String getToken() {
        HttpServletRequest request = ServletUtils.getRequest();
        String token = "";
        if (request != null) {
            // 从请求中获取token，先从Header里取，取不到的话再从cookie里取（适配前后端分离的模式）
            token = request.getHeader(GlobalConstant.TENANT_TOKEN_HEADER_KEY);
            if (StrUtils.isBlank(token)) {
                token = CookieUtils.getCookie(request, GlobalConstant.TENANT_TOKEN_HEADER_KEY);
            }
        }
        return token;
    }

}
