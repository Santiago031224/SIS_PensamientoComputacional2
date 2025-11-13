package com.proyect.pensamiento_comp.documents.repository;

import com.proyect.pensamiento_comp.documents.ActivityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityDocumentRepository extends MongoRepository<ActivityDocument, String> {
    Optional<ActivityDocument> findByRelationalId(Long relationalId);
    void deleteByRelationalId(Long relationalId);
    List<ActivityDocument> findByProfessorId(Long professorId);
}
