package com.johnathanmsmith.mvc.web.config;

import com.johnathanmsmith.mvc.web.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.johnathanmsmith.mvc.web"})
public class WebMVCConfig extends WebMvcConfigurerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(WebMVCConfig.class);

    @Bean
    public ViewResolver resolver()    {
        UrlBasedViewResolver url = new UrlBasedViewResolver();
        url.setPrefix("/WEB-INF/views/");
        url.setViewClass(JstlView.class);
        url.setSuffix(".jsp");
        return url;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)    {
        logger.debug("setting up resource handlers");
        registry.addResourceHandler("/resources/").addResourceLocations("/resources/**");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)    {
        logger.debug("configureDefaultServletHandling");
        configurer.enable();
    }

    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver()    {
        SimpleMappingExceptionResolver b = new SimpleMappingExceptionResolver();

        Properties mappings = new Properties();
        mappings.put("org.springframework.web.servlet.PageNotFound", "p404");
        mappings.put("org.springframework.dao.DataAccessException", "dataAccessFailure");
        mappings.put("org.springframework.transaction.TransactionException", "dataAccessFailure");
        b.setExceptionMappings(mappings);
        return b;
    }

    @Bean
    public Car carList(){
        return new Car();
    }
}
