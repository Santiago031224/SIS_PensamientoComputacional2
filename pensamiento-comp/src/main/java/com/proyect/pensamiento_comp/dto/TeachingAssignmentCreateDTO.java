package com.proyect.pensamiento_comp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TeachingAssignmentCreateDTO {
    private LocalDate assignedDate;
    private String status;
    private Double evaluationWeight;
    private String roleInCourse;
    private String classroomLocation;
    private String modality;
    private String notes;
    private Long courseId;
    private Long professorId;
    private Long groupId;
}
