package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.JwtRequest;
import com.openclassrooms.mddapi.model.JwtResponse;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.CustomUserDetails;
import com.openclassrooms.mddapi.service.JwtUserDetailsService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.configuration.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion de l'authentification et des utilisateurs.
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthenticationController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Enregistre un nouvel utilisateur et génère un jeton JWT pour l'utilisateur créé.
     *
     * @param userDTO données de l'utilisateur à enregistrer.
     * @return un jeton JWT pour le nouvel utilisateur.
     */
    @PostMapping("/api/auth/register")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        User user = User.createNewUser(userDTO.getEmail(), userDTO.getUsername(), userDTO.getPassword());
        userDetailsService.save(user);

        final CustomUserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Authentifie un utilisateur et génère un jeton JWT en cas de succès.
     *
     * @param authenticationRequest informations d'authentification.
     * @return un jeton JWT pour l'utilisateur authentifié.
     * @throws Exception en cas d'échec d'authentification.
     */
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getLogin(), authenticationRequest.getPassword());

        final CustomUserDetails userDetails = userDetailsService.loadUserByLogin(authenticationRequest.getLogin());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Authentifie l'utilisateur avec les informations fournies.
     *
     * @param login    identifiant de l'utilisateur.
     * @param password mot de passe de l'utilisateur.
     * @throws Exception si l'utilisateur est désactivé ou les informations sont invalides.
     */
    private void authenticate(String login, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    /**
     * Met à jour les informations d'un utilisateur et génère un jeton JWT pour le profil mis à jour.
     *
     * @param id      identifiant de l'utilisateur.
     * @param userDTO nouvelles informations de l'utilisateur.
     * @return un jeton JWT pour l'utilisateur mis à jour.
     */
    @PutMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(id, userDTO.getEmail(), userDTO.getUsername());

        final CustomUserDetails userDetails = userDetailsService.loadUserByLogin(updatedUserDTO.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }
}

