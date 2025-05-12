package org.tinycloud.tinymock.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.tinycloud.tinymock.common.config.interceptor.AccessLimitInterceptor;
import org.tinycloud.tinymock.common.config.interceptor.TenantAuthInterceptor;
import org.tinycloud.tinymock.common.config.interceptor.TraceIdInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * WebMvcConfig配置类
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-05-30 13:02
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    @Autowired
    private TraceIdInterceptor traceIdInterceptor;

    @Autowired
    private TenantAuthInterceptor tenantAuthInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> tenantExcludePaths = new ArrayList<>();
        // 开放登录等接口
        tenantExcludePaths.add("/");
        tenantExcludePaths.add("/auth/login");
        tenantExcludePaths.add("/auth/getCode");
        tenantExcludePaths.add("/auth/register");
        tenantExcludePaths.add("/auth/sendEmail");
        tenantExcludePaths.add("/mock/**");

        // 添加静态资源路径
        tenantExcludePaths.add("/css/**");
        tenantExcludePaths.add("/images/**");
        tenantExcludePaths.add("/js/**");
        tenantExcludePaths.add("/lib/**");
        tenantExcludePaths.add("/page/**");
        tenantExcludePaths.add("/index.html");

        // 加入的顺序就是拦截器执行的顺序，按顺序执行所有拦截器的preHandle，所有的preHandle 执行完再执行全部postHandle 最后是postHandle
        // 注册会话拦截器
        registry.addInterceptor(tenantAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(tenantExcludePaths);

        // 注册限流拦截器
        registry.addInterceptor(accessLimitInterceptor)
                .addPathPatterns("/**");

        // 注册TraceId日志追踪拦截器
        registry.addInterceptor(traceIdInterceptor)
                .addPathPatterns("/**");
    }

    /**
     * 配置静态资源映射（spring.web.resources.add-mappings配置为false后，这里需要自定义资源映射）
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/lib/**").addResourceLocations("classpath:/static/lib/");
        registry.addResourceHandler("/page/**").addResourceLocations("classpath:/static/page/");
        registry.addResourceHandler("/index.html").addResourceLocations("classpath:/static/");
    }
}
