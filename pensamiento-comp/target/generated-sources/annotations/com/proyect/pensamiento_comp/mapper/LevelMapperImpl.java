package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.LevelCreateDTO;
import com.proyect.pensamiento_comp.dto.response.LevelResponseDTO;
import com.proyect.pensamiento_comp.model.Level;
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
public class LevelMapperImpl implements LevelMapper {

    @Override
    public Level toEntity(LevelCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Level level = new Level();

        level.setName( dto.getName() );
        level.setCreatedAt( dto.getCreatedAt() );

        return level;
    }

    @Override
    public LevelResponseDTO toDto(Level entity) {
        if ( entity == null ) {
            return null;
        }

        LevelResponseDTO levelResponseDTO = new LevelResponseDTO();

        levelResponseDTO.setId( entity.getId() );
        levelResponseDTO.setName( entity.getName() );
        levelResponseDTO.setCreatedAt( entity.getCreatedAt() );

        return levelResponseDTO;
    }

    @Override
    public List<LevelResponseDTO> toDtoList(List<Level> entities) {
        if ( entities == null ) {
            return null;
        }

        List<LevelResponseDTO> list = new ArrayList<LevelResponseDTO>( entities.size() );
        for ( Level level : entities ) {
            list.add( toDto( level ) );
        }

        return list;
    }
}
