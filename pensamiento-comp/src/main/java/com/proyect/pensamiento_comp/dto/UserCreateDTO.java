package com.proyect.pensamiento_comp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String documentType;
    private String document;
    private Set<Long> roleIds;
}
