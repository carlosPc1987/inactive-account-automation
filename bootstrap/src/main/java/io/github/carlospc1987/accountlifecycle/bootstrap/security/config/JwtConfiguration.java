package io.github.carlospc1987.accountlifecycle.bootstrap.security.config;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.token.DefaultJwtTokenService;
import io.github.carlospc1987.accountlifecycle.bootstrap.security.token.JwtTokenService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {

    @Bean
    JwtTokenService jwtTokenService(JwtProperties jwtProperties) {
        return new DefaultJwtTokenService(jwtProperties);
    }
}
