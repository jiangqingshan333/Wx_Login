package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题
 * 改配置文件是配置在Controller层的，但JwtFilter是在Controller之前运行，所以JwtFilter也要做跨域
 */
@Component
@Configuration
//@ConfigurationProperties(prefix = "my-cross-config")
public class CrossConfig implements WebMvcConfigurer {

	private String[] origins;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*")
//				.allowedOrigins(origins)
				.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
				.allowCredentials(true)
				.maxAge(3600)
				.allowedHeaders("*");
	}

	public void setOrigins(String[] origins) {
		this.origins = origins;
	}

}
