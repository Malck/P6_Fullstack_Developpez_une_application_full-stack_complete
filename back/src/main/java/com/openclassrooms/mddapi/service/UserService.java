package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.exception.UserAlreadyExistsException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(String email, String username, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("An account with this email already exists");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("An account with this username already exists");
        }

        User user = User.createNewUser(email, username, password);
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);  // Utilisation du mapper
    }

    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserMapper::toDTO).orElse(null);  // Utilisation du mapper
    }

    public UserDTO getUserByName(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(UserMapper::toDTO).orElse(null);  // Utilisation du mapper
    }

    public UserDTO updateUser(Long id, String email, String username) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }

        User user = existingUser.get();
        user.setEmail(email);
        user.setUsername(username);
        userRepository.save(user);
        return UserMapper.toDTO(user);  // Utilisation du mapper
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
