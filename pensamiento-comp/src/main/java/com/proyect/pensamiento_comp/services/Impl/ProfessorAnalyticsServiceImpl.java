package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.documents.ActivityDocument;
import com.proyect.pensamiento_comp.documents.repository.ActivityDocumentRepository;
import com.proyect.pensamiento_comp.documents.repository.SubmissionDocumentRepository;
import com.proyect.pensamiento_comp.dto.response.ProfessorAnalyticsDTO;
import com.proyect.pensamiento_comp.dto.response.TeachingAssignmentResponseDTO;
import com.proyect.pensamiento_comp.mapper.TeachingAssignmentMapper;
import com.proyect.pensamiento_comp.model.Group;
import com.proyect.pensamiento_comp.model.Professor;
import com.proyect.pensamiento_comp.model.TeachingAssignment;
import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.repository.IGroupRepository;
import com.proyect.pensamiento_comp.repository.IProfessorRepository;
import com.proyect.pensamiento_comp.repository.ITeachingAssignmentRepository;
import com.proyect.pensamiento_comp.security.CustomUserDetails;
import com.proyect.pensamiento_comp.services.ProfessorAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorAnalyticsServiceImpl implements ProfessorAnalyticsService {

    private final ITeachingAssignmentRepository teachingAssignmentRepository;
    private final IProfessorRepository professorRepository;
    private final IGroupRepository groupRepository;
    private final ActivityDocumentRepository activityDocumentRepository;
    private final SubmissionDocumentRepository submissionDocumentRepository;
    private final TeachingAssignmentMapper teachingAssignmentMapper;

    @Override
    @Transactional(readOnly = true)
    public ProfessorAnalyticsDTO getProfessorAnalytics(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        ProfessorAnalyticsDTO analytics = new ProfessorAnalyticsDTO();

        // 1. Get teaching assignments from Oracle
        List<TeachingAssignment> teachingAssignments = teachingAssignmentRepository.findByProfessorId(professorId);
        List<TeachingAssignmentResponseDTO> teachingAssignmentDTOs = teachingAssignmentMapper.toDtoList(teachingAssignments);
        analytics.setTeachingAssignments(teachingAssignmentDTOs);

        // 2. Get activities from MongoDB
        List<ActivityDocument> activities = activityDocumentRepository.findByProfessorId(professorId);
        List<ProfessorAnalyticsDTO.ActivitySummaryDTO> activitySummaries = activities.stream()
                .map(this::mapActivityToSummary)
                .collect(Collectors.toList());
        analytics.setActivities(activitySummaries);

        // 3. Get groups
        Set<Long> groupIds = teachingAssignments.stream()
                .map(ta -> ta.getGroup() != null ? ta.getGroup().getId() : null)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        List<ProfessorAnalyticsDTO.GroupSummaryDTO> groupSummaries = new ArrayList<>();
        for (Long groupId : groupIds) {
            Group group = groupRepository.findById(groupId).orElse(null);
            if (group != null) {
                groupSummaries.add(mapGroupToSummary(group));
            }
        }
        analytics.setGroups(groupSummaries);

        // 4. Calculate statistics
        ProfessorAnalyticsDTO.ProfessorStatistics statistics = calculateStatistics(
                teachingAssignments, activities, groupSummaries);
        analytics.setStatistics(statistics);

        return analytics;
    }

    @Override
    public ProfessorAnalyticsDTO getCurrentProfessorAnalytics() {
        // Get current user from security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        User user = null;
        if (principal instanceof CustomUserDetails) {
            user = ((CustomUserDetails) principal).getUser();
        } else if (principal instanceof User) {
            user = (User) principal;
        }
        
        if (user == null) {
            throw new RuntimeException("Unable to get current user from security context");
        }
        
        Professor professor = professorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Professor profile not found for current user"));
        return getProfessorAnalytics(professor.getId());
    }

    private ProfessorAnalyticsDTO.ActivitySummaryDTO mapActivityToSummary(ActivityDocument activity) {
        ProfessorAnalyticsDTO.ActivitySummaryDTO summary = new ProfessorAnalyticsDTO.ActivitySummaryDTO();
        summary.setId(activity.getRelationalId() != null ? activity.getRelationalId().toString() : null);
        summary.setRelationalId(activity.getRelationalId());
        summary.setTitle(activity.getTitle());
        summary.setDescription(activity.getDescription());
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        summary.setStartDate(activity.getStartDate() != null ? activity.getStartDate().format(formatter) : null);
        summary.setEndDate(activity.getEndDate() != null ? activity.getEndDate().format(formatter) : null);
        
        summary.setExerciseCount(activity.getExercises() != null ? activity.getExercises().size() : 0);
        
        // Count submissions for this activity using MongoDB ID (relationalId)
        int submissionCount = 0;
        if (activity.getRelationalId() != null) {
            submissionCount = (int) submissionDocumentRepository.findByActivityId(activity.getRelationalId().toString()).size();
        }
        summary.setSubmissionCount(submissionCount);
        
        return summary;
    }

    private ProfessorAnalyticsDTO.GroupSummaryDTO mapGroupToSummary(Group group) {
        ProfessorAnalyticsDTO.GroupSummaryDTO summary = new ProfessorAnalyticsDTO.GroupSummaryDTO();
        summary.setId(group.getId());
        summary.setName(group.getName());
        summary.setPeriodCode(group.getPeriod() != null ? group.getPeriod().getCode() : null);
        
        // Count students in this group - handle potential lazy loading issues
        int studentCount = 0;
        try {
            studentCount = group.getStudents() != null ? group.getStudents().size() : 0;
        } catch (Exception e) {
            // If lazy loading fails, default to 0
            studentCount = 0;
        }
        summary.setStudentCount(studentCount);
        
        // Get course name from teaching assignment
        TeachingAssignment assignment = teachingAssignmentRepository.findByGroupId(group.getId()).stream()
                .findFirst()
                .orElse(null);
        
        if (assignment != null && assignment.getCourse() != null) {
            summary.setCourseName(assignment.getCourse().getName());
        }
        
        return summary;
    }

    private ProfessorAnalyticsDTO.ProfessorStatistics calculateStatistics(
            List<TeachingAssignment> teachingAssignments,
            List<ActivityDocument> activities,
            List<ProfessorAnalyticsDTO.GroupSummaryDTO> groups) {
        
        ProfessorAnalyticsDTO.ProfessorStatistics stats = new ProfessorAnalyticsDTO.ProfessorStatistics();
        
        stats.setTotalTeachingAssignments(teachingAssignments.size());
        stats.setTotalActivities(activities.size());
        stats.setTotalGroups(groups.size());
        
        // Calculate total students
        int totalStudents = groups.stream()
                .mapToInt(ProfessorAnalyticsDTO.GroupSummaryDTO::getStudentCount)
                .sum();
        stats.setTotalStudents(totalStudents);
        
        // Calculate active and completed activities
        LocalDate now = LocalDate.now();
        long activeActivities = activities.stream()
                .filter(a -> a.getStartDate() != null && a.getEndDate() != null)
                .filter(a -> !now.isBefore(a.getStartDate()) && !now.isAfter(a.getEndDate()))
                .count();
        
        long completedActivities = activities.stream()
                .filter(a -> a.getEndDate() != null)
                .filter(a -> now.isAfter(a.getEndDate()))
                .count();
        
        stats.setActiveActivities((int) activeActivities);
        stats.setCompletedActivities((int) completedActivities);
        
        return stats;
    }
}
