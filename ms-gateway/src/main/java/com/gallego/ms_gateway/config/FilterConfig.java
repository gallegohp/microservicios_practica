package com.gallego.ms_gateway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gallego.ms_gateway.filter.JwtValidationFilter;

//Fabrica de beans (springboot al arrancar lee la app para configurar el comportamiento de esta misma)
@Configuration
public class FilterConfig {
    // Filtro deshabilitado temporalmente para pruebas
    // @Bean
    // FilterRegistrationBean<JwtValidationFilter> jwtFilter(JwtValidationFilter jwtValitationFilter) {
    //     FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();
    //     registrationBean.setFilter(jwtValitationFilter);
    //     registrationBean.addUrlPatterns("/*");
    //     registrationBean.setOrder(1000);
    //     return registrationBean;
    // }
}
