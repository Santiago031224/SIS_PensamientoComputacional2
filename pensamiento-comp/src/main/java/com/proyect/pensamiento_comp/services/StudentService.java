package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.StudentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.StudentResponseDTO;
import java.util.List;

public interface StudentService {

    List<StudentResponseDTO> findAll();

    StudentResponseDTO findById(Long id);

    StudentResponseDTO findByUserEmail(String email);

    StudentResponseDTO findByUserId(Long userId);

    StudentResponseDTO create(StudentCreateDTO dto);

    StudentResponseDTO update(Long id, StudentCreateDTO dto);

    void deleteById(Long id);
}
