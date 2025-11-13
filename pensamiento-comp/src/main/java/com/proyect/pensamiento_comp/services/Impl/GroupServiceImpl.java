package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.GroupCreateDTO;
import com.proyect.pensamiento_comp.dto.response.GroupResponseDTO;
import com.proyect.pensamiento_comp.mapper.GroupMapper;
import com.proyect.pensamiento_comp.model.Group;
import com.proyect.pensamiento_comp.model.Period;
import com.proyect.pensamiento_comp.repository.IGroupRepository;
import com.proyect.pensamiento_comp.repository.IPeriodRepository;
import com.proyect.pensamiento_comp.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final IGroupRepository groupRepository;
    private final IPeriodRepository periodRepository;
    private final GroupMapper groupMapper;
    private final Logger logger = Logger.getLogger(GroupServiceImpl.class.getName());

    @Override
    public List<GroupResponseDTO> findAll() {
        logger.info("Fetching all groups");
        return groupMapper.toDtoList(groupRepository.findAll());
    }

    @Override
    public GroupResponseDTO findById(Long id) {
        logger.info("Fetching group with ID: " + id);
        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return groupMapper.toDto(entity);
    }

    @Override
    public GroupResponseDTO create(GroupCreateDTO dto) {
        logger.info("Creating group");
        Group entity = groupMapper.toEntity(dto);
        
        // Asignar el Period si periodId está presente
        if (dto.getPeriodId() != null) {
            Period period = periodRepository.findById(dto.getPeriodId())
                    .orElseThrow(() -> new RuntimeException("Period not found with ID: " + dto.getPeriodId()));
            entity.setPeriod(period);
        }
        
        return groupMapper.toDto(groupRepository.save(entity));
    }

    @Override
    public GroupResponseDTO update(Long id, GroupCreateDTO dto) {
        logger.info("Updating group with ID: " + id);
        Group existing = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        Group updated = groupMapper.toEntity(dto);
        updated.setId(existing.getId());
        
        if (dto.getPeriodId() != null) {
            Period period = periodRepository.findById(dto.getPeriodId())
                    .orElseThrow(() -> new RuntimeException("Period not found with ID: " + dto.getPeriodId()));
            updated.setPeriod(period);
        }
        
        return groupMapper.toDto(groupRepository.save(updated));
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting group with ID: " + id);
        groupRepository.deleteById(id);
    }
}
