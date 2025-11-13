package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.LevelCreateDTO;
import com.proyect.pensamiento_comp.dto.response.LevelResponseDTO;
import com.proyect.pensamiento_comp.mapper.LevelMapper;
import com.proyect.pensamiento_comp.model.Level;
import com.proyect.pensamiento_comp.repository.ILevelRepository;
import com.proyect.pensamiento_comp.services.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final ILevelRepository levelRepository;
    private final LevelMapper levelMapper;
    private final Logger logger = Logger.getLogger(LevelServiceImpl.class.getName());

    @Override
    public List<LevelResponseDTO> findAll() {
        logger.info("Fetching all levels");
        return levelMapper.toDtoList(levelRepository.findAll());
    }

    @Override
    public LevelResponseDTO findById(Long id) {
        logger.info("Fetching level with ID: " + id);
        Level entity = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found"));
        return levelMapper.toDto(entity);
    }

    @Override
    public LevelResponseDTO create(LevelCreateDTO dto) {
        logger.info("Creating level");
        Level entity = levelMapper.toEntity(dto);
        return levelMapper.toDto(levelRepository.save(entity));
    }

    @Override
    public LevelResponseDTO update(Long id, LevelCreateDTO dto) {
        logger.info("Updating level with ID: " + id);
        Level existing = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found"));
        Level updated = levelMapper.toEntity(dto);
        updated.setId(existing.getId());
        return levelMapper.toDto(levelRepository.save(updated));
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting level with ID: " + id);
        levelRepository.deleteById(id);
    }
}
