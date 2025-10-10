package io.github.rodolfocf.user.controller;

import io.github.rodolfocf.user.business.UserService;
import io.github.rodolfocf.user.infrastructure.converter.UserMapper;
import io.github.rodolfocf.user.infrastructure.dtos.UserRequestDTO;
import io.github.rodolfocf.user.infrastructure.dtos.UserResponseDTO;
import io.github.rodolfocf.user.infrastructure.repositories.UserRepository;
import io.github.rodolfocf.user.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRequestDTO));
    }


    @GetMapping
    public ResponseEntity<UserResponseDTO> getUserByEmail(@RequestParam("email") String email) {
        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(userMapper.toUserDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserByEmail(@RequestParam("email") String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
