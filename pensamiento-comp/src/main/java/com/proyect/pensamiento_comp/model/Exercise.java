package com.proyect.pensamiento_comp.model;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DEPRECATED: This JPA entity is not used anymore.
 * Exercise data is now stored in MongoDB using ExerciseDocument.
 * Keeping this class for reference only.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @Entity  // COMMENTED OUT - Exercise is now in MongoDB only
// @Table(name = "EXERCISE")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_seq")
    @SequenceGenerator(name = "exercise_seq", sequenceName = "EXERCISE_SEQ", allocationSize = 1)
    private Long id;

    @Lob
    private String title;
    private String description;
    private String difficulty;
    private String programming_language;

    @ManyToMany(mappedBy = "exercises")
    private Set<Level> levels = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "ACTIVITY_EXERCISE",
        joinColumns = @JoinColumn(name = "EXERCISE_id"),
        inverseJoinColumns = @JoinColumn(name = "ACTIVITY_id")
    )
    private Set<Activity> activities = new HashSet<>();

    @OneToMany(mappedBy = "exercise")
    private List<Submission> submissions;

    @OneToMany(mappedBy = "exercise")
    private List<ActivityExercise> activityExercises;

}
