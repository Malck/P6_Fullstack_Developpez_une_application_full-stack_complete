package com.openclassrooms.mddapi.exception.globalException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.openclassrooms.mddapi.exception.ArticleNotFoundException;
import com.openclassrooms.mddapi.exception.CommentNotFoundException;
import com.openclassrooms.mddapi.exception.DeleteArticleException;
import com.openclassrooms.mddapi.exception.DeleteCommentException;
import com.openclassrooms.mddapi.exception.DeleteSubjectException;
import com.openclassrooms.mddapi.exception.InvalidArticleDataException;
import com.openclassrooms.mddapi.exception.InvalidCommentDataException;
import com.openclassrooms.mddapi.exception.InvalidSubjectDataException;
import com.openclassrooms.mddapi.exception.SubjectAlreadyExistsException;
import com.openclassrooms.mddapi.exception.SubjectNotFoundException;
import com.openclassrooms.mddapi.exception.UpdateArticleException;
import com.openclassrooms.mddapi.exception.UpdateCommentException;
import com.openclassrooms.mddapi.exception.UpdateSubjectException;
import com.openclassrooms.mddapi.exception.UserAlreadyExistsException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SubjectAlreadyExistsException.class)
    public ResponseEntity<String> handleSubjectAlreadyExistsException(SubjectAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<String> handleSubjectNotFoundException(SubjectNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UpdateSubjectException.class)
    public ResponseEntity<String> handleUpdateSubjectException(UpdateSubjectException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(DeleteSubjectException.class)
    public ResponseEntity<String> handleDeleteSubjectException(DeleteSubjectException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidSubjectDataException.class)
    public ResponseEntity<String> handleInvalidSubjectDataException(InvalidSubjectDataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<String> handleArticleNotFoundException(ArticleNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UpdateArticleException.class)
    public ResponseEntity<String> handleUpdateArticleException(UpdateArticleException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteArticleException.class)
    public ResponseEntity<String> handleDeleteArticleException(DeleteArticleException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidArticleDataException.class)
    public ResponseEntity<String> handleInvalidArticleDataException(InvalidArticleDataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handleCommentNotFoundException(CommentNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteCommentException.class)
    public ResponseEntity<String> handleDeleteCommentException(DeleteCommentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidCommentDataException.class)
    public ResponseEntity<String> handleInvalidCommentDataException(InvalidCommentDataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateCommentException.class)
    public ResponseEntity<String> handleUpdateCommentException(UpdateCommentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
