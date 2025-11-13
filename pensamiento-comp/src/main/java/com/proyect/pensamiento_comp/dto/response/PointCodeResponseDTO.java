package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PointCodeResponseDTO {
    private String code;
    private Integer points;
    private LocalDateTime redeemedAt;
    private String status;
    private Integer usageLimit;
    private Long activityId;
    private Long studentId;
}
