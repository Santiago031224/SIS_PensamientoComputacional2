package com.proyect.pensamiento_comp.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "submissions")
public class SubmissionDocument {
    
    @Id
    private String id;
    
    private Long sqlId;
    private String activityId;
    private Long studentId;
    private String studentName;
    private String studentCode;
    private Long groupId;


    @Field("total_points") 
    private Integer totalPoints;
    private SubmissionData submissionData;
    private List<ExerciseAnswer> exerciseAnswers;
    private Grading grading;
    private String pointCodeUsed;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubmissionData {
        private String type;
        private LocalDateTime submittedAt;
        private String status;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExerciseAnswer {
        private String exerciseId;
        private Long sqlExerciseId;
        private List<String> files;
        private List<String> links;
        private String codeSnippet;
        private LocalDateTime submittedAt;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Grading {
        private String status;
        private Long gradedBy;
        private LocalDateTime gradedAt;
        private Integer totalPoints;
        private List<ExerciseGrade> breakdown;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExerciseGrade {
        private String exerciseId;
        private Integer pointsAwarded;
        private String comments;
    }
}
