package com.hptg.tik_toc;

import com.hptg.tik_toc.controller.BasicController;
import com.hptg.tik_toc.controller.interceptor.MiniInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Description:配置Tomcat的虚拟目录并映射成web资源以达到图片的展示
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("file:/Users/hptg/Documents/Project/Spring/Tik_Toc/Resources/");
    }

//    @Bean(initMethod="init")
//    public ZKCuratorClient zkCuratorClient() {
//        return new ZKCuratorClient();
//    }

    /**
     * Description：注册拦截器，和下面那个addInterceptors是一起的
     **/
    @Bean
    public MiniInterceptor miniInterceptor() {
        return new MiniInterceptor();
    }

    /**
     * Description：所有的 /user/** api url 都要通过拦截
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(miniInterceptor())
                .addPathPatterns("/user/**")
                .addPathPatterns("/video/upload", "/video/uploadCover", "/video/userLike", "/video/userUnLike", "/video/saveComment")
                .addPathPatterns("/bgm/**")
                .excludePathPatterns("/user/queryPublisher");
        super.addInterceptors(registry);
    }
}
