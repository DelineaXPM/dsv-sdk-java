package com.delinea.secrets.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;

@SpringBootTest
class ApplicationTests {
    @Configuration
    @ComponentScan("com.delinea.secrets")
    public static class Config {
        @Bean
        public ClientHttpRequestFactory clientRequestFactory() {
            return new JdkClientHttpRequestFactory();
        }
    }

    @Test
    void contextLoads() {
    }
}
