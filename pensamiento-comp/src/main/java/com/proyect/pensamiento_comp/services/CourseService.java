package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.CourseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.CourseResponseDTO;
import java.util.List;

public interface CourseService {
    List<CourseResponseDTO> findAll();
    CourseResponseDTO findById(Long id);
    CourseResponseDTO create(CourseCreateDTO dto);
    CourseResponseDTO update(Long id, CourseCreateDTO dto);
    void delete(Long id);
}
