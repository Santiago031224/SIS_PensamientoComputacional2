package com.proyect.pensamiento_comp.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "point_codes")
public class PointCodeDocument {
    
    @Id
    private String id;
    
    private String activityId;
    private Long professorId;
    private String professorName;
    private Integer points;
    private Integer usageLimit;
    private Integer usedCount;
    private List<Redemption> redemptions;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Redemption {
        private Long studentId;
        private String studentName;
        private LocalDateTime redeemedAt;
        private String submissionId;
    }
}
