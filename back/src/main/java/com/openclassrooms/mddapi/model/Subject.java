package com.openclassrooms.mddapi.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subjects", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) 
    private String name;

    public Subject(String name) {
        this.name = name;
    }

    @Lob // permet de stocker de longs textes
    @Column(nullable = false, length = 5000) 
    private String description;

    // Methode pour debugging
    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static Subject createNewSubject(String name) {
        Subject subject = new Subject();
        subject.setName(name);
        return subject;    
    }  
}
