package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.ArticleWithCommentsDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.*;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Contrôleur pour gérer les opérations CRUD d'articles dans l'application.
 */
@RestController
@RequestMapping("/api/article")
public class ArticleController {
    
    private final ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
    
    /**
     * Récupère les articles auxquels l'utilisateur connecté est abonné.
     *
     * @return Liste des articles sous forme de DTO.
     */
    @GetMapping
    public List<ArticleDTO> subscribedArticles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.getUserByName(username);

        List<ArticleDTO> subscribedArticles = articleService.getSubscribedArticlesForUser(userDTO.getId());
        return subscribedArticles;
    }

    /**
     * Récupère un article spécifique avec ses commentaires par son ID.
     *
     * @param id L'ID de l'article à récupérer.
     * @return L'article avec ses commentaires sous forme de DTO.
     * @throws ArticleNotFoundException Si aucun article n'est trouvé avec l'ID donné.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleWithCommentsDTO> getArticleById(@PathVariable Long id) {
        ArticleWithCommentsDTO articleWithCommentsDTO = articleService.getArticleById(id);
        if (articleWithCommentsDTO == null) {
            throw new ArticleNotFoundException("Article with ID " + id + " not found");
        }
        return ResponseEntity.ok(articleWithCommentsDTO);
    }

    /**
     * Crée un nouvel article.
     *
     * @param articleDTO Les détails de l'article à créer.
     * @return Un message de confirmation de la création de l'article.
     */
    @PostMapping
    public ResponseEntity<String> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        Article createdArticle = articleService.createArticle(articleDTO);
        if (createdArticle != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("New Article created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Article");
        }
    }

    /**
     * Met à jour un article existant.
     *
     * @param id L'ID de l'article à mettre à jour.
     * @param articleDTO Les détails mis à jour de l'article.
     * @return Un message de confirmation de la mise à jour de l'article.
     * @throws ArticleNotFoundException Si l'article à mettre à jour n'est pas trouvé.
     * @throws UpdateArticleException Si la mise à jour échoue.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDTO articleDTO) {
        ArticleWithCommentsDTO existingArticle = articleService.getArticleById(id);
        if (existingArticle == null) {
            throw new ArticleNotFoundException("Article with ID " + id + " not found");
        }

        Article updatedArticle = articleService.updateArticle(id, articleDTO);
        if (updatedArticle != null) {
            return ResponseEntity.ok().body("Article updated");
        } else {
            throw new UpdateArticleException("Failed to update Article");
        }
    }

    /**
     * Supprime un article par son ID.
     *
     * @param id L'ID de l'article à supprimer.
     * @return Un message de confirmation de la suppression de l'article.
     * @throws ArticleNotFoundException Si aucun article n'est trouvé avec l'ID donné.
     * @throws DeleteArticleException Si la suppression échoue.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticleById(@PathVariable Long id) {
        ArticleWithCommentsDTO articleWithCommentsDTO = articleService.getArticleById(id);
        if (articleWithCommentsDTO == null) {
            throw new ArticleNotFoundException("Article with ID " + id + " not found");
        }

        try {
            this.articleService.deleteArticleById(id);
            return ResponseEntity.ok().body("{\"message\": \"Article deleted successfully\"}");
        } catch (Exception e) {
            throw new DeleteArticleException("Failed to delete Article with ID " + id);
        }
    }
}
