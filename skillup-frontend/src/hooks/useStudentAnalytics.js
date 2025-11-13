import { useEffect, useMemo, useState } from 'react';
import { ActivityDocuments } from '../services/activityDocuments';
import { SubmissionDocuments } from '../services/submissionDocuments';
import { ExerciseDocuments } from '../services/exerciseDocuments';
import { StudentsApi } from '../services/students';
import { UsersApi } from '../services/users';

function toDateString(value) {
  if (!value) return null;
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return null;
  return date.toISOString().slice(0, 10);
}

// Función local para verificar roles y evitar el import de roles.js (si no está disponible)
const checkAdminOrProfessor = (roles) => {
    const roleNames = (roles || []).map(r => (r.name || '').toUpperCase());
    return roleNames.some(r => ['ADMIN', 'ROLE_ADMIN', 'ADMINISTRATOR', 'PROFESOR', 'ROLE_PROFESOR', 'PROFESSOR'].includes(r));
};

export function useStudentAnalytics(user) {
  const [state, setState] = useState({
    loading: true,
    error: null,
    student: null,
    activities: [],
    achievements: [],
    leaderboard: [],
    practiceModules: [],
    totals: { submissions: 0, points: 0 },
  });

  useEffect(() => {
    let cancelled = false;

    async function load() {
      if (!user?.id) {
        setState(prev => ({ ...prev, loading: false, student: null }));
        return;
      }

      setState(prev => ({ ...prev, loading: true, error: null }));

      try {
        let student = await StudentsApi.findByUserId(user.id);
        console.log('🔍 Student found:', student);

        // Fallback: se mantiene la lógica de búsqueda por lista para admins/profesores
        // si findByUserId falla, pero se evita para estudiantes.
        if (!student) {
          const isAdminOrProfessor = checkAdminOrProfessor(user?.roles);
          if (isAdminOrProfessor) {
            try {
              const allStudents = await StudentsApi.list();
              const found = (allStudents || []).find(s => s.userId === user.id || s.user?.id === user.id);
              if (found) {
                console.warn('⚠️ useStudentAnalytics - Fallback: found student by userId from students.list()', found);
                student = found;
              } else {
                console.warn('⚠️ useStudentAnalytics - Fallback: no student record found for user id', user.id);
              }
            } catch (e) {
              // Este error 403 es esperado si el usuario no tiene permisos
              console.error('❌ useStudentAnalytics - Fallback error listing students:', e);
            }
          } else {
            console.debug('ℹ️ useStudentAnalytics - Skipping students.list() fallback for non-privileged user');
          }
        }

        if (!student) {
          if (!cancelled) setState(prev => ({ ...prev, loading: false, student: null }));
          return;
        }

        // 🛑 CAMBIO CLAVE: Lógica condicional para evitar el 403 al obtener la lista de estudiantes
        const adminOrProf = checkAdminOrProfessor(user?.roles);
        
        // Si es Admin/Profesor, pide la lista completa (puede fallar para estudiantes, pero lo evitamos)
        // Si es Estudiante, solo usa su propio registro: [student]
        const studentsPromise = adminOrProf ? StudentsApi.list() : Promise.resolve([student]);


        const [activitiesDocs, submissionsDocs, allStudents, users, exerciseDocs] = await Promise.all([
          ActivityDocuments.list(),
          SubmissionDocuments.list(),
          studentsPromise, // Contendrá la lista global o solo [student]
          UsersApi.list(),
          ExerciseDocuments.list(),
        ]);
        try { console.debug('useStudentAnalytics - activitiesDocs (first 5):', (activitiesDocs || []).slice(0,5)); } catch(e){}

       const submissionsByStudent = (submissionsDocs || []).filter(item => 
            item.studentId === student.id || // Comprueba si studentId es 1
            item.student_id === student.id || // Comprueba si student_id es 1 (snake_case)
            item.studentCode === student.code // 👈 NUEVA LÍNEA: Comprueba si studentCode es S1001
        );
        
        // 🚀 MEJORA: Optimizado para leer el campo 'total_points' de Mongo
        const pointsFromDoc = (doc) => {
          // 1. Prioriza 'total_points' a nivel principal (Coincide con tus datos de Mongo)
          if (doc?.total_points !== undefined && doc.total_points !== null) {
            return doc.total_points;
          }
          // 2. Verifica si hay puntos en grading.totalPoints (CamelCase - Tu DTO Java)
          if (doc?.grading?.totalPoints !== undefined && doc.grading.totalPoints !== null) {
            return doc.grading.totalPoints;
          }
          // 3. Verifica grading.total_points (Snake case - si la serialización es diferente)
          if (doc?.grading?.total_points !== undefined && doc.grading.total_points !== null) {
            return doc.grading.total_points;
          }
          return 0;
        };
        const totalPoints = submissionsByStudent.reduce((acc, d) => acc + pointsFromDoc(d), 0);

        // ... (Lógica de earliestSubmission, bestScore, y achievements) ...
        const earliestSubmission = submissionsByStudent.reduce((acc, item) => {
          const submitted = item?.submissionData?.submittedAt || item?.grading?.gradedAt;
          const current = submitted ? new Date(submitted) : null;
          if (!current || Number.isNaN(current.getTime())) return acc;
          if (!acc || current < acc) return current;
          return acc;
        }, null);
        const bestScore = submissionsByStudent.reduce((acc, d) => Math.max(acc, pointsFromDoc(d) || 0), 0);
        const bestScoreSubmission = submissionsByStudent.find(d => pointsFromDoc(d) >= 9.5);

        const achievements = [
          {
            id: 'first_submission',
            title: 'Primera entrega',
            description: 'Enviaste tu primera actividad al profesor.',
            points: 50,
            earned: submissionsByStudent.length > 0,
            earnedAt: earliestSubmission ? earliestSubmission.toISOString().slice(0, 10) : null,
          },
          {
            id: 'consistent_participation',
            title: 'Racha activa',
            description: 'Realizaste tres actividades o mas durante el periodo.',
            points: 80,
            earned: submissionsByStudent.length >= 3,
            earnedAt: submissionsByStudent.length >= 3 ? toDateString(submissionsByStudent[2]?.submissionData?.submittedAt || submissionsByStudent[2]?.grading?.gradedAt) : null,
          },
          {
            id: 'perfect_score',
            title: 'Resultado destacado',
            description: 'Alcanzaste una calificacion sobresaliente en una actividad.',
            points: 120,
            earned: bestScore >= 9.5,
            earnedAt: bestScore >= 9.5 ? toDateString(bestScoreSubmission?.submissionData?.submittedAt || bestScoreSubmission?.grading?.gradedAt) : null,
          },
        ];


        // 🌍 Lógica del Podio Global (Para Admins/Profesores) o Propio (Para Estudiantes)
        // El podio usará la lista de 'allStudents' que es completa o solo el estudiante logueado.
        const studentsForLeaderboard = allStudents || []; 
        const usersById = new Map(users.map(item => [item.id, item]));

        const leaderboard = studentsForLeaderboard
          .map(item => {
            // Filtra las entregas para CADA estudiante de la lista, usando 'student_id' (Mongo)
            const subs = (submissionsDocs || []).filter(sub => sub.student_id === item.id);
            const points = subs.reduce((acc, d) => acc + pointsFromDoc(d), 0);
            const userInfo = usersById.get(item.userId);
            return {
              studentId: item.id,
              name: `${userInfo?.name || ''} ${userInfo?.lastName || ''}`.trim() || 'Sin nombre',
              points,
              group: item.studyGroupAffiliation || 'General',
            };
          })
          .filter(entry => entry.points > 0 || entry.studentId === student.id) // Muestra al logueado aunque tenga 0
          .sort((a, b) => b.points - a.points)
          .map((entry, index) => ({ ...entry, rank: index + 1 }));

        const modules = (activitiesDocs || [])
          .flatMap(act => (act.exercises || []).map(ex => ({
            id: `${act.relationalId}-${ex.exerciseId}`,
            title: ex.description,
            difficulty: ex.difficulty,
            activityId: act.relationalId,
          })))
          .filter(Boolean)
          .reduce((acc, item) => {
            if (acc.find(existing => existing.id === item.id)) return acc;
            acc.push(item);
            return acc;
          }, [])
          .slice(0, 6);

        if (!cancelled) {
          setState({
            loading: false,
            error: null,
            student,
            activities: activitiesDocs || [],
            achievements,
            leaderboard,
            practiceModules: modules,
            totals: {
              submissions: submissionsByStudent.length,
              points: Number(totalPoints.toFixed(2)),
            },
          });
        }
      } catch (error) {
        console.error('❌ useStudentAnalytics - Error loading analytics:', error);
        if (!cancelled) {
          setState(prev => ({
            ...prev,
            loading: false,
            error,
          }));
        }
      }
    }

    load();

    // Poll less frequently to avoid noisy UI updates while navigating.
    const interval = setInterval(() => {
      load();
    }, 30000); // 30s
    return () => {
      cancelled = true;
      clearInterval(interval);
    };
  }, [user]);

  const value = useMemo(() => state, [state]);
  return value;
}