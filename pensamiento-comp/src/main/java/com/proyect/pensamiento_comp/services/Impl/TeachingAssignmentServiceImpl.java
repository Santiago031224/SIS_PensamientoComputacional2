package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.TeachingAssignmentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.TeachingAssignmentResponseDTO;
import com.proyect.pensamiento_comp.mapper.TeachingAssignmentMapper;
import com.proyect.pensamiento_comp.model.TeachingAssignment;
import com.proyect.pensamiento_comp.repository.ITeachingAssignmentRepository;
import com.proyect.pensamiento_comp.services.TeachingAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeachingAssignmentServiceImpl implements TeachingAssignmentService {

    private final ITeachingAssignmentRepository repository;
    private final TeachingAssignmentMapper mapper;

    @Override
    public List<TeachingAssignmentResponseDTO> findAll() {
        log.info("🔍 [SERVICE] TeachingAssignmentService.findAll() - Iniciando búsqueda");
        try {
            log.info("🔍 [SERVICE] Llamando a repository.findAllWithRelations()...");
            List<TeachingAssignment> assignments = repository.findAllWithRelations();
            log.info("📦 [SERVICE] Repository devolvió {} teaching assignments", assignments.size());
            
            List<TeachingAssignmentResponseDTO> dtos = new ArrayList<>();
            
            for (int i = 0; i < assignments.size(); i++) {
                TeachingAssignment assignment = assignments.get(i);
                try {
                    log.info("🔄 [SERVICE] Mapeando assignment {} de {} - ID: {}", i + 1, assignments.size(), assignment.getId());
                    log.info("   [SERVICE] - Course: {}", assignment.getCourse() != null ? assignment.getCourse().getId() : "NULL");
                    log.info("   [SERVICE] - Professor: {}", assignment.getProfessor() != null ? assignment.getProfessor().getId() : "NULL");
                    log.info("   [SERVICE] - Group: {}", assignment.getGroup() != null ? assignment.getGroup().getId() : "NULL");
                    
                    log.info("   [SERVICE] Llamando a mapper.toDto()...");
                    TeachingAssignmentResponseDTO dto = mapper.toDto(assignment);
                    log.info("   [SERVICE] mapper.toDto() completado para ID: {}", assignment.getId());
                    
                    dtos.add(dto);
                    log.info("✅ [SERVICE] Assignment {} mapeado correctamente", assignment.getId());
                } catch (Exception e) {
                    log.error("⚠️ [SERVICE] Error mapeando teaching assignment ID {}", assignment.getId());
                    log.error("⚠️ [SERVICE] Tipo de excepción: {}", e.getClass().getName());
                    log.error("⚠️ [SERVICE] Mensaje: {}", e.getMessage());
                    log.error("⚠️ [SERVICE] Stack trace:", e);
                    // Continue with next assignment
                }
            }
            
            log.info("✅ [SERVICE] Retornando {} DTOs mapeados exitosamente", dtos.size());
            return dtos;
        } catch (Exception e) {
            log.error("❌ [SERVICE] Error en findAll()");
            log.error("❌ [SERVICE] Tipo de excepción: {}", e.getClass().getName());
            log.error("❌ [SERVICE] Mensaje: {}", e.getMessage());
            log.error("❌ [SERVICE] Error en findAll(): {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching teaching assignments", e);
        }
    }

    @Override
    public TeachingAssignmentResponseDTO findById(Long id) {
        log.info("🔍 Fetching teaching assignment with id: {}", id);
        TeachingAssignment entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeachingAssignment not found"));
        return mapper.toDto(entity);
    }

    @Override
    public TeachingAssignmentResponseDTO create(TeachingAssignmentCreateDTO dto) {
        log.info("➕ Creating new teaching assignment");
        TeachingAssignment entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public TeachingAssignmentResponseDTO update(Long id, TeachingAssignmentCreateDTO dto) {
        log.info("✏️ Updating teaching assignment with id: {}", id);
        TeachingAssignment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeachingAssignment not found"));
        TeachingAssignment updated = mapper.toEntity(dto);
        updated.setId(existing.getId());
        return mapper.toDto(repository.save(updated));
    }

    @Override
    public void deleteById(Long id) {
        log.info("🗑️ Deleting teaching assignment with id: {}", id);
        repository.deleteById(id);
    }
}
