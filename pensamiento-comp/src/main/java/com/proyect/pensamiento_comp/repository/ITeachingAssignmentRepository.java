package com.proyect.pensamiento_comp.repository;

import com.proyect.pensamiento_comp.model.TeachingAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITeachingAssignmentRepository extends JpaRepository<TeachingAssignment, Long> {
    List<TeachingAssignment> findByProfessorId(Long professorId);
    List<TeachingAssignment> findByGroupId(Long groupId);
    
    @Query("SELECT DISTINCT ta FROM TeachingAssignment ta " +
           "LEFT JOIN FETCH ta.course " +
           "LEFT JOIN FETCH ta.professor " +
           "LEFT JOIN FETCH ta.group")
    List<TeachingAssignment> findAllWithRelations();
}
