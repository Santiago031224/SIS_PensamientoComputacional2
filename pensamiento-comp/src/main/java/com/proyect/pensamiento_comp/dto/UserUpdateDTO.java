// UserUpdateDTO.java
package com.proyect.pensamiento_comp.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;    
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private String name;
    private String lastName;
    private String email;   
}