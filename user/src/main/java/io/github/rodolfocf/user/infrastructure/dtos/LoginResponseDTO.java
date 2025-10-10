package io.github.rodolfocf.user.infrastructure.dtos;

public record LoginResponseDTO(
        String token // JWT gerado para o usuário
) {}
