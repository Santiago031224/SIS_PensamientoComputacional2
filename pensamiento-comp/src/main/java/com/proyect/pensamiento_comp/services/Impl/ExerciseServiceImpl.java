package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.documents.ExerciseDocument;
import com.proyect.pensamiento_comp.documents.repository.ExerciseDocumentRepository;
import com.proyect.pensamiento_comp.dto.ExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ExerciseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.proyect.pensamiento_comp.services.ExerciseService;
import java.util.Objects;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseDocumentRepository exerciseDocumentRepository;
    private final Logger logger = Logger.getLogger(ExerciseServiceImpl.class.getName());

    @Override
    public List<ExerciseResponseDTO> findAll() {
        logger.info("Fetching all exercises from MongoDB");
        return exerciseDocumentRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseResponseDTO findById(Long id) {
        logger.info("Fetching exercise with relational ID: " + id);
        ExerciseDocument document = exerciseDocumentRepository.findByRelationalId(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found with relational ID: " + id));
        return toResponseDTO(document);
    }

    @Override
    public ExerciseResponseDTO create(ExerciseCreateDTO dto) {
        logger.info("Creating exercise in MongoDB");

        Long newRelationalId = generateNewRelationalId();

        ExerciseDocument document = new ExerciseDocument(
                newRelationalId,
                dto.getTitle(),
                dto.getDescription(),
                dto.getDifficulty(),
                dto.getProgramming_language()
        );

        ExerciseDocument saved = exerciseDocumentRepository.save(document);
        logger.info("Exercise created with ID: " + saved.getId());

        return toResponseDTO(saved);
    }

    @Override
    public ExerciseResponseDTO update(Long id, ExerciseCreateDTO dto) {
        logger.info("Updating exercise with relational ID: " + id);

        ExerciseDocument existing = exerciseDocumentRepository.findByRelationalId(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found with relational ID: " + id));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setDifficulty(dto.getDifficulty());
        existing.setProgramming_language(dto.getProgramming_language());

        ExerciseDocument updated = exerciseDocumentRepository.save(existing);
        logger.info("Exercise updated: " + updated.getId());

        return toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting exercise with relational ID: " + id);
        exerciseDocumentRepository.deleteByRelationalId(id);
        logger.info("Exercise deleted with relational ID: " + id);
    }

    private ExerciseResponseDTO toResponseDTO(ExerciseDocument document) {
    ExerciseResponseDTO dto = new ExerciseResponseDTO();
    dto.setRelationalId(document.getRelationalId()); // asegúrate que sea Long válido
    dto.setTitle(document.getTitle());
    dto.setDescription(document.getDescription());
    dto.setDifficulty(document.getDifficulty());
    dto.setProgramming_language(document.getProgramming_language());
    dto.setMongoId(document.getId());
    return dto;
}



    private Long generateNewRelationalId() {
    return exerciseDocumentRepository.findAll().stream()
            .map(ExerciseDocument::getRelationalId)
            .filter(Objects::nonNull)
            .max(Long::compareTo)
            .orElse(0L) + 1;
}

}
