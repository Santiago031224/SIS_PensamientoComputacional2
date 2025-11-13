package com.proyect.pensamiento_comp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PROFESSOR")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "professor_seq")
    @SequenceGenerator(name = "professor_seq", sequenceName = "PROFESSOR_SEQ", allocationSize = 1)
    private Long id;
    
    @Column(name = "OFFICE_LOCATION")
    private String officeLocation;
    @Column(name = "SPECIALTY_AREA")
    private String specialtyArea;
    @Column(name = "MAX_GROUPS_ALLOWED")
    private Integer maxGroupsAllowed;
    @Column(name = "ACADEMIC_RANK")
    private String academicRank;

    @OneToOne
    @JoinColumn(name = "USER_id", nullable = false)
    @JsonIgnore
    private User user;

    // Relaciones
    // COMMENTED: Activity is now MongoDB-only, cannot have JPA relationship
    // @OneToMany(mappedBy = "professor")
    // private List<Activity> activities;

    @OneToMany(mappedBy = "professor")
    private List<TeachingAssignment> teachingAssignments;


}
