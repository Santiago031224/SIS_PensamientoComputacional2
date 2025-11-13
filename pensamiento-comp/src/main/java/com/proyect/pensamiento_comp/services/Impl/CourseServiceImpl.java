package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.CourseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.CourseResponseDTO;
import com.proyect.pensamiento_comp.mapper.CourseMapper;
import com.proyect.pensamiento_comp.model.Administrator;
import com.proyect.pensamiento_comp.model.Course;
import com.proyect.pensamiento_comp.repository.IAdministratorRepository;
import com.proyect.pensamiento_comp.repository.ICourseRepository;
import com.proyect.pensamiento_comp.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final ICourseRepository courseRepository;
    private final IAdministratorRepository administratorRepository;
    private final CourseMapper courseMapper;
    private final Logger logger = Logger.getLogger(CourseServiceImpl.class.getName());

    @Override
    public List<CourseResponseDTO> findAll() {
        logger.info("Fetching all courses");
        return courseMapper.toDtoList(courseRepository.findAll());
    }

    @Override
    public CourseResponseDTO findById(Long id) {
        logger.info("Fetching course with ID: " + id);
        Course entity = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return courseMapper.toDto(entity);
    }

    @Override
    public CourseResponseDTO create(CourseCreateDTO dto) {
        logger.info("Creating course");
        Course entity = courseMapper.toEntity(dto);
        
        if (dto.getAdministratorId() != null) {
            Administrator administrator = administratorRepository.findById(dto.getAdministratorId())
                    .orElseThrow(() -> new RuntimeException("Administrator not found with ID: " + dto.getAdministratorId()));
            entity.setAdministrator(administrator);
        }
        
        return courseMapper.toDto(courseRepository.save(entity));
    }

    @Override
    public CourseResponseDTO update(Long id, CourseCreateDTO dto) {
        logger.info("Updating course with ID: " + id);
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Course updated = courseMapper.toEntity(dto);
        updated.setId(existing.getId());
        
        if (dto.getAdministratorId() != null) {
            Administrator administrator = administratorRepository.findById(dto.getAdministratorId())
                    .orElseThrow(() -> new RuntimeException("Administrator not found with ID: " + dto.getAdministratorId()));
            updated.setAdministrator(administrator);
        }
        
        return courseMapper.toDto(courseRepository.save(updated));
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting course with ID: " + id);
        courseRepository.deleteById(id);
    }
}
