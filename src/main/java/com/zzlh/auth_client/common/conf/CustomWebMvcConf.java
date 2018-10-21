package com.zzlh.auth_client.common.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description 自定义webmvc配置
 * @author liulei
 * @date 2018年7月13日 下午2:17:06
 */
@EnableWebMvc
@Configuration
public class CustomWebMvcConf implements WebMvcConfigurer {
	@Autowired
	private AuthInterceptor authInterceptor;
	
	/**
	 * @Description 认证拦截器
	 * @param registry
	 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }

    /**
     * @Description 自定义资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
          registry.addResourceHandler("/webjars/**")
                 .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
