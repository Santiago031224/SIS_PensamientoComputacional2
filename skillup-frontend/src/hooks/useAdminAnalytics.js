import { useEffect, useMemo, useState } from 'react';
import { UsersApi } from '../services/users';
import { StudentsApi } from '../services/students';
import { ProfessorsApi } from '../services/professors';
import { ActivityDocuments } from '../services/activityDocuments';
import { ScoresApi } from '../services/scores';
import { GroupsApi } from '../services/groups';
import { SubmissionDocuments } from '../services/submissionDocuments';

function weekKey(date) {
  if (!date) return null;
  
  // Si la fecha viene como string, convertir a Date
  const value = typeof date === 'string' ? new Date(date) : date;
  if (Number.isNaN(value.getTime())) return null;
  
  // Obtener año y semana
  const year = value.getFullYear();
  
  // Calcular número de semana (método ISO simple)
  const firstDayOfYear = new Date(year, 0, 1);
  const pastDaysOfYear = (value - firstDayOfYear) / 86400000;
  const week = Math.ceil((pastDaysOfYear + firstDayOfYear.getDay() + 1) / 7);
  
  return `Semana ${week.toString().padStart(2, '0')} de ${year}`;
}

export function useAdminAnalytics(periodId = null, year = null) {
  const [state, setState] = useState({
    loading: true,
    metrics: [],
    weeklyActivity: [],
    heatmap: [],
    alerts: [],
    submissionsSummary: { total: 0, pending: 0, graded: 0 },
    globalAverage: 0,
    platformUsage: { activeUsers: 0, totalUsers: 0, percentage: 0 }
  });

  useEffect(() => {
    let cancelled = false;
    
    async function load() {
      setState(prev => ({ ...prev, loading: true }));
      
      try {
        const [users, students, professors, activityDocs, scores, groups, submissionDocs] = await Promise.all([
          UsersApi.list(),
          StudentsApi.list(),
          ProfessorsApi.list(),
          ActivityDocuments.list(),
          ScoresApi.list(),
          GroupsApi.list(),
          SubmissionDocuments.list(),
        ]);

        // Filtrar actividades - AJUSTADO para estructura MongoDB
        let filteredActivities = activityDocs;
        
        // Si necesitas filtrar por año, usa startDate
        if (year) {
          filteredActivities = filteredActivities.filter(a => {
            if (!a.startDate) return false;
            const activityYear = new Date(a.startDate).getFullYear();
            return activityYear.toString() === year.toString();
          });
        }

        // Métricas básicas
        const activeUsers = users.filter(user => user.status === 1).length; // status es number, no string
        const metrics = [
          { 
            id: 'users', 
            label: 'Usuarios activos', 
            value: activeUsers, 
            change: `${users.length - activeUsers} inactivos` 
          },
          { 
            id: 'teachers', 
            label: 'Profesores', 
            value: professors.length, 
            change: 'Total registrados' 
          },
          { 
            id: 'students', 
            label: 'Estudiantes', 
            value: students.length, 
            change: 'Total registrados' 
          },
          { 
            id: 'activities', 
            label: 'Actividades publicadas', 
            value: filteredActivities.length, 
            change: year ? `Del año ${year}` : 'En el sistema' 
          },
        ];

        // Actividades por semana - AJUSTADO

        const weeklyBuckets = filteredActivities.reduce((acc, activity) => {
          // Intentar diferentes campos de fecha
          const dateField = activity.startDate || activity.createdAt || activity.created_at;
          const key = weekKey(dateField);
          if (!key) return acc;
          acc.set(key, (acc.get(key) || 0) + 1);
          return acc;
        }, new Map());

        const weeklyActivity = Array.from(weeklyBuckets.entries())
          .map(([key, value]) => ({ key, value }))
          .sort((a, b) => {
            // Ordenar por fecha (más reciente primero)
            return b.key.localeCompare(a.key);
          });

        // Heatmap de grupos - SIMPLIFICADO (ajusta según tu lógica real)
        const heatmap = groups.map(group => {
          // Simular datos - reemplaza con tu lógica real de estudiantes y calificaciones
          const average = 3.5 + Math.random() * 1.5; // Simulación entre 3.5 y 5.0
          return {
            label: group.name || `Grupo ${group.id}`,
            value: Number((average * 20).toFixed(1)), // Para mostrar en progreso
            average: Number(average.toFixed(2)),
          };
        });

        // Alertas - AJUSTADO
        const alerts = [];
        
        if (filteredActivities.length === 0) {
          alerts.push('No hay actividades registradas en el sistema.');
        } else {
          // Actividades próximas (en los próximos 7 días)
          const now = new Date();
          const nextWeek = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000);
          
          const upcomingActivities = filteredActivities.filter(activity => {
            if (!activity.startDate) return false;
            const startDate = new Date(activity.startDate);
            return startDate > now && startDate <= nextWeek;
          });

          if (upcomingActivities.length > 0) {
            alerts.push(`${upcomingActivities.length} actividades programadas para la próxima semana.`);
          }

          // Actividades sin ejercicios
          const activitiesWithoutExercises = filteredActivities.filter(
            activity => !activity.exercises || activity.exercises.length === 0
          );
          if (activitiesWithoutExercises.length > 0) {
            alerts.push(`${activitiesWithoutExercises.length} actividades sin ejercicios asignados.`);
          }
        }
        const totalSubmissions = submissionDocs.length;
        const pendingSubmissions = submissionDocs.filter(
          submission => submission.status !== 'GRADED' && submission.status !== 'REVIEWED'
        ).length;
        const gradedSubmissions = totalSubmissions - pendingSubmissions;

        const globalAverage = scores.length > 0 
          ? scores.reduce((acc, score) => acc + (score.pointsAwarded || 0), 0) / scores.length
          : 0;

        // Calcular porcentaje de uso de la plataforma (usuarios activos vs total)
        const totalUsers = users.length;
        const platformUsagePercentage = totalUsers > 0 
          ? Math.round((activeUsers / totalUsers) * 100)
          : 0;

        if (!cancelled) {
          setState({
            loading: false,
            metrics,
            weeklyActivity,
            heatmap: heatmap.slice(0, 5), // Mostrar solo 5 grupos
            alerts: alerts.slice(0, 3), // Mostrar solo 3 alertas
            submissionsSummary: {
              total: totalSubmissions,
              pending: pendingSubmissions,
              graded: gradedSubmissions,
            },
            globalAverage: Number(globalAverage.toFixed(2)),
            platformUsage: {
              activeUsers,
              totalUsers,
              percentage: platformUsagePercentage
            }
          });
        }

      } catch (error) {
        if (!cancelled) {
          console.error('Failed to load admin analytics', error);
          setState(prev => ({ 
            ...prev, 
            loading: false,
            alerts: ['Error cargando los datos de analytics. Intenta nuevamente.']
          }));
        }
      }
    }

    load();
    
    return () => {
      cancelled = true;
    };
  }, [periodId, year]);

  return useMemo(() => state, [state]);
}