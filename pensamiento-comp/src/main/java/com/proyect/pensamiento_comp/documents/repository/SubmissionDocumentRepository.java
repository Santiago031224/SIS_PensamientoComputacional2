package com.proyect.pensamiento_comp.documents.repository;

import com.proyect.pensamiento_comp.documents.SubmissionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionDocumentRepository extends MongoRepository<SubmissionDocument, String> {
    Optional<SubmissionDocument> findBySqlId(Long sqlId);
    List<SubmissionDocument> findByStudentId(Long studentId);
    List<SubmissionDocument> findByActivityId(String activityId);
    void deleteBySqlId(Long sqlId);
}
