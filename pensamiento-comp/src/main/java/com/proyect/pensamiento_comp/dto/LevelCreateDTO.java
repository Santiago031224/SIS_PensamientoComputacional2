package com.proyect.pensamiento_comp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelCreateDTO {
    @NotBlank(message = "Level name is required")
    @Size(max = 20, message = "Level name must not exceed 20 characters")
    private String name;
    
    private LocalDateTime createdAt;
}
