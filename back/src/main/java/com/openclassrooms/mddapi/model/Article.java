package com.openclassrooms.mddapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "articles", uniqueConstraints = {
    @UniqueConstraint(columnNames = "title")
})
@Data 
@NoArgsConstructor
@AllArgsConstructor 
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author; 

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject; 

    @Column(nullable = false)   
    private String title;

    @Lob    // permet de stocker de longs textes
    @Column(nullable = false, length = 5000) 
    private String content;

    @Temporal(TemporalType.TIMESTAMP)   //spécifie que la propriété publishedAt doit être persistée en tant que TIMESTAMP SQL
    private Date publishedAt;

    @PrePersist //méthode de rappel qui sera exécutée juste avant qu'une entité soit initialement persistée
    protected void onCreate() {
        publishedAt = new Date();   
    }

    public static Article createNewArticle(String title, String content, Subject subject, User author) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setSubject(subject);
        article.setAuthor(author);
        return article;
    }

}
