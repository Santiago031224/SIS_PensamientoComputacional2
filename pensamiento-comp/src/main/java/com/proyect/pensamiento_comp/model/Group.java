package com.proyect.pensamiento_comp.model;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"GROUP\"")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_seq")
    @SequenceGenerator(name = "group_seq", sequenceName = "GROUP_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PERIOD_id")
    private Period period;

    @ManyToMany(mappedBy = "groups")
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private List<TeachingAssignment> teachingAssignments;
}
