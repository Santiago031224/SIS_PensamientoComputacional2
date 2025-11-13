package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ActivityExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityExerciseResponseDTO;
import com.proyect.pensamiento_comp.model.ActivityExercise;
import com.proyect.pensamiento_comp.model.ActivityExerciseId;
import com.proyect.pensamiento_comp.model.Activity;
import com.proyect.pensamiento_comp.model.Exercise;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivityExerciseMapperTest {
    private final ActivityExerciseMapper mapper = Mappers.getMapper(ActivityExerciseMapper.class);

    @Test
    void testToEntity_fullData() {

        ActivityExerciseCreateDTO dto = new ActivityExerciseCreateDTO();
        dto.setActivityId(1L);
        dto.setExerciseId(2L);
        dto.setPosition(3);


        ActivityExercise entity = mapper.toEntity(dto);


        assertNotNull(entity);

        assertNull(entity.getId());
        assertEquals(3, entity.getPosition());
        assertNull(entity.getActivity());
        assertNull(entity.getExercise());
    }

    @Test
    void testToDto_fullData() {

        ActivityExerciseId id = new ActivityExerciseId(1L, 2L);
        Activity activity = new Activity();
        activity.setId(1L);
        Exercise exercise = new Exercise();
        exercise.setId(2L);
        ActivityExercise entity = new ActivityExercise();
        entity.setId(id);
        entity.setActivity(activity);
        entity.setExercise(exercise);
        entity.setPosition(5);


        ActivityExerciseResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(1L, dto.getActivityId());
        assertEquals(2L, dto.getExerciseId());
        assertEquals(5, dto.getPosition());
    }

    @Test
    void testToDtoList_multipleElements() {

        ActivityExerciseId id1 = new ActivityExerciseId(1L, 2L);
        ActivityExercise entity1 = new ActivityExercise();
        entity1.setId(id1);
        entity1.setPosition(10);

        Activity activity1 = new Activity();
        activity1.setId(1L);
        Exercise exercise1 = new Exercise();
        exercise1.setId(2L);
        entity1.setActivity(activity1);
        entity1.setExercise(exercise1);

        ActivityExerciseId id2 = new ActivityExerciseId(3L, 4L);
        ActivityExercise entity2 = new ActivityExercise();
        entity2.setId(id2);
        entity2.setPosition(20);
        Activity activity2 = new Activity();
        activity2.setId(3L);
        Exercise exercise2 = new Exercise();
        exercise2.setId(4L);
        entity2.setActivity(activity2);
        entity2.setExercise(exercise2);

        List<ActivityExercise> entities = Arrays.asList(entity1, entity2);


        List<ActivityExerciseResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getActivityId());
        assertEquals(2L, dtos.get(0).getExerciseId());
        assertEquals(10, dtos.get(0).getPosition());
        assertEquals(3L, dtos.get(1).getActivityId());
        assertEquals(4L, dtos.get(1).getExerciseId());
        assertEquals(20, dtos.get(1).getPosition());
    }

    @Test
    void testToEntity_minimalIds() {

        ActivityExerciseCreateDTO dto = new ActivityExerciseCreateDTO();
        dto.setActivityId(99L);
        dto.setExerciseId(88L);


        ActivityExercise entity = mapper.toEntity(dto);


        assertNotNull(entity);

        assertNull(entity.getId());
    }
}
