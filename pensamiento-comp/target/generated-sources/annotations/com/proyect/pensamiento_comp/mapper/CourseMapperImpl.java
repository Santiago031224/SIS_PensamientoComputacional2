package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.CourseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.CourseResponseDTO;
import com.proyect.pensamiento_comp.model.Administrator;
import com.proyect.pensamiento_comp.model.Course;
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
public class CourseMapperImpl implements CourseMapper {

    @Override
    public Course toEntity(CourseCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Course course = new Course();

        course.setName( dto.getName() );

        return course;
    }

    @Override
    public CourseResponseDTO toDto(Course entity) {
        if ( entity == null ) {
            return null;
        }

        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();

        courseResponseDTO.setAdministratorId( entityAdministratorId( entity ) );
        courseResponseDTO.setId( entity.getId() );
        courseResponseDTO.setName( entity.getName() );

        return courseResponseDTO;
    }

    @Override
    public List<CourseResponseDTO> toDtoList(List<Course> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CourseResponseDTO> list = new ArrayList<CourseResponseDTO>( entities.size() );
        for ( Course course : entities ) {
            list.add( toDto( course ) );
        }

        return list;
    }

    private Long entityAdministratorId(Course course) {
        if ( course == null ) {
            return null;
        }
        Administrator administrator = course.getAdministrator();
        if ( administrator == null ) {
            return null;
        }
        Long id = administrator.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
