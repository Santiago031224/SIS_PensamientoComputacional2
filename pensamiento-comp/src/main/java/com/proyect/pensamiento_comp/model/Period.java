package com.proyect.pensamiento_comp.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PERIOD")
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "period_seq")
    @SequenceGenerator(name = "period_seq", sequenceName = "PERIOD_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "CODE")
    private String code;
    
    @Column(name = "START_DATE")
    private LocalDate startDate;
    
    @Column(name = "END_DATE")
    private LocalDate endDate;

    @OneToMany(mappedBy = "period")
    private List<Group> groups;
}
