package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 跨域配置
 * </p>
 * @Author: Shun
 * @CreateTime: 2019-08-31 11:41
 * @Emil: 381889220@qq.com
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedOrigins("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
