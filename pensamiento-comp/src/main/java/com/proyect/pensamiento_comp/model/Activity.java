package com.proyect.pensamiento_comp.model;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DEPRECATED: This JPA entity is not used anymore.
 * Activity data is now stored in MongoDB using ActivityDocument.
 * Keeping this class for reference only.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @Entity  // COMMENTED OUT - Activity is now in MongoDB only
// @Table(name = "ACTIVITY")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_seq")
    @SequenceGenerator(name = "activity_seq", sequenceName = "ACTIVITY_SEQ", allocationSize = 1)
    private Long id;
    private String title;
    @Lob
    private String description;

    @Column(name = "START_DATE")
    private LocalDate startDate;
    @Column(name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "PROFESSOR_id")
    private Professor professor;

    @ManyToMany(mappedBy = "activities")
    private Set<Exercise> exercises = new HashSet<>();

    @OneToMany(mappedBy = "activity")
    private List<PointCode> pointCodes;

    @OneToMany(mappedBy = "activity")
    private List<ActivityExercise> activityExercises;


    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private List<File> files;

}
