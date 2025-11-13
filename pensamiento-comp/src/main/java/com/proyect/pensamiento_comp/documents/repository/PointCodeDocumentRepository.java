package com.proyect.pensamiento_comp.documents.repository;

import com.proyect.pensamiento_comp.documents.PointCodeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PointCodeDocumentRepository extends MongoRepository<PointCodeDocument, String> {
    List<PointCodeDocument> findByActivityId(String activityId);
    List<PointCodeDocument> findByProfessorId(Long professorId);
    List<PointCodeDocument> findByStatus(String status);
}
