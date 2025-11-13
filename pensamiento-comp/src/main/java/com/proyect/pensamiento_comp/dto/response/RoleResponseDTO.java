package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;
import java.util.Set;

@Data
public class RoleResponseDTO {
    private Long id;
    private String name;
    private Set<PermissionResponseDTO> permissions;
}
