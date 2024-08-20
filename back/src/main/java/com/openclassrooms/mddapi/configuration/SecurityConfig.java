package com.openclassrooms.mddapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
public class SecurityConfig {

    @Bean   // Chaine de filtre de sécurité
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http.cors() 
                .and().  
                csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                .and()
                .authorizeRequests() 
                .antMatchers("/api/auth/*").permitAll()  
                .antMatchers("/api/**").authenticated()  
                .anyRequest().permitAll() 
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class); // Ajoute le filtre JWT avant le filtre d'authentification par nom d'utilisateur et mot de passe.

        return http.build(); 

    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        
        CorsConfiguration configuration = new CorsConfiguration();

        // Autorise les requêtes provenant de l'URL de mon front-end.
        configuration.addAllowedOrigin("http://localhost:4200"); 

        // Autorise toutes les méthodes HTTP (GET, POST, PUT, etc.).
        configuration.addAllowedMethod("*");

        // Autorise tous les en-têtes dans la requête.
        configuration.addAllowedHeader("*");

        // Création d'une source de configuration basée sur l'URL pour les paramètres CORS.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Enregistrement de la configuration CORS pour toutes les routes.
        source.registerCorsConfiguration("/**", configuration);

        // Retourne la source de configuration CORS pour être utilisée par Spring Security.
        return source;
    }


    @Bean
    public jwtRequestFilter jwtFilter() {
        return new jwtRequestFilter(); // Instancie un nouvel objet de type jwtRequestFilter
    }

    // Définit un bean pour l'encodeur de mot de passe (injecté dans JwtUserDetailsService pour encoder les mots de passe avant de les stocker dans la base de données.)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utilise BCrypt pour encoder les mots de passe
    }

    // Définit un bean pour le gestionnaire d'authentification
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); 
    }

}
