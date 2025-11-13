package com.proyect.pensamiento_comp.repository;

import com.proyect.pensamiento_comp.model.Student;
import com.proyect.pensamiento_comp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
}
