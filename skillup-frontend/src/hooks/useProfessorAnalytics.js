import { useEffect, useMemo, useState } from 'react';
import { ProfessorsApi } from '../services/professors';
import { TeachingAssignmentsApi } from '../services/teachingAssignments';
import { ActivityDocuments } from '../services/activityDocuments';
import { StudentsApi } from '../services/students';
import { UsersApi } from '../services/users';
import { SubmissionDocuments } from '../services/submissionDocuments';
import { GroupsApi } from '../services/groups';
// Exercises and activity-exercises are derived from document structure

const STATUS_GRADED = 'GRADED';

function average(values) {
  if (!values.length) return 0;
  const sum = values.reduce((acc, value) => acc + value, 0);
  return sum / values.length;
}

export function useProfessorAnalytics(user) {
  const [state, setState] = useState({
    loading: true,
    professor: null,
    assignments: [],
    activities: [],
    groups: [],
    summary: { groups: 0, completed: 0, pending: 0, average: 0 },
    groupPerformance: [],
    upcomingActivities: [],
    feedbackQueue: [],
    gradingQueue: [],
  });

  useEffect(() => {
    let cancelled = false;

    async function load() {
      if (!user?.id) {
        setState(prev => ({ ...prev, loading: false, professor: null }));
        return;
      }
      setState(prev => ({ ...prev, loading: true }));

      try {
        
        const [professors, assignments, activityDocs, students, users, submissionDocs, groups] = await Promise.all([
          ProfessorsApi.list(),
          TeachingAssignmentsApi.list(),
          ActivityDocuments.list(),
          StudentsApi.list(),
          UsersApi.list(),
          SubmissionDocuments.list(),
          GroupsApi.list(),
        ]);

        const professor = professors.find(item => item.userId === user.id) || null;
        
        if (!professor) {
          if (!cancelled) setState(prev => ({ ...prev, loading: false, professor: null }));
          return;
        }

        const assignmentsForProfessor = assignments.filter(item => item.professorId === professor.id);
        
        const groupsById = new Map(groups.map(item => [item.id, item]));
  const activitiesByProfessor = Array.isArray(activityDocs) ? activityDocs : [];
  const activitiesIds = new Set(activitiesByProfessor.map(item => item.id || item.relationalId));

        const usersById = new Map(users.map(item => [item.id, item]));
        const submissionsByProfessorStudents = submissionDocs.filter(sub => {
          const student = students.find(stu => stu.id === sub.studentId);
          if (!student) return false;
          if (!student.studyGroupAffiliation) return false;
          return assignmentsForProfessor.some(assignment => {
            const group = groupsById.get(assignment.groupId);
            return group && group.name === student.studyGroupAffiliation;
          });
        });

        const pointsFromDoc = (doc) => {
          const g = doc?.grading;
          if (!g) return 0;
          if (typeof g.totalPoints === 'number') return g.totalPoints || 0;
          if (Array.isArray(g.breakdown)) return g.breakdown.reduce((acc, it) => acc + (it.pointsAwarded || 0), 0);
          return 0;
        };

        const isGraded = (sub) => sub.status === STATUS_GRADED || !!sub.grading;

        const completed = submissionsByProfessorStudents.filter(isGraded).length;
        const pending = submissionsByProfessorStudents.length - completed;
        const averageScore = average(submissionsByProfessorStudents.map(pointsFromDoc));

        const groupPerformance = assignmentsForProfessor.map(assignment => {
          const group = groupsById.get(assignment.groupId);
          const groupName = group?.name || 'Sin grupo';
          const studentsInGroup = students.filter(student => (student.studyGroupAffiliation || '') === groupName);
          const studentIds = new Set(studentsInGroup.map(student => student.id));
          const submissionsInGroup = submissionDocs.filter(sub => studentIds.has(sub.studentId));
          const completedCount = submissionsInGroup.filter(isGraded).length;
          const pendingCount = submissionsInGroup.length - completedCount;
          const averageGroupScore = average(submissionsInGroup.map(pointsFromDoc));
          return {
            assignmentId: assignment.id,
            groupId: assignment.groupId,
            groupName,
            average: Number(averageGroupScore.toFixed(2)),
            completed: completedCount,
            pending: pendingCount,
          };
        });

        const now = new Date();
        const upcomingActivities = activitiesByProfessor
          .filter(activity => {
            const due = activity?.endDate || activity?.dueDate || activity?.closeDate || null;
            if (!due) return false;
            const endDate = new Date(due);
            return !Number.isNaN(endDate.getTime()) && endDate >= now;
          })
          .sort((a, b) => new Date(a.endDate) - new Date(b.endDate))
          .slice(0, 6)
          .map(activity => {
            // Try to find matching assignment - activities don't directly link to assignments in document model
            // so we'll use a default weight percentage
            return {
              id: activity.id || activity.relationalId,
              title: activity.title || 'Actividad',
              dueDate: activity.endDate || activity.dueDate || activity.closeDate || null,
              groupName: 'Todos los grupos', // Activities in MongoDB don't link to specific groups
              weight: 1.0, // Default 100% weight since we can't determine it from documents
            };
          });
        // Build quick lookup from exerciseId -> { description, activityTitle }
        const exerciseInfo = new Map();
        (activitiesByProfessor || []).forEach(act => {
          const title = act?.title || 'Actividad';
          (act?.exercises || []).forEach(rel => {
            const exId = rel?.exerciseId;
            if (exId != null && !exerciseInfo.has(exId)) {
              exerciseInfo.set(exId, {
                description: rel?.description || `Ejercicio ${exId}`,
                activityTitle: title,
              });
            }
          });
        });

        const feedbackQueue = submissionsByProfessorStudents
          .filter(sub => !isGraded(sub))
          .map(sub => {
            const student = students.find(item => item.id === sub.studentId);
            const userInfo = student ? usersById.get(student.userId) : null;
            const ex = exerciseInfo.get(sub.exerciseId);
            return {
              id: sub.id,
              student: `${userInfo?.name || ''} ${userInfo?.lastName || ''}`.trim() || 'Estudiante',
              exercise: ex?.description || `Ejercicio ${sub.exerciseId}`,
              activity: ex?.activityTitle || null,
              submittedAt: sub.date || sub?.submissionData?.submittedAt || null,
              status: sub.status,
            };
          })
          .sort((a, b) => new Date(a.submittedAt) - new Date(b.submittedAt));

        const gradingQueue = submissionsByProfessorStudents
          .map(sub => {
            const student = students.find(item => item.id === sub.studentId);
            const userInfo = student ? usersById.get(student.userId) : null;
            const ex = exerciseInfo.get(sub.exerciseId);
            return {
              id: sub.id,
              student: `${userInfo?.name || ''} ${userInfo?.lastName || ''}`.trim() || 'Estudiante',
              exercise: ex?.description || `Ejercicio ${sub.exerciseId}`,
              activity: ex?.activityTitle || null,
              submittedAt: sub.date || sub?.submissionData?.submittedAt || null,
              score: pointsFromDoc(sub) || null,
              status: sub.status,
            };
          })
          .sort((a, b) => new Date(b.submittedAt) - new Date(a.submittedAt));

        if (!cancelled) {
          setState({
            loading: false,
            professor,
            assignments: assignmentsForProfessor,
            activities: activitiesByProfessor,
            groups,
            summary: {
              groups: assignmentsForProfessor.length,
              completed,
              pending,
              average: Number(averageScore.toFixed(2)),
            },
            groupPerformance,
            upcomingActivities,
            feedbackQueue,
            gradingQueue,
          });
        }
      } catch (error) {
        if (!cancelled) {
          setState(prev => ({ ...prev, loading: false }));
          console.error('Failed to load professor analytics', error);
        }
      }
    }

    load();
    return () => {
      cancelled = true;
    };
  }, [user]);

  return useMemo(() => state, [state]);
}
