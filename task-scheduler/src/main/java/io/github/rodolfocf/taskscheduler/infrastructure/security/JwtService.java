package io.github.rodolfocf.taskscheduler.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class JwtService {

    // 🔐 Chave secreta para assinatura HMAC
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // ⏱️ Tempo de expiração do token em milissegundos
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Gera a chave de assinatura HMAC (para HS256)
     * Caso você mude para RSA futuramente, altere o retorno para PrivateKey/PublicKey.
     */
    private SecretKey getSigningKey() {
        // Se o segredo estiver codificado em Base64:
        // return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        // Caso seja texto simples:
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Valida a assinatura e expiração do token.
     * Retorna true se o token for válido, false caso contrário.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()                         // novo parser (sem parserBuilder)
                    .verifyWith(getSigningKey())       // substitui setSigningKey()
                    .build()                           // constrói o parser
                    .parseSignedClaims(token);         // valida assinatura e expiração
            return true;
        } catch (JwtException ex) {
            // JwtException cobre assinatura inválida, token expirado, etc.
            return false;
        }
    }

    /**
     * Extrai os claims (payload) do token já validado.
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload(); // retorna o conteúdo do JWT
    }

    /**
     * Obtém o subject (normalmente o e-mail do usuário) do token.
     */
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }
}
