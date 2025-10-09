package io.github.rodolfocf.user.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "jwt") // Lê propriedades do application.yml com prefixo "jwt"
public record JwtProperties(
        String secret,       // usado em HMAC
        long expiration,     // tempo de expiração em ms
        String algorithm,    // "HMAC" ou "RSA"
        String privateKey,   // chave privada em Base64 (RSA)
        String publicKey     // chave pública em Base64 (RSA)
) {
}
