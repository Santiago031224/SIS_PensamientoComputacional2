package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.response.UserResponseDTO;
import com.proyect.pensamiento_comp.mapper.UserMapper;
import com.proyect.pensamiento_comp.model.Role;
import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.repository.IRoleRepository;
import com.proyect.pensamiento_comp.repository.IUserRepository;
import com.proyect.pensamiento_comp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.proyect.pensamiento_comp.dto.*;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());


    @Override
    public List<UserResponseDTO> findAll() {
        logger.info("Fetching all users");
        return mapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserResponseDTO findById(Long id) {
        logger.info("Fetching user with ID: " + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDTO create(UserCreateDTO dto) {
        logger.info("Creating new user");

        dto.setPassword(dto.getPassword());
        User user = mapper.toEntity(dto);
        user.setStatus(1);

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            for (Long roleId : dto.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
                user.getRoles().add(role);
            }
        }

        User saved = userRepository.save(user);
        logger.info("User created successfully with ID: " + saved.getId());
        return mapper.toDto(saved);
    }



    @Override
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        logger.info("Updating user with ID: " + id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (dto.getName() != null) {
            existing.setName(dto.getName());
        }
        if (dto.getLastName() != null) {
            existing.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            existing.setEmail(dto.getEmail());
        }
        
        User updated = userRepository.save(existing);
        logger.info("User updated successfully with ID: " + id);
        return mapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting user with ID: " + id);
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        logger.info("Fetching user with email: " + email);
        User user = userRepository.findByEmailWithRolesAndPermissions(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDTO addRoleToUser(Long userId, Long roleId) {
        logger.info("Adding role ID " + roleId + " to user ID " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        return mapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDTO removeRoleFromUser(Long userId, Long roleId) {
        logger.info("Removing role ID " + roleId + " from user ID " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().remove(role);
        return mapper.toDto(userRepository.save(user));
    }

    @Override
    public void toggleUserStatus(Long id) {
        logger.info("Toggling status for user ID: " + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    int s = String.valueOf(user.getStatus()) == null ? 0 : user.getStatus();
    user.setStatus(s == 1 ? 0 : 1);
        userRepository.save(user);
    }


    @Override
    public List<User> findAllEntities() {
        logger.info("Fetching all users for MVC");
        return userRepository.findAll();
    }

    @Override
    public User createEntity(UserCreateDTO dto) {
        logger.info("Creating new user for MVC");
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = mapper.toEntity(dto);
        user.setStatus(1);
        if (user.getRoles() == null) {
            user.setRoles(new java.util.HashSet<>());
        }

        return userRepository.save(user);
    }

    @Override
    public User updateEntity(Long id, UserCreateDTO dto) {
        logger.info("Updating user for MVC with ID: " + id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User updated = mapper.toEntity(dto);
        updated.setId(existing.getId());
        return userRepository.save(updated);
    }

    @Override
    public void deleteEntity(Long id) {
        logger.info("Deleting user for MVC with ID: " + id);
        userRepository.deleteById(id);
    }

    @Override
    public User addRoleToUserEntity(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public User removeRoleFromUserEntity(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().remove(role);
        return userRepository.save(user);
    }

    @Override
    public void toggleUserStatusEntity(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    int s2 = String.valueOf(user.getStatus()) == null ? 0 : user.getStatus();
    user.setStatus(s2 == 1 ? 0 : 1);
        userRepository.save(user);
    }

    // En UserServiceImpl.java
    @Override
    public void changePassword(Long id, ChangePasswordDTO dto) {
        logger.info("Changing password for user ID: " + id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        
        // Validar que la nueva contraseña sea diferente
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("La nueva contraseña debe ser diferente a la actual");
        }
        
        // Validar longitud de la nueva contraseña
        if (dto.getNewPassword().length() < 6) {
            throw new RuntimeException("La nueva contraseña debe tener al menos 6 caracteres");
        }
        
        // Encriptar y guardar la nueva contraseña
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        
        logger.info("Password changed successfully for user ID: " + id);
    }
}
