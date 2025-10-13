package io.github.rodolfocf.taskscheduler.infrastructure.security;


import io.github.rodolfocf.taskscheduler.infrastructure.client.UserClient;
import io.github.rodolfocf.taskscheduler.infrastructure.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementação personalizada do UserDetailsService do Spring Security.
 * <p>
 * Esta classe é responsável por carregar informações de usuário durante o processo
 * de autenticação. O Spring Security usa esta implementação para:
 * 1. Validar credenciais durante o login
 * 2. Carregar informações do usuário para validação de tokens JWT
 * 3. Verificar permissões e roles do usuário
 */
@Service // Marca como componente de serviço gerenciado pelo Spring
@RequiredArgsConstructor // Lombok: gera construtor com campos "final" automaticamente
public class UserDetailsServiceImpl {

    // Logger para registrar eventos importantes (login attempts, erros, etc.)
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserClient userClient;


    public UserDetails loadDataUser(String email, String token) {
        UserResponseDTO userResponseDTO = userClient.getUserByEmail(email, token);
        return User
                .withUsername(userResponseDTO.getEmail())     // Define email como identificador único
                .password(userResponseDTO.getPassword())      // Senha já deve estar criptografada (BCrypt)
                .build();                         // Constrói o objeto final UserDetails
    }
}
