package com.proyect.pensamiento_comp.repository;

import com.proyect.pensamiento_comp.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}
