package com.proyect.pensamiento_comp.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DEPRECATED: This JPA entity is not used anymore.
 * PointCode data is now stored in MongoDB (point_codes collection).
 * Keeping this class for reference only.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @Entity  // COMMENTED OUT - PointCode is now in MongoDB only
// @Table(name = "POINT_CODE")
public class PointCode {

    @Id
    private String code;

    private Integer points;
    private LocalDateTime redeemedAt;
    private String status;
    private Integer usageLimit;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "STUDENT_id")
    private Student student;
}

