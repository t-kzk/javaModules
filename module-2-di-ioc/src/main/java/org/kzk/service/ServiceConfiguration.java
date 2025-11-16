package org.kzk.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean(name = "rootDirectory")
    public String rootDir() {
        return "di-ioc/";
    }
}
