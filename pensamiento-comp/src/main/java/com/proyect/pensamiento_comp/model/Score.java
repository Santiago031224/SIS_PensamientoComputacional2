package com.proyect.pensamiento_comp.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DEPRECATED: This JPA entity is not used anymore.
 * Score data is managed within MongoDB (submissions.grading).
 * Keeping this class for reference only.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @Entity  // COMMENTED OUT - Score is now part of MongoDB documents
// @Table(name = "SCORE")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "score_seq")
    @SequenceGenerator(name = "score_seq", sequenceName = "SCORE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "POINTS_AWARDED")
    private Double pointsAwarded;
    @Column(name = "ASSIGNMENT_DATE")
    private LocalDateTime assignmentDate;

    @ManyToOne
    @JoinColumn(name = "SUBMISSION_id")
    private Submission submission;
}
