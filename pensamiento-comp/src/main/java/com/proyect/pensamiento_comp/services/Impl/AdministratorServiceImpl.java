package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.AdministratorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.AdministratorResponseDTO;
import com.proyect.pensamiento_comp.mapper.AdministratorMapper;
import com.proyect.pensamiento_comp.model.Administrator;
import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.repository.IAdministratorRepository;
import com.proyect.pensamiento_comp.repository.IUserRepository;
import com.proyect.pensamiento_comp.services.AdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AdministratorServiceImpl implements AdministratorService {

    private final IAdministratorRepository repository;
    private final IUserRepository userRepository;
    private final AdministratorMapper mapper;
    private final Logger logger = Logger.getLogger(AdministratorServiceImpl.class.getName());

    @Override
    public List<AdministratorResponseDTO> findAll() {
        logger.info("Fetching all administrators");
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public AdministratorResponseDTO findById(Long id) {
        logger.info("Fetching administrator with id: " + id);
        Administrator entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found"));
        return mapper.toDto(entity);
    }

    @Override
    public AdministratorResponseDTO create(AdministratorCreateDTO dto) {
        logger.info("Creating new administrator");

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Administrator entity = mapper.toEntity(dto);
        entity.setUser(user);

        Administrator saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public AdministratorResponseDTO update(Long id, AdministratorCreateDTO dto) {
        logger.info("Updating administrator with id: " + id);

        Administrator existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found"));

        existing.setDepartment(dto.getDepartment());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existing.setUser(user);
        }

        Administrator updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting administrator with id: " + id);
        repository.deleteById(id);
    }
}
