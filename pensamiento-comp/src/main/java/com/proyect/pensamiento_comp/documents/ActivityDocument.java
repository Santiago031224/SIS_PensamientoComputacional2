package com.proyect.pensamiento_comp.documents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "activities")
public class ActivityDocument {

    @Id
    @Field("_id")
    private Long relationalId;
    
    private String title;
    private String description;
    
    @Field("professor_id")
    private Long professorId;
    
    @Field("professor_name")
    private String professorName;
    
    @Field("schedule")
    private Schedule schedule;

    private List<ExerciseEmbed> exercises = new ArrayList<>();
    private List<ActivityFileEmbed> files = new ArrayList<>();
    private List<PointCodeEmbed> pointCodes = new ArrayList<>();

    public ActivityDocument() {}

    public ActivityDocument(Long relationalId, String title, String description,
                            Schedule schedule, Long professorId) {
        this.relationalId = relationalId;
        this.title = title;
        this.description = description;
        this.schedule = schedule;
        this.professorId = professorId;
    }
    
    // Getters de conveniencia para mantener compatibilidad
    public LocalDate getStartDate() {
        return schedule != null && schedule.getStartDate() != null 
            ? schedule.getStartDate().toLocalDate() 
            : null;
    }
    
    public LocalDate getEndDate() {
        return schedule != null && schedule.getEndDate() != null 
            ? schedule.getEndDate().toLocalDate() 
            : null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Schedule {
        @Field("start_date")
        private LocalDateTime startDate;
        
        @Field("end_date")
        private LocalDateTime endDate;
        
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseEmbed {
        @Field("exercise_id")
        private Long exerciseId;
        
        private String title;
        private String description;
        private String difficulty;
        private Integer position;
        
        @Field("point_value")
        private Integer pointValue; // Puntos que vale el ejercicio en la actividad
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityFileEmbed {
        private Long fileId;
        private String fileName;
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointCodeEmbed {
        private Long codeId;
        private String codeValue;
        private Integer points;
    }
}
