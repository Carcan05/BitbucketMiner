package com.aiss.bitbucketminer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Value("${bitbucket.api.username}")
    private String username;

    @Value("${bitbucket.api.app-password}")
    private String appPassword;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rest = new RestTemplate();
        // Añadimos autenticación básica para Bitbucket
        rest.getInterceptors()
                .add(new BasicAuthenticationInterceptor(username, appPassword));
        return rest;
    }
}


