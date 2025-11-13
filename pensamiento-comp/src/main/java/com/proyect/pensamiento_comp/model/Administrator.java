package com.proyect.pensamiento_comp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ADMINISTRATOR")
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "administrator_seq")
    @SequenceGenerator(name = "administrator_seq", sequenceName = "ADMINISTRATOR_SEQ", allocationSize = 1)
    private Long id;

    private String department;

    @OneToOne
    @JoinColumn(name = "USER_id", nullable = false)
    @JsonIgnore
    private User user;
}
