package com.greenew.produtores.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced // Habilita a integração com o Eureka Load Balancer
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}