package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.dto.UserDTO;

public class UserMapper {

    // Transforme une entité User en DTO
    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getUsername(), user.getPassword());
    }

    // Transforme un DTO en entité User
    public static User toEntity(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getEmail(), userDTO.getUsername(), userDTO.getPassword());
    }
}
