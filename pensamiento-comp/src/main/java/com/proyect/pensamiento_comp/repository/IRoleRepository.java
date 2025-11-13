package com.proyect.pensamiento_comp.repository;

import com.proyect.pensamiento_comp.model.Permission;
import com.proyect.pensamiento_comp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    List<Role> findByPermissionsContains(Permission permission);

}
