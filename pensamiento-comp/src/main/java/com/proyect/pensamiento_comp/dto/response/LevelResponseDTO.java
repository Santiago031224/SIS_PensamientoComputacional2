package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LevelResponseDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
