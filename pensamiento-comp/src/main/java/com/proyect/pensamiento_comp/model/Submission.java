package com.proyect.pensamiento_comp.model;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DEPRECATED: This JPA entity is not used anymore.
 * Submission data is now stored in MongoDB using SubmissionDocument.
 * Keeping this class for reference only.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @Entity  // COMMENTED OUT - Submission is now in MongoDB only
// @Table(name = "SUBMISSION")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "submission_seq")
    @SequenceGenerator(name = "submission_seq", sequenceName = "SUBMISSION_SEQ", allocationSize = 1)
    private Long id;
    private String link;
    private LocalDateTime date;
    private String status;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "STUDENT_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "EXERCISE_id")
    private Exercise exercise;

    @OneToMany(mappedBy = "submission")
    private List<Score> scores;


    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private List<File> files;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
