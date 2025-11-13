package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.documents.SubmissionDocument;
import com.proyect.pensamiento_comp.documents.repository.SubmissionDocumentRepository;
import com.proyect.pensamiento_comp.dto.ScoreCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ScoreResponseDTO;
import com.proyect.pensamiento_comp.mapper.ScoreMapper;
import com.proyect.pensamiento_comp.services.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final SubmissionDocumentRepository submissionRepository;
    private final ScoreMapper mapper;
    private final Logger logger = Logger.getLogger(ScoreServiceImpl.class.getName());

    @Override
    public List<ScoreResponseDTO> findAll() {
        logger.info("Fetching all scores from MongoDB (embedded in submissions)");
        return submissionRepository.findAll().stream()
                .filter(sub -> sub.getGrading() != null && sub.getGrading().getBreakdown() != null)
                .flatMap(sub -> sub.getGrading().getBreakdown().stream())
                .map(mapper::gradeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScoreResponseDTO findById(Long id) {
        logger.info("Score findById not supported - scores are embedded in submissions");
        throw new UnsupportedOperationException("Scores are embedded in submissions. Use submission endpoints.");
    }

    @Override
    public ScoreResponseDTO create(ScoreCreateDTO dto) {
        logger.info("Creating score by updating submission grading in MongoDB");
        SubmissionDocument submission = submissionRepository.findBySqlId(dto.getSubmissionId())
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        
        if (submission.getGrading() == null) {
            submission.setGrading(new SubmissionDocument.Grading());
        }
        if (submission.getGrading().getBreakdown() == null) {
            submission.getGrading().setBreakdown(new ArrayList<>());
        }
        
        SubmissionDocument.ExerciseGrade grade = mapper.dtoToGrade(dto);
        submission.getGrading().getBreakdown().add(grade);
        submission.getGrading().setTotalPoints(
            submission.getGrading().getBreakdown().stream()
                .mapToInt(SubmissionDocument.ExerciseGrade::getPointsAwarded)
                .sum()
        );
        
        submissionRepository.save(submission);
        return mapper.gradeToDto(grade);
    }

    @Override
    public ScoreResponseDTO update(Long id, ScoreCreateDTO dto) {
        logger.info("Score update not directly supported - update submission grading instead");
        throw new UnsupportedOperationException("Scores are embedded. Update via submission endpoints.");
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Score delete not directly supported");
        throw new UnsupportedOperationException("Scores are embedded. Delete via submission endpoints.");
    }
}
