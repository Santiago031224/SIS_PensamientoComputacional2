package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.PeriodCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PeriodResponseDTO;
import com.proyect.pensamiento_comp.mapper.PeriodMapper;
import com.proyect.pensamiento_comp.model.Period;
import com.proyect.pensamiento_comp.repository.IPeriodRepository;
import com.proyect.pensamiento_comp.services.PeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PeriodServiceImpl implements PeriodService {

    private final IPeriodRepository periodRepository;
    private final PeriodMapper periodMapper;
    private final Logger logger = Logger.getLogger(PeriodServiceImpl.class.getName());

    @Override
    public List<PeriodResponseDTO> findAll() {
        logger.info("Fetching all periods");
        return periodMapper.toDtoList(periodRepository.findAll());
    }

    @Override
    public PeriodResponseDTO findById(Long id) {
        logger.info("Fetching period with ID: " + id);
        Period entity = periodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Period not found"));
        return periodMapper.toDto(entity);
    }

    @Override
    public PeriodResponseDTO create(PeriodCreateDTO dto) {
        logger.info("Creating period");
        Period entity = periodMapper.toEntity(dto);
        return periodMapper.toDto(periodRepository.save(entity));
    }

    @Override
    public PeriodResponseDTO update(Long id, PeriodCreateDTO dto) {
        logger.info("Updating period with ID: " + id);
        Period existing = periodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Period not found"));
        Period updated = periodMapper.toEntity(dto);
        updated.setId(existing.getId());
        return periodMapper.toDto(periodRepository.save(updated));
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting period with ID: " + id);
        periodRepository.deleteById(id);
    }
}
