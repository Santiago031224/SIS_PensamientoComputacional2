package com.proyect.pensamiento_comp.documents.repository;

import com.proyect.pensamiento_comp.documents.ExerciseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ExerciseDocumentRepository extends MongoRepository<ExerciseDocument, String> {
    Optional<ExerciseDocument> findByRelationalId(Long relationalId);
    void deleteByRelationalId(Long relationalId);
}
