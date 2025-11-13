package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.ProfessorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ProfessorResponseDTO;
import com.proyect.pensamiento_comp.mapper.ProfessorMapper;
import com.proyect.pensamiento_comp.model.Professor;
import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.repository.IProfessorRepository;
import com.proyect.pensamiento_comp.repository.IUserRepository;
import com.proyect.pensamiento_comp.services.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final IProfessorRepository repository;
    private final IUserRepository userRepository;
    private final ProfessorMapper mapper;
    private final Logger logger = Logger.getLogger(ProfessorServiceImpl.class.getName());

    @Override
    public List<ProfessorResponseDTO> findAll() {
        logger.info("Fetching all professors");
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public ProfessorResponseDTO findById(Long id) {
        logger.info("Fetching professor with id: " + id);
        Professor entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor not found"));
        return mapper.toDto(entity);
    }

    @Override
    public ProfessorResponseDTO create(ProfessorCreateDTO dto) {
        logger.info("Creating professor");

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Professor entity = mapper.toEntity(dto);
        entity.setUser(user);

        Professor saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public ProfessorResponseDTO update(Long id, ProfessorCreateDTO dto) {
        logger.info("Updating professor with id: " + id);

        Professor existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        existing.setOfficeLocation(dto.getOfficeLocation());
        existing.setSpecialtyArea(dto.getSpecialtyArea());
        existing.setMaxGroupsAllowed(dto.getMaxGroupsAllowed());
        existing.setAcademicRank(dto.getAcademicRank());

        Professor updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting professor with id: " + id);
        repository.deleteById(id);
    }
}
