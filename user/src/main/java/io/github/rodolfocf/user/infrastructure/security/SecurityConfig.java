package io.github.rodolfocf.user.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
// Indica que esta classe é uma configuração do Spring e será carregada no contexto da aplicação

@EnableWebSecurity
// Ativa o suporte de segurança do Spring Security

@RequiredArgsConstructor
// Gera automaticamente um construtor com todos os atributos finais, permitindo injeção de dependências

public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    // Filtro personalizado que valida o JWT em cada requisição

    private final UserDetailsService userDetailsService;
    // Serviço que o Spring usa para buscar os dados do usuário (neste caso, UserService)

    @Bean
    // Indica que este método cria e registra um bean no contexto do Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configura a cadeia de filtros de segurança que será aplicada em todas as requisições

        return http
                .csrf(AbstractHttpConfigurer::disable)
                // Desabilita a proteção CSRF (não necessária em APIs REST que usam JWT)

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/auth/**").permitAll()
                                // Libera todas as rotas que começam com /auth
                                // 🔧 ALTERAR AQUI se você mudar o prefixo dos endpoints de autenticação (ex: /api/auth)

                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                // Libera rotas do Swagger para facilitar testes e documentação
                                // 🔧 REMOVER AQUI se não usar Swagger neste projeto

                                .anyRequest().authenticated()
                        // Qualquer outra requisição precisa de autenticação com JWT válido
                        // 🔧 ALTERAR AQUI se quiser deixar outros endpoints públicos ou exigir roles específicas
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Define que não haverá sessão no servidor, apenas autenticação stateless via JWT

                .authenticationProvider(authenticationProvider())
                // Registra o provedor de autenticação (quem valida login e senha)

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                // Adiciona o filtro JWT antes do filtro padrão de autenticação do Spring

                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Cria o provedor padrão de autenticação baseado em DAO.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    // Cria o bean de PasswordEncoder
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // Usa o algoritmo BCrypt para encriptar e validar senhas
    }

    @Bean
    // Cria o bean de AuthenticationManager
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
        // Recupera o gerenciador de autenticação configurado pelo Spring
    }
}
