package io.github.rodolfocf.user.infrastructure.converter;

import io.github.rodolfocf.user.infrastructure.dtos.UserRequestDTO;
import io.github.rodolfocf.user.infrastructure.dtos.UserResponseDTO;
import io.github.rodolfocf.user.infrastructure.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUSerEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toUserDTO(User user);

}
