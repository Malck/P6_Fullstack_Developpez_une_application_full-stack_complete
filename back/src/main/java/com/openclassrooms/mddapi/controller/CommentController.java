package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.exception.*;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour la gestion des commentaires.
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Récupère tous les commentaires.
     *
     * @return liste des commentaires.
     */
    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }

    /**
     * Récupère un commentaire par son identifiant.
     *
     * @param id identifiant du commentaire.
     * @return le commentaire correspondant ou une erreur si introuvable.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO commentDTO = commentService.getCommentById(id);
        if (commentDTO == null) {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }
        return ResponseEntity.ok(commentDTO);
    }

    /**
     * Crée un nouveau commentaire.
     *
     * @param commentDTO données du commentaire à créer.
     * @return message de succès ou d'échec.
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createComment(@Valid @RequestBody CommentDTO commentDTO) {
        Comment createdComment = commentService.createComment(commentDTO);
        Map<String, String> response = new HashMap<>();

        if (createdComment != null) {
            response.put("message", "New Comment created");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "Failed to create Comment");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Met à jour un commentaire existant.
     *
     * @param id identifiant du commentaire.
     * @param commentDTO nouvelles données du commentaire.
     * @return message de succès ou une erreur si introuvable.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateComment(@PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO) {
        CommentDTO existingComment = commentService.getCommentById(id);
        if (existingComment == null) {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }

        Comment updatedComment = commentService.updateComment(id, commentDTO);
        if (updatedComment != null) {
            return ResponseEntity.ok().body("Comment updated");
        } else {
            throw new UpdateCommentException("Failed to update Comment");
        }
    }

    /**
     * Supprime un commentaire par son identifiant.
     *
     * @param id identifiant du commentaire.
     * @return message de confirmation de suppression ou une erreur si introuvable.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long id) {
        CommentDTO commentDTO = commentService.getCommentById(id);
        if (commentDTO == null) {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }

        try {
            this.commentService.deleteCommentById(id);
            return ResponseEntity.ok().body("{\"message\": \"Comment deleted successfully\"}");
        } catch (Exception e) {
            throw new DeleteCommentException("Failed to delete Comment with ID " + id);
        }
    }
}

