package io.github.rodolfocf.user.infrastructure.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email @NotBlank String email,    // Email do usuário
        @NotBlank String password         // Senha do usuário
) {
}
