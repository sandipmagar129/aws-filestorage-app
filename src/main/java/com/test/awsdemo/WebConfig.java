package com.test.awsdemo;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{

	// If you are using Simple JavaScript / HTML Page 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") 
        		.allowedOrigins("*")
        		.allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
    }
}
