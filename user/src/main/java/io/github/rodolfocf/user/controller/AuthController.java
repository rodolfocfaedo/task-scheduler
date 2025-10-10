package io.github.rodolfocf.user.controller;

import io.github.rodolfocf.user.business.UserService;
import io.github.rodolfocf.user.infrastructure.dtos.LoginRequestDTO;
import io.github.rodolfocf.user.infrastructure.dtos.LoginResponseDTO;
import io.github.rodolfocf.user.infrastructure.entities.User;
import io.github.rodolfocf.user.infrastructure.repositories.UserRepository;
import io.github.rodolfocf.user.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.email(),
                            loginRequestDTO.password()
                    )
            );

            User user = userService.findByEmail(loginRequestDTO.email());

            String token = jwtService.generateToken(user.getEmail());

            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO("Invalid credentials"));
        }
    }


}


