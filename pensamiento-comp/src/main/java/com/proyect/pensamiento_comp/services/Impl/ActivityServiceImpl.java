package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.documents.ActivityDocument;
import com.proyect.pensamiento_comp.documents.repository.ActivityDocumentRepository;
import com.proyect.pensamiento_comp.dto.ActivityCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityResponseDTO;
import com.proyect.pensamiento_comp.mapper.ActivityMapper;
import com.proyect.pensamiento_comp.services.ActivityService;
import com.proyect.pensamiento_comp.services.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityDocumentRepository activityDocumentRepository;
    private final ActivityMapper activityMapper;
    private final SequenceGeneratorService sequenceGenerator;
    private final Logger logger = Logger.getLogger(ActivityServiceImpl.class.getName());

    @Override
    public List<ActivityResponseDTO> findAll() {
        logger.info("Fetching all activities from MongoDB");
        return activityDocumentRepository.findAll().stream()
                .map(activityMapper::documentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityResponseDTO findById(Long id) {
        logger.info("Fetching activity with ID: " + id + " from MongoDB");
        ActivityDocument activity = activityDocumentRepository.findByRelationalId(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        return activityMapper.documentToDto(activity);
    }

    @Override
    public ActivityResponseDTO create(ActivityCreateDTO dto) {
        logger.info("Creating activity in MongoDB");
        ActivityDocument activity = activityMapper.dtoToDocument(dto);
        activity.setRelationalId(sequenceGenerator.generateSequence("activity_sequence"));
        ActivityDocument saved = activityDocumentRepository.save(activity);
        return activityMapper.documentToDto(saved);
    }

    @Override
    public ActivityResponseDTO update(Long id, ActivityCreateDTO dto) {
        logger.info("Updating activity with ID: " + id + " in MongoDB");
        ActivityDocument existing = activityDocumentRepository.findByRelationalId(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        ActivityDocument updated = activityMapper.dtoToDocument(dto);
        updated.setRelationalId(existing.getRelationalId());
        ActivityDocument saved = activityDocumentRepository.save(updated);
        return activityMapper.documentToDto(saved);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting activity with ID: " + id + " from MongoDB");
        activityDocumentRepository.deleteByRelationalId(id);
    }
}
