package com.thycotic.secrets.spring;

import com.thycotic.secrets.vault.spring.SecretsVault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.thycotic.secrets.vault.spring")
public class Application {
    private final Logger log = LoggerFactory.getLogger(Application.class);

    @Value("${secret.path:#null}")
    private String secretPath;

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner runVault(final SecretsVault secretsVault) throws Exception {
        return args -> {
            log.info("running with args \"{}\"; getSecret(\"{}\") -> {}", args, secretPath,
                    secretsVault.getSecret(secretPath).toString());
        };
    }
}
