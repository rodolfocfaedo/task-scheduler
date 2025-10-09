package io.github.rodolfocf.user.infrastructure.security;

import io.github.rodolfocf.user.infrastructure.entities.User;
import io.github.rodolfocf.user.infrastructure.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

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
public class UserDetailsServiceImpl implements UserDetailsService {


    // Repositório para acesso aos dados de usuário no banco de dados
    // Final + @RequiredArgsConstructor = injeção de dependência automática
    private final UserRepository userRepository;

    /**
     * Método principal do UserDetailsService - carrega usuário por email/username.
     * <p>
     * Este método é chamado pelo Spring Security durante:
     * - Processo de login (AuthenticationManager)
     * - Validação de tokens JWT (JwtRequestFilter)
     * - Verificação de permissões
     *
     * @param email O email do usuário (usado como username no sistema)
     * @return UserDetails objeto com informações de autenticação e autorização
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // === VALIDAÇÃO DE ENTRADA ===
        // Verifica se o email não está vazio ou nulo
        if (!StringUtils.hasText(email)) {
            throw new UsernameNotFoundException("Email cannot be null");
        }

        // === BUSCA NO BANCO DE DADOS ===
        // Busca o usuário no repositório pelo email
        // Optional.orElseThrow() lança exceção se não encontrar
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    // Log da falha de autenticação (importante para segurança)
                    return new UsernameNotFoundException("Email not found: " + email);
                });


        // === CONSTRUÇÃO DO OBJETO USERDETAILS ===
        // Cria o objeto UserDetails que o Spring Security usa internamente
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())     // Define email como identificador único
                .password(user.getPassword())      // Senha já deve estar criptografada (BCrypt)
//                .disabled(!user.isActive())       // Conta ativa/inativa
//                .accountExpired(user.isAccountExpired())   // Conta não expirada (implementar lógica se necessário)
//                .accountLocked(user.isAccountLocked())     // Conta não bloqueada (implementar lógica se necessário)
//                .credentialsExpired(user.isCredentialsExpired())   // Credenciais não expiradas (implementar se necessário)
                //.authorities(user.getRoles())     // Roles/permissões do usuário (se implementadas)
                .build();                         // Constrói o objeto final UserDetails
    }

    /**
     * Método auxiliar para verificar se um usuário existe por email.
     * Útil para validações antes de operações que dependem da existência do usuário.
     *
     * @param email O email a ser verificado
     * @return true se o usuário existir, false caso contrário
     */
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }

        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Método auxiliar para carregar usuário sem lançar exceção.
     * Útil quando você quer verificar se um usuário existe sem interromper o fluxo.
     *
     * @param email O email do usuário
     * @return Optional<UserDetails> - presente se encontrado, vazio se não
     */
    public Optional<UserDetails> loadUserByUsernameOptional(String email) {
        try {
            return Optional.of(loadUserByUsername(email));
        } catch (UsernameNotFoundException e) {

            return Optional.empty();
        }
    }
}
