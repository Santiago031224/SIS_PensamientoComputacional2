package com.proyect.pensamiento_comp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PointCodeCreateDTO {
    private String code;
    private Integer points;
    private String status;
    private Integer usageLimit;
    private LocalDateTime redeemedAt;
    private Long activityId;
    private Long studentId;
}
