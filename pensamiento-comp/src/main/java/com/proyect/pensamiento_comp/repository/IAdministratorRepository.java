package com.proyect.pensamiento_comp.repository;

import com.proyect.pensamiento_comp.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdministratorRepository extends JpaRepository<Administrator, Long> {}

