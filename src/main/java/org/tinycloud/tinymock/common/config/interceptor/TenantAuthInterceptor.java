package org.tinycloud.tinymock.common.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.tinycloud.tinymock.common.config.ApplicationConfig;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.TenantErrorCode;
import org.tinycloud.tinymock.common.exception.TenantException;
import org.tinycloud.tinymock.common.utils.JwtUtils;
import org.tinycloud.tinymock.common.utils.RedisUtils;
import org.tinycloud.tinymock.common.utils.StrUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 租户会话拦截器
 *
 * @author liuxingyu01
 * @since 2022-03-26 20:48
 **/
@Slf4j
@Component
public class TenantAuthInterceptor implements HandlerInterceptor {

    private static final Long MILLIS_MINUTE_TEN = 10 * 60 * 1000L;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ApplicationConfig applicationConfig;

    /*
     * 进入controller层之前拦截请求
     * 返回值：表示是否将当前的请求拦截下来  false：拦截请求，请求别终止。true：请求不被拦截，继续执行
     * Object obj:表示被拦的请求的目标对象（controller中方法）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断请求类型，如果是OPTIONS，直接返回
        String options = HttpMethod.OPTIONS.toString();
        if (options.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 第1步：先判断token是否为空
        String token = TenantTokenUtils.getToken(request);
        if (StrUtils.isBlank(token)) {
            throw new TenantException(TenantErrorCode.TENANT_NOT_LOGIN);
        }
        // 第2步：校验token的格式是否正确
        if (token.startsWith(GlobalConstant.TOKEN_PREFIX)) {
            // 去除TOKEN_PREFIX
            token = token.replace(GlobalConstant.TOKEN_PREFIX, "");
        } else { // token不是以TOKEN_PREFIX开头的，不合格
            throw new TenantException(TenantErrorCode.TENANT_NOT_LOGIN);
        }
        // 第3步：校验token是不是伪造的
        Map<String, String> claims = JwtUtils.getClaims(applicationConfig.getJwtSecret(), token);
        if (Objects.isNull(claims)) {
            throw new TenantException(TenantErrorCode.TENANT_NOT_LOGIN);
        }
        // 从jwt的Payload里获取auth_token，这是会话的redis-key，它在redis里面存着用户信息，包括userId，username等
        token = claims.get("token");

        // 再判断token是否存在
        TenantAuthCache tenantAuthCache = (TenantAuthCache) redisUtils.get(GlobalConstant.TENANT_TOKEN_REDIS_KEY + token);
        if (Objects.isNull(tenantAuthCache)) {
            throw new TenantException(TenantErrorCode.TENANT_NOT_LOGIN);
        }
        long expireTime = tenantAuthCache.getLoginExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            // 刷新会话缓存时长
            tenantAuthCache.setLoginExpireTime(currentTime + applicationConfig.getTenantAuthTimeout() * 1000);
            redisUtils.set(GlobalConstant.TENANT_TOKEN_REDIS_KEY + token, (tenantAuthCache), applicationConfig.getTenantAuthTimeout(), TimeUnit.SECONDS);
        }
        TenantHolder.setTenant(tenantAuthCache);

        // 合格不需要拦截，放行
        return true;
    }

    /*
     * 处理请求完成后视图渲染之前的处理操作
     * 通过ModelAndView参数改变显示的视图，或发往视图的方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    /*
     * 视图渲染之后的操作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        TenantHolder.clearTenant();
    }

}