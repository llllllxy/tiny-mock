package org.tinycloud.tinymock.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.tinycloud.tinymock.common.config.interceptor.AccessLimitInterceptor;
import org.tinycloud.tinymock.common.config.interceptor.TenantAuthInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * WebMvcConfig配置类
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-05-30 13:02
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

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
        tenantExcludePaths.add("/static/**");
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
    }

    /**
     * 配置静态资源映射
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

        // 配置swagger-ui资源映射(knife4j)
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    /**
     * 这里加这个配置是为了解决Jackson2ObjectMapperBuilderCustomizer自定义配置不生效的问题
     * 参考自：<a href="https://www.jianshu.com/p/09169bd31f72">...</a>
     */
    @Autowired(required = false)
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        if (Objects.isNull(mappingJackson2HttpMessageConverter)) {
            converters.add(0, new MappingJackson2HttpMessageConverter());
        } else {
            converters.add(0, mappingJackson2HttpMessageConverter);
        }
    }
}
