package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.documents.SubmissionDocument;
import com.proyect.pensamiento_comp.documents.repository.SubmissionDocumentRepository;
import com.proyect.pensamiento_comp.dto.SubmissionCreateDTO;
import com.proyect.pensamiento_comp.dto.response.SubmissionResponseDTO;
import com.proyect.pensamiento_comp.mapper.SubmissionMapper;
import com.proyect.pensamiento_comp.services.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionDocumentRepository repository;
    private final SubmissionMapper mapper;
    private final Logger logger = Logger.getLogger(SubmissionServiceImpl.class.getName());

    @Override
    public List<SubmissionResponseDTO> findAll() {
        logger.info("Fetching all submissions from MongoDB");
        return repository.findAll().stream()
                .map(mapper::documentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubmissionResponseDTO findById(Long id) {
        logger.info("Fetching submission with id: " + id + " from MongoDB");
        SubmissionDocument entity = repository.findBySqlId(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        return mapper.documentToDto(entity);
    }

    @Override
    public SubmissionResponseDTO create(SubmissionCreateDTO dto) {
        logger.info("Creating new submission in MongoDB");
        SubmissionDocument entity = mapper.dtoToDocument(dto);
        return mapper.documentToDto(repository.save(entity));
    }

    @Override
    public SubmissionResponseDTO update(Long id, SubmissionCreateDTO dto) {
        logger.info("Updating submission with id: " + id + " in MongoDB");
        SubmissionDocument existing = repository.findBySqlId(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        SubmissionDocument updated = mapper.dtoToDocument(dto);
        updated.setId(existing.getId());
        updated.setSqlId(existing.getSqlId());
        return mapper.documentToDto(repository.save(updated));
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting submission with id: " + id + " from MongoDB");
        repository.deleteBySqlId(id);
    }
}
