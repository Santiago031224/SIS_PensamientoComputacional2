package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.PeriodCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PeriodResponseDTO;
import com.proyect.pensamiento_comp.model.Period;
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
public class PeriodMapperImpl implements PeriodMapper {

    @Override
    public Period toEntity(PeriodCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Period period = new Period();

        period.setCode( dto.getCode() );
        period.setStartDate( dto.getStartDate() );
        period.setEndDate( dto.getEndDate() );

        return period;
    }

    @Override
    public PeriodResponseDTO toDto(Period entity) {
        if ( entity == null ) {
            return null;
        }

        PeriodResponseDTO periodResponseDTO = new PeriodResponseDTO();

        periodResponseDTO.setId( entity.getId() );
        periodResponseDTO.setCode( entity.getCode() );
        periodResponseDTO.setStartDate( entity.getStartDate() );
        periodResponseDTO.setEndDate( entity.getEndDate() );

        return periodResponseDTO;
    }

    @Override
    public List<PeriodResponseDTO> toDtoList(List<Period> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PeriodResponseDTO> list = new ArrayList<PeriodResponseDTO>( entities.size() );
        for ( Period period : entities ) {
            list.add( toDto( period ) );
        }

        return list;
    }
}
