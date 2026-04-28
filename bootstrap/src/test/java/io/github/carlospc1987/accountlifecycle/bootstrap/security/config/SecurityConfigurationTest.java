package io.github.carlospc1987.accountlifecycle.bootstrap.security.config;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.token.JwtTokenService;
import io.github.carlospc1987.accountlifecycle.bootstrap.security.web.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecurityConfigurationTest {

    @Test
    void givenJwtTokenService_whenCreateJwtAuthenticationFilterBean_thenReturnFilterInstance() {
        SecurityConfiguration configuration = new SecurityConfiguration();
        JwtTokenService jwtTokenService = Mockito.mock(JwtTokenService.class);

        JwtAuthenticationFilter filter = configuration.jwtAuthenticationFilter(jwtTokenService);

        assertNotNull(filter);
    }

    @Test
    void givenHttpSecurityAndJwtFilter_whenBuildSecurityFilterChain_thenReturnBuiltChain() throws Exception {
        SecurityConfiguration configuration = new SecurityConfiguration();
        HttpSecurity http = Mockito.mock(HttpSecurity.class);
        JwtAuthenticationFilter filter = Mockito.mock(JwtAuthenticationFilter.class);
        DefaultSecurityFilterChain builtChain = Mockito.mock(DefaultSecurityFilterChain.class);

        when(http.csrf(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)).thenReturn(http);
        when(http.build()).thenReturn(builtChain);

        SecurityFilterChain chain = configuration.securityFilterChain(http, filter);

        assertNotNull(chain);
        verify(http).csrf(any());
        verify(http).sessionManagement(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        verify(http).build();
    }
}
