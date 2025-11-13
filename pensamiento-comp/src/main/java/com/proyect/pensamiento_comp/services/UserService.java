package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.ChangePasswordDTO;
import com.proyect.pensamiento_comp.dto.UserCreateDTO;
import com.proyect.pensamiento_comp.dto.response.UserResponseDTO;
import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.dto.UserUpdateDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> findAll();

    UserResponseDTO findById(Long id);

    UserResponseDTO create(UserCreateDTO dto);

    UserResponseDTO update(Long id, UserUpdateDTO dto);

    void deleteById(Long id);

    UserResponseDTO findByEmail(String email);

    UserResponseDTO addRoleToUser(Long userId, Long roleId);

    UserResponseDTO removeRoleFromUser(Long userId, Long roleId);

    void toggleUserStatus(Long id);
    void changePassword(Long id, ChangePasswordDTO dto);


    List<User> findAllEntities();
    User createEntity(UserCreateDTO dto);
    User updateEntity(Long id, UserCreateDTO dto);
    void deleteEntity(Long id);
    void toggleUserStatusEntity(Long id);
    User addRoleToUserEntity(Long userId, Long roleId);
    User removeRoleFromUserEntity(Long userId, Long roleId);


}
