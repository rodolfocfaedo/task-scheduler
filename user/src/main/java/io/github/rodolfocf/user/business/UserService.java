package io.github.rodolfocf.user.business;

import io.github.rodolfocf.user.infrastructure.converter.UserMapper;
import io.github.rodolfocf.user.infrastructure.dtos.UserRequestDTO;
import io.github.rodolfocf.user.infrastructure.dtos.UserResponseDTO;
import io.github.rodolfocf.user.infrastructure.entities.User;
import io.github.rodolfocf.user.infrastructure.exceptions.ConflictException;
import io.github.rodolfocf.user.infrastructure.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        try {
            emailExist(userRequestDTO.getEmail());
            UserRequestDTO encodedRequest = new UserRequestDTO(
                    userRequestDTO.getEmail(),
                    passwordEncoder.encode(userRequestDTO.getPassword())
            );

            User userEntity = userMapper.toUSerEntity(encodedRequest);
            return userMapper.toUserDTO(userRepository.saveAndFlush(userEntity));
        }catch(ConflictException conflictException){
            throw new ConflictException("Email " + userRequestDTO.getEmail() + " is already registered");
        }
    }

    public boolean checkExistingEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void emailExist(String email){
        try{
            boolean existingEmail = checkExistingEmail(email);
            if(existingEmail){
                throw new ConflictException("Email " + email + " is already registered");
            }
        }catch(ConflictException conflictException){
            throw new ConflictException("Email " + email + " is already registered");
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public void deleteUserByEmail(String email){
        userRepository.deleteByEmail(email);
    }




}
