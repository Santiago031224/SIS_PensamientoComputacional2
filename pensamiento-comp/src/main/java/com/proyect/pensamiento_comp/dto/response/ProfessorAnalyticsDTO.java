package com.proyect.pensamiento_comp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorAnalyticsDTO {
    
    // Teaching assignments (from Oracle)
    private List<TeachingAssignmentResponseDTO> teachingAssignments;
    
    // Activities (from MongoDB)
    private List<ActivitySummaryDTO> activities;
    
    // Groups managed
    private List<GroupSummaryDTO> groups;
    
    // Statistics
    private ProfessorStatistics statistics;
    
    /**
     * Creates an empty analytics DTO for non-professor users
     */
    public static ProfessorAnalyticsDTO empty() {
        ProfessorAnalyticsDTO dto = new ProfessorAnalyticsDTO();
        dto.setTeachingAssignments(new ArrayList<>());
        dto.setActivities(new ArrayList<>());
        dto.setGroups(new ArrayList<>());
        dto.setStatistics(new ProfessorStatistics(0, 0, 0, 0, 0, 0));
        return dto;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivitySummaryDTO {
        private String id; // MongoDB ID
        private Long relationalId;
        private String title;
        private String description;
        private String startDate;
        private String endDate;
        private Integer exerciseCount;
        private Integer submissionCount;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupSummaryDTO {
        private Long id;
        private String name;
        private String periodCode;
        private Integer studentCount;
        private String courseName;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfessorStatistics {
        private Integer totalTeachingAssignments;
        private Integer totalActivities;
        private Integer totalStudents;
        private Integer totalGroups;
        private Integer activeActivities;
        private Integer completedActivities;
    }
}
