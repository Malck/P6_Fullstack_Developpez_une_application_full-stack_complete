package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.UpdateSubjectException;
import com.openclassrooms.mddapi.exception.UpdateUserException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des utilisateurs.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @param userDTO données de l'utilisateur à créer.
     * @return message confirmant la création de l'utilisateur ou un message d'erreur.
     */
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUserDTO = userService.createUser(userDTO.getEmail(), userDTO.getUsername(), userDTO.getPassword());

        if (createdUserDTO != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("New User created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create User");
        }
    }

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur.
     * @return l'utilisateur correspondant ou une exception s'il n'est pas trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            throw new UserNotFoundException("User not found with id: " + id); 
        } else {
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
    }

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur.
     * @return réponse sans contenu après suppression.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Récupère l'utilisateur actuellement connecté.
     *
     * @return les informations de l'utilisateur connecté ou une exception s'il n'est pas trouvé.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.getUserByName(username);

        if (userDTO == null) {
            throw new UserNotFoundException("User not found with userName: " + username); 
        } else {
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
    }
}
