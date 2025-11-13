package com.proyect.pensamiento_comp.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"LEVEL\"")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "level_seq")
    @SequenceGenerator(name = "level_seq", sequenceName = "LEVEL_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", length = 20, nullable = false)
    private String name;
    
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "level")
    private List<Student> students;

    // DEPRECATED: Exercise is now in MongoDB only
    // This relation is no longer maintained in Oracle
    // @ManyToMany
    // @JoinTable(
    //     name = "EXERCISE_LEVEL",
    //     joinColumns = @JoinColumn(name = "LEVEL_id"),
    //     inverseJoinColumns = @JoinColumn(name = "EXERCISE_id")
    // )
    // private Set<Exercise> exercises = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
