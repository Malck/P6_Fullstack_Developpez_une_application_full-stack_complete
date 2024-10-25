package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.DeleteSubjectException;
import com.openclassrooms.mddapi.exception.InvalidSubjectDataException;
import com.openclassrooms.mddapi.exception.SubjectAlreadyExistsException;
import com.openclassrooms.mddapi.exception.SubjectNotFoundException;
import com.openclassrooms.mddapi.exception.UpdateSubjectException;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.service.SubjectService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Contrôleur pour la gestion des sujets, permettant les opérations CRUD (création, lecture, mise à jour, suppression).
 */
@RestController
@RequestMapping("/api/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Autowired
    private UserService userService;

    /**
     * Récupère tous les sujets associés à l'utilisateur actuellement connecté.
     *
     * @return liste des sujets de l'utilisateur.
     */
    @GetMapping
    public List<SubjectDTO> getAllSubjects() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.getUserByName(username);

        return subjectService.getAllSubjects(userDTO.getId());
    }

    /**
     * Récupère un sujet par son identifiant.
     *
     * @param id identifiant du sujet à récupérer.
     * @return le sujet correspondant à l'identifiant, ou une exception si non trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        SubjectDTO subjectDTO = subjectService.getSubjectById(id);
        if (subjectDTO == null) {
            throw new SubjectNotFoundException("Subject with ID " + id + " not found");
        }
        return ResponseEntity.ok(subjectDTO);
    }

    /**
     * Crée un nouveau sujet.
     *
     * @param subjectDTO données du sujet à créer.
     * @return message confirmant la création ou une erreur si l'opération échoue.
     */
    @PostMapping
    public ResponseEntity<String> createSubject(@RequestBody SubjectDTO subjectDTO) {
        if (subjectDTO.getName() == null || subjectDTO.getName().trim().isEmpty()) {
            throw new InvalidSubjectDataException("Invalid Subject data");
        }

        if (subjectService.subjectExists(subjectDTO.getName())) {
            throw new SubjectAlreadyExistsException("Subject with name " + subjectDTO.getName() + " already exists");
        }

        Subject createdSubject = subjectService.createSubject(subjectDTO);
        if (createdSubject != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("New Subject created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Subject");
        }
    }

    /**
     * Met à jour un sujet existant.
     *
     * @param id         identifiant du sujet à mettre à jour.
     * @param subjectDTO nouvelles données du sujet.
     * @return message confirmant la mise à jour ou une exception si une erreur survient.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSubject(@PathVariable Long id, @RequestBody SubjectDTO subjectDTO) {
        if (subjectDTO.getName() == null || subjectDTO.getName().trim().isEmpty()) {
            throw new InvalidSubjectDataException("Invalid Subject data");
        }

        SubjectDTO existingSubject = subjectService.getSubjectById(id);
        if (existingSubject == null) {
            throw new SubjectNotFoundException("Subject with ID " + id + " not found");
        }

        Subject updatedSubject = subjectService.updateSubject(id, subjectDTO);
        if (updatedSubject != null) {
            return ResponseEntity.ok().body("Subject updated");
        } else {
            throw new UpdateSubjectException("Failed to update Subject");
        }
    }

    /**
     * Supprime un sujet par son identifiant.
     *
     * @param id identifiant du sujet à supprimer.
     * @return message confirmant la suppression ou une exception si l'opération échoue.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubjectById(@PathVariable Long id) {
        SubjectDTO subjectDTO = subjectService.getSubjectById(id);
        if (subjectDTO == null) {
            throw new SubjectNotFoundException("Subject with ID " + id + " not found");
        }

        try {
            this.subjectService.deleteSubjectById(id);
            return ResponseEntity.ok().body("{\"message\": \"Subject deleted successfully\"}");
        } catch (Exception e) {
            throw new DeleteSubjectException("Failed to delete Subject with ID " + id);
        }
    }
}

