package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.StudentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.StudentResponseDTO;
import com.proyect.pensamiento_comp.mapper.StudentMapper;
import com.proyect.pensamiento_comp.model.Level;
import com.proyect.pensamiento_comp.model.Student;
import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.repository.IStudentRepository;
import com.proyect.pensamiento_comp.repository.IUserRepository;
import com.proyect.pensamiento_comp.repository.ILevelRepository;
import com.proyect.pensamiento_comp.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IStudentRepository repository;
    private final IUserRepository userRepository;
    private final ILevelRepository levelRepository;
    private final StudentMapper mapper;
    private final Logger logger = Logger.getLogger(StudentServiceImpl.class.getName());

    @Override
    public List<StudentResponseDTO> findAll() {
        logger.info("Fetching all students");
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public StudentResponseDTO findById(Long id) {
        logger.info("Fetching student with id: " + id);
        Student entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return mapper.toDto(entity);
    }

    @Override
    public StudentResponseDTO findByUserEmail(String email) {
        logger.info("Fetching student with user email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Student student = repository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found for this user"));
        return mapper.toDto(student);
    }

    @Override
    public StudentResponseDTO create(StudentCreateDTO dto) {
        logger.info("Creating new student");

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Level level = levelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new RuntimeException("Level not found"));


        Student entity = mapper.toEntity(dto);
        entity.setUser(user);
        entity.setLevel(level);

        Student saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public StudentResponseDTO update(Long id, StudentCreateDTO dto) {
        logger.info("Updating student with id: " + id);

        Student existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        existing.setCode(dto.getCode());
        existing.setGpa(dto.getGpa());

    // NOTE: Student no longer has a Period relationship; any incoming periodId should be ignored upstream.

        if (dto.getLevelId() != null) {
            Level level = levelRepository.findById(dto.getLevelId())
                    .orElseThrow(() -> new RuntimeException("Level not found"));
            existing.setLevel(level);
        }

        Student updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting student with id: " + id);
        repository.deleteById(id);
    }

    @Override
    public StudentResponseDTO findByUserId(Long userId) {
        logger.info("Fetching student by user id: " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Student student = repository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found for this user"));
        return mapper.toDto(student);
    }
}
