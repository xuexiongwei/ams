package com.xxw.springcloud.ams.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	/**
	 * 添加类型转换器和格式化器
	 * 
	 * @param registry
	 */
	public void addFormatters(FormatterRegistry registry) {

	}

	/**
	 * 跨域支持
	 * 
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowCredentials(true)
				.allowedMethods("GET", "POST", "DELETE", "PUT").maxAge(3600 * 24);
	}

	/**
	 * 添加静态资源--过滤swagger-api (开源的在线API文档)
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 过滤swagger
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		registry.addResourceHandler("/swagger-resources/**")
				.addResourceLocations("classpath:/META-INF/resources/swagger-resources/");

		registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/META-INF/resources/swagger*");

		registry.addResourceHandler("/v2/api-docs/**")
				.addResourceLocations("classpath:/META-INF/resources/v2/api-docs/");

	}
	
	public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CommonCostInterceptor()).addPathPatterns("/**");
    }

}
