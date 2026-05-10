package com.gallego.ms_auth.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gallego.ms_auth.filter.JwtValidationFilter;

//Fabrica de beans (springboot al arrancar lee la app para configurar el comportamiento de esta misma)
@Configuration
public class FilterConfig {
    @Bean
    FilterRegistrationBean<JwtValidationFilter> jwtFilter(JwtValidationFilter jwtValitationFilter) {
        
        // Creamos un contenedor de registro del bean para el filtro
        FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();

        // Es decir a Spring que este es el filtro el el que quiero que trabaje
        registrationBean.setFilter(jwtValitationFilter);

        // Definir el alcance del filtro quiero que revise todas las peticiones que entren a mi app
        registrationBean.addUrlPatterns("/*");

        //Establecemos la prioridad de ejecucion de los filtros
        registrationBean.setOrder(0);

        //Retornamos el bean configurado para que spring lo guarde en su contexto (inyeccion)
        return registrationBean;
    }
}
