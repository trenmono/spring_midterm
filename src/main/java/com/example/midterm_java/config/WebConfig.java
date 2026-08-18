package com.example.midterm_java.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/CSS/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/CSS/");
        registry.addResourceHandler("/js/**", "/JS/**")
                .addResourceLocations("classpath:/static/js/", "classpath:/static/JS/");
        registry.addResourceHandler("/images/**", "/IMAGES/**")
                .addResourceLocations("classpath:/static/images/", "classpath:/static/IMAGES/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
