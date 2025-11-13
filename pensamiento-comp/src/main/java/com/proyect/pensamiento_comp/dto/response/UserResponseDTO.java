package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String profilePicture;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    private Set<RoleResponseDTO> roles;
}
    