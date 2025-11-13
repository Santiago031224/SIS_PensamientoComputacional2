package com.proyect.pensamiento_comp.repository;

import com.proyect.pensamiento_comp.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByUserId(Long userId);
}

