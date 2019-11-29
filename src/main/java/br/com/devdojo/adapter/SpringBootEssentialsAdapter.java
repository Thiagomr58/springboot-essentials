package br.com.devdojo.adapter;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


/**
 * Created by Thiago Rodrigues on 29/11/2019
 */
@Configuration
public class SpringBootEssentialsAdapter implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver pfmar = new PageableHandlerMethodArgumentResolver();
        pfmar.setFallbackPageable( PageRequest.of(0,5));
        argumentResolvers.add(pfmar);

    }
}
