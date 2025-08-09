package com.jingdong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置类
 * 解决前后端分离项目的跨域问题
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Configuration
public class CorsConfig {

    /**
     * 配置跨域访问规则
     * 
     * @return 跨域配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 允许所有域名访问
        configuration.addAllowedOriginPattern("*");
        
        // 允许所有请求头
        configuration.addAllowedHeader("*");
        
        // 允许所有请求方法
        configuration.addAllowedMethod("*");
        
        // 允许携带认证信息（如Cookie）
        configuration.setAllowCredentials(true);
        
        // 预检请求的有效期，单位为秒
        configuration.setMaxAge(3600L);
        
        // 注册跨域配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
