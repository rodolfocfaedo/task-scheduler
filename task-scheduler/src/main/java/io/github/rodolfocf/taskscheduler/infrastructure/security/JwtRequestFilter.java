package io.github.rodolfocf.taskscheduler.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Indica que essa classe será gerenciada pelo Spring
@RequiredArgsConstructor // Injeta dependências obrigatórias
public class JwtRequestFilter extends OncePerRequestFilter { // Executa apenas uma vez por requisição

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Método principal do filtro que será executado em cada requisição

        String authHeader = request.getHeader("Authorization");
        // Captura o cabeçalho Authorization

        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            // Remove "Bearer " do início para pegar só o token
            email = jwtService.getEmailFromToken(token);
            // 🔧 ALTERAR AQUI se você usar outro campo como principal no token
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsServiceImpl.loadDataUser(email, token);
            // Busca os dados do usuário no banco

            if (jwtService.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // Autentica o usuário no contexto do Spring
            }
        }

        filterChain.doFilter(request, response);
        // Continua a cadeia de filtros
    }
}
