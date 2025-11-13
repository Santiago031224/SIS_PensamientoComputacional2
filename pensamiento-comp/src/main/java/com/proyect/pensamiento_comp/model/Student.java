package com.proyect.pensamiento_comp.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "STUDENT")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @SequenceGenerator(name = "student_seq", sequenceName = "STUDENT_SEQ", allocationSize = 1)
    private Long id;

    private String code;
    private Double gpa;

    @OneToOne
    @JoinColumn(name = "USER_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "LEVEL_id")
    private Level level;

    // COMMENTED: PointCode and Submission are MongoDB-only, cannot have JPA relationships
    // @OneToMany(mappedBy = "student")
    // private List<PointCode> pointCodes;

    // @OneToMany(mappedBy = "student")
    // private List<Submission> submissions;

    @ManyToMany
    @JoinTable(
        name = "STUDENT_GROUP",
        joinColumns = @JoinColumn(name = "STUDENT_id"),
        inverseJoinColumns = @JoinColumn(name = "GROUP_id")
    )
    private Set<Group> groups = new HashSet<>();
}
