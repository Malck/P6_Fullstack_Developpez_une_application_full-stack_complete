package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur pour la gestion des abonnements des utilisateurs aux sujets.
 */
@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Vérifie si un utilisateur est abonné à un sujet.
     *
     * @param subjectId identifiant du sujet.
     * @param userId    identifiant de l'utilisateur.
     * @return {@code true} si l'utilisateur est abonné, {@code false} sinon.
     */
    @GetMapping("/{subjectId}/{userId}")
    public ResponseEntity<Boolean> isSubscribed(@PathVariable Long subjectId, @PathVariable Long userId) {
        boolean isSubscribed = subscriptionService.isSubscribed(subjectId, userId);
        return ResponseEntity.ok(isSubscribed);
    }

    /**
     * Abonne un utilisateur à un sujet.
     *
     * @param request carte contenant les identifiants du sujet et de l'utilisateur.
     * @return message confirmant l'abonnement.
     */
    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribeToSubject(@RequestBody Map<String, Long> request) {
        Long subjectId = request.get("subjectId");
        Long userId = request.get("userId");
        subscriptionService.subscribeToSubject(subjectId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Subscribed successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Désabonne un utilisateur d'un sujet.
     *
     * @param subjectId identifiant du sujet.
     * @param userId    identifiant de l'utilisateur.
     * @return message confirmant le désabonnement.
     */
    @DeleteMapping("/unsubscribe/{subjectId}/{userId}")
    public ResponseEntity<Map<String, String>> unsubscribeSubject(@PathVariable Long subjectId, @PathVariable Long userId) {
        subscriptionService.unsubscribeSubject(subjectId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Unsubscribed successfully");

        return ResponseEntity.ok(response);
    }
}

