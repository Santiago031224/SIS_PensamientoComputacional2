package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.documents.PointCodeDocument;
import com.proyect.pensamiento_comp.documents.repository.PointCodeDocumentRepository;
import com.proyect.pensamiento_comp.dto.PointCodeCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PointCodeResponseDTO;
import com.proyect.pensamiento_comp.mapper.PointCodeMapper;
import com.proyect.pensamiento_comp.services.PointCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointCodeServiceImpl implements PointCodeService {

    private final PointCodeDocumentRepository repository;
    private final PointCodeMapper mapper;
    private final Logger logger = Logger.getLogger(PointCodeServiceImpl.class.getName());

    @Override
    public List<PointCodeResponseDTO> findAll() {
        logger.info("Fetching all point codes from MongoDB");
        return repository.findAll().stream()
                .map(mapper::documentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PointCodeResponseDTO findById(String code) {
        logger.info("Fetching point code with code: " + code);
        PointCodeDocument document = repository.findById(code)
                .orElseThrow(() -> new RuntimeException("PointCode not found"));
        return mapper.documentToDto(document);
    }

    @Override
    public PointCodeResponseDTO create(PointCodeCreateDTO dto) {
        logger.info("Creating new point code in MongoDB");
        
        PointCodeDocument document = mapper.dtoToDocument(dto);
        
        if (document.getCreatedAt() == null) {
            document.setCreatedAt(LocalDateTime.now());
        }
        if (document.getStatus() == null) {
            document.setStatus("ACTIVE");
        }
        if (document.getUsedCount() == null) {
            document.setUsedCount(0);
        }

        PointCodeDocument saved = repository.save(document);
        return mapper.documentToDto(saved);
    }

    @Override
    public PointCodeResponseDTO update(String code, PointCodeCreateDTO dto) {
        logger.info("Updating point code with code: " + code);

        PointCodeDocument existing = repository.findById(code)
                .orElseThrow(() -> new RuntimeException("PointCode not found"));

        if (dto.getPoints() != null) existing.setPoints(dto.getPoints());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        if (dto.getUsageLimit() != null) existing.setUsageLimit(dto.getUsageLimit());
        if (dto.getActivityId() != null) existing.setActivityId(dto.getActivityId().toString());

        PointCodeDocument updated = repository.save(existing);
        return mapper.documentToDto(updated);
    }

    @Override
    public void delete(String code) {
        logger.info("Deleting point code with code: " + code);
        if (!repository.existsById(code)) {
            throw new RuntimeException("PointCode not found");
        }
        repository.deleteById(code);
    }
}
