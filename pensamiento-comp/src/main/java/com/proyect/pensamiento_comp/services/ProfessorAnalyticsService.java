package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.response.ProfessorAnalyticsDTO;


public interface ProfessorAnalyticsService {


    ProfessorAnalyticsDTO getProfessorAnalytics(Long professorId);
    ProfessorAnalyticsDTO getCurrentProfessorAnalytics();
}
