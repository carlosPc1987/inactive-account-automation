package io.github.carlospc1987.accountlifecycle.bootstrap.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String secret = "";
    private long expirationMinutes = 60;
    private String issuer = "account-lifecycle-manager";
    private String algorithm = "HS256";
    private List<String> requiredClaims = List.of("sub", "roles", "iat", "exp");

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMinutes() {
        return expirationMinutes;
    }

    public void setExpirationMinutes(long expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public List<String> getRequiredClaims() {
        return requiredClaims;
    }

    public void setRequiredClaims(List<String> requiredClaims) {
        this.requiredClaims = requiredClaims;
    }
}
