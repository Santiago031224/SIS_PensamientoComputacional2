package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.StudentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.StudentResponseDTO;
import com.proyect.pensamiento_comp.model.Level;
import com.proyect.pensamiento_comp.model.Student;
import com.proyect.pensamiento_comp.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T21:33:45-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student toEntity(StudentCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Student student = new Student();

        student.setCode( dto.getCode() );
        student.setGpa( dto.getGpa() );

        return student;
    }

    @Override
    public StudentResponseDTO toDto(Student entity) {
        if ( entity == null ) {
            return null;
        }

        StudentResponseDTO studentResponseDTO = new StudentResponseDTO();

        studentResponseDTO.setUserId( entityUserId( entity ) );
        studentResponseDTO.setLevelId( entityLevelId( entity ) );
        studentResponseDTO.setId( entity.getId() );
        studentResponseDTO.setCode( entity.getCode() );
        studentResponseDTO.setGpa( entity.getGpa() );

        return studentResponseDTO;
    }

    @Override
    public List<StudentResponseDTO> toDtoList(List<Student> entities) {
        if ( entities == null ) {
            return null;
        }

        List<StudentResponseDTO> list = new ArrayList<StudentResponseDTO>( entities.size() );
        for ( Student student : entities ) {
            list.add( toDto( student ) );
        }

        return list;
    }

    private Long entityUserId(Student student) {
        if ( student == null ) {
            return null;
        }
        User user = student.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityLevelId(Student student) {
        if ( student == null ) {
            return null;
        }
        Level level = student.getLevel();
        if ( level == null ) {
            return null;
        }
        Long id = level.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
