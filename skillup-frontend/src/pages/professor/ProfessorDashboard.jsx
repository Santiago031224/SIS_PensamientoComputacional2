import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { useAuth } from '../../services/AuthContext';
import { useProfessorAnalytics } from '../../hooks/useProfessorAnalytics';
import '../../styles/professor.css';

const ChartIcon = () => <span className="cardIcon">📊</span>;
const CalendarIcon = () => <span className="cardIcon">📅</span>;
const ChatIcon = () => <span className="cardIcon">💬</span>;

export default function ProfessorDashboard() {
  const { user } = useAuth();
  const { loading, professor, summary, groupPerformance, upcomingActivities, feedbackQueue } = useProfessorAnalytics(user);
  const averageDisplay = Number(summary?.average || 0).toFixed(1);

  if (loading) {
    return (
      <div className="loadingContainer">
        <div className="spinner" role="status" aria-label="Cargando analíticas del profesor" />
      </div>
    );
  }

  if (!professor) {
    return (
      <div className="container">
        <div className="alert alertWarning" role="alert">
          No encontramos información del profesor asociada al usuario actual. Verifica tu cuenta o contacta al administrador.
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      {/* Header Section */}
      <div className="header">
        <div className="headerContent">
          <h2>Panel del Profesor</h2>
          <p>Monitorea avance, planea nuevas actividades y califica entregas.</p>
        </div>
        <div className="statsContainer">
          <div className="statCard">
            <span className="statLabel">Grupos</span>
            <div className="statValue">{summary.groups}</div>
          </div>
          <div className="statCard">
            <span className="statLabel">Entregas Revisadas</span>
            <div className="statValue">{summary.completed}</div>
          </div>
          <div className="statCard">
            <span className="statLabel">Pendientes</span>
            <div className="statValue">{summary.pending}</div>
          </div>
          <div className="statCard">
            <span className="statLabel">Promedio General</span>
            <div className="statValue">{averageDisplay}</div>
          </div>
        </div>
      </div>

      {/* Main Content Grid */}
      <div className="contentGrid">
        {/* Performance Chart Section */}
        <section>
          <div className="card">
            <div className="cardHeader">
              <h5 className="cardTitle">
                <ChartIcon />
                Desempeño por Grupo
              </h5>
            </div>
            <div className="cardBody">
              <div className="chartContainer">
                <ResponsiveContainer>
                  <BarChart data={groupPerformance} margin={{ top: 10, right: 10, bottom: 0, left: 0 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="groupName" interval={0} angle={-15} textAnchor="end" height={50} />
                    <YAxis />
                    <Tooltip />
                    <Bar dataKey="average" name="Promedio" fill="#667eea" radius={[4,4,0,0]} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
              <div className="tableResponsive">
                <table className="table">
                  <thead>
                    <tr>
                      <th scope="col">Grupo</th>
                      <th scope="col">Promedio</th>
                      <th scope="col">Completadas</th>
                      <th scope="col">Pendientes</th>
                    </tr>
                  </thead>
                  <tbody>
  {groupPerformance.length === 0 ? (
    <tr>
      <td colSpan={4} className="emptyState">
        No hay datos de desempeño disponibles.
      </td>
    </tr>
  ) : (
    groupPerformance.map(item => {
      const rowClass = item.pending === 0 ? 'completedRow' : 'pendingRow'; // verde si todo completado, gris si pendiente
      return (
        <tr key={`${item.groupId}-${item.assignmentId}`} className={rowClass}>
          <td>{item.groupName}</td>
          <td>{item.average.toFixed(1)}</td>
          <td>{item.completed}</td>
          <td>{item.pending}</td>
        </tr>
      );
    })
  )}
</tbody>

                </table>
              </div>
            </div>
          </div>
        </section>

        {/* Upcoming Activities Section */}
        <section>
          <div className="card">
            <div className="cardHeader">
              <h5 className="cardTitle">
                <CalendarIcon />
                Próximas Actividades
              </h5>
            </div>
            <div className="cardBody">
              <ul className="activityList">
                {upcomingActivities.length === 0 ? (
                  <li className="emptyState">
                    <div className="emptyStateIcon">📅</div>
                    No hay actividades próximas programadas.
                  </li>
                ) : (
                  upcomingActivities.map(activity => (
                    <li key={activity.id} className="activityItem">
                      <div className="activityContent">
                        <h6>{activity.title}</h6>
                        <div className="activityMeta">
                          Entrega: {new Date(activity.dueDate).toLocaleDateString()}
                        </div>
                        <div className="activityMeta">
                          Grupo: {activity.groupName}
                        </div>
                      </div>
                      {activity.weight != null && activity.weight > 0 && (
                        <span className="activityWeight">
                          {Math.round(activity.weight * 100)}%
                        </span>
                      )}
                    </li>
                  ))
                )}
              </ul>
            </div>
          </div>
        </section>
      </div>

      {/* Feedback Queue Section */}
      <section className="feedbackSection">
        <div className="card">
          <div className="cardHeader">
            <h5 className="cardTitle">
              <ChatIcon />
              Cola de Retroalimentación
            </h5>
          </div>
          <div className="cardBody">
            <div className="tableResponsive">
              <table className="table">
                <thead>
                  <tr>
                    <th scope="col">Estudiante</th>
                    <th scope="col">Actividad</th>
                    <th scope="col">Calificación</th>
                    <th scope="col" className="textEnd">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {feedbackQueue.length === 0 ? (
                    <tr>
                      <td colSpan={4} className="emptyState">
                        No tienes entregas pendientes de retroalimentación.
                      </td>
                    </tr>
                  ) : (
                    feedbackQueue.map(row => (
                      <tr key={row.id}>
                        <td>{row.student}</td>
                        <td>
                          <div className="activityContent">
                            <h6>{row.activity || 'Actividad sin título'}</h6>
                            <div className="activityMeta">{row.exercise}</div>
                          </div>
                        </td>
                        <td>
                          <span className={`statusBadge ${row.status === 'GRADED' ? 'statusGraded' : 'statusPending'}`}>
                            {row.status === 'GRADED' ? 'Calificada' : 'Sin evaluar'}
                          </span>
                        </td>
                        <td className="textEnd">
                          <button type="button" className="actionButton">
                            Abrir entrega
                          </button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
