package com.proyect.pensamiento_comp.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TEACHING_ASSIGNMENT")
public class TeachingAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teaching_assignment_seq")
    @SequenceGenerator(name = "teaching_assignment_seq", sequenceName = "TEACHING_ASSIGNMENT_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "ASSIGNED_DATE")
    private LocalDate assignedDate;
    private String status;
    @Column(name = "EVALUATION_WEIGHT")
    private Double evaluationWeight;
    @Column(name = "ROLE_IN_COURSE")
    private String roleInCourse;
    @Column(name = "CLASSROOM_LOCATION")
    private String classroomLocation;

    @Lob
    private String notes;

    @ManyToOne
    @JoinColumn(name = "COURSE_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "PROFESSOR_id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "GROUP_id")
    private Group group;
}
