package io.github.carlospc1987.accountlifecycle.bootstrap.security.web;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.token.JwtClaims;
import io.github.carlospc1987.accountlifecycle.bootstrap.security.token.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = Objects.requireNonNull(jwtTokenService, "jwtTokenService is required");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();
        try {
            JwtClaims claims = jwtTokenService.validateAndExtract(token);
            var authorities = claims.roles().stream().map(SimpleGrantedAuthority::new).toList();
            var authentication = new UsernamePasswordAuthenticationToken(claims.subject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (IllegalArgumentException ex) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
