package com.proyect.pensamiento_comp.repository;

import com.proyect.pensamiento_comp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICourseRepository extends JpaRepository<Course, Long> {}

