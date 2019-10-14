package com.imooc;

import com.imooc.controller.interceptor.MiniInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("file:/Users/hptg/Documents/WeChat/IMOOV_VIDEO_FILE/");
    }

    @Bean
    public MiniInterceptor miniInterceptor(){
        return new MiniInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**").addPathPatterns("/bgm/**").addPathPatterns("/video/upload","/video/uploadCover").addPathPatterns("/video/userLike","/video/userUnLike").excludePathPatterns("/user/queryPublisher");
        super.addInterceptors(registry);
    }
}
