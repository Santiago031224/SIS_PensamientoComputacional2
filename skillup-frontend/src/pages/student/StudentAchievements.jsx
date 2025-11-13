import React, { useMemo, useState } from 'react';
import { useAuth } from '../../services/AuthContext.jsx';
import { useStudentAnalytics } from '../../hooks/useStudentAnalytics.js';
import '../../styles/achivements.css';

export default function StudentAchievements(){
  const [filter, setFilter] = useState('all');
  const { user } = useAuth();
  const analytics = useStudentAnalytics(user);

  const achievements = useMemo(() => analytics.achievements || [], [analytics.achievements]);

  const filtered = useMemo(() => {
    if (filter === 'earned') return achievements.filter(a => a.earned);
    if (filter === 'pending') return achievements.filter(a => !a.earned);
    return achievements;
  }, [filter, achievements]);

  const stats = useMemo(() => {
    const earned = achievements.filter(a => a.earned).length;
    const total = achievements.length;
    const totalPoints = achievements.filter(a => a.earned).reduce((sum, a) => sum + (a.points || 0), 0);
    const percentage = total > 0 ? Math.round((earned / total) * 100) : 0;
    
    return { earned, total, totalPoints, percentage };
  }, [achievements]);

  if (analytics.loading) {
    return (
      <div className="achievements-container">
        <div className="loading-state">
          <div className="spinner"></div>
          <p>Cargando logros...</p>
        </div>
      </div>
    );
  }

  if (!analytics.student) {
    return (
      <div className="achievements-container">
        <div className="empty-state warning">
          <i className="bi bi-exclamation-triangle-fill"></i>
          <h3>Sin información</h3>
          <p>No hay información de estudiante para mostrar logros.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="achievements-container">
      <link
        href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css"
        rel="stylesheet"
      />
      
      {/* Header Section */}
      <div className="achievements-header">
        <div className="header-content">
          <div className="header-text">
            <h1>
              <i className="bi bi-trophy-fill"></i>
              Mis Logros
            </h1>
            <p>Consulta lo que ya desbloqueaste y lo que sigue en camino.</p>
          </div>
          
          {/* Stats Cards */}
          <div className="stats-row">
            <div className="stat-card">
              <div className="stat-icon earned">
                <i className="bi bi-check-circle-fill"></i>
              </div>
              <div className="stat-info">
                <div className="stat-value">{stats.earned}</div>
                <div className="stat-label">Desbloqueados</div>
              </div>
            </div>
            
            <div className="stat-card">
              <div className="stat-icon pending">
                <i className="bi bi-hourglass-split"></i>
              </div>
              <div className="stat-info">
                <div className="stat-value">{stats.total - stats.earned}</div>
                <div className="stat-label">Pendientes</div>
              </div>
            </div>
            
            <div className="stat-card">
              <div className="stat-icon points">
                <i className="bi bi-star-fill"></i>
              </div>
              <div className="stat-info">
                <div className="stat-value">{stats.totalPoints}</div>
                <div className="stat-label">Puntos Ganados</div>
              </div>
            </div>
            
            <div className="stat-card progress-card">
              <div className="progress-info">
                <span className="progress-label">Progreso Total</span>
                <span className="progress-percentage">{stats.percentage}%</span>
              </div>
              <div className="progress-bar-container">
                <div className="progress-bar-fill" style={{ width: `${stats.percentage}%` }}></div>
              </div>
            </div>
          </div>
        </div>
        
        {/* Filter Buttons */}
        <div className="filter-section">
          <div className="filter-buttons">
            <button
              className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
              onClick={() => setFilter('all')}
            >
              <i className="bi bi-collection"></i>
              Todos
              <span className="badge">{achievements.length}</span>
            </button>
            <button
              className={`filter-btn ${filter === 'earned' ? 'active' : ''}`}
              onClick={() => setFilter('earned')}
            >
              <i className="bi bi-check-circle"></i>
              Ganados
              <span className="badge earned">{stats.earned}</span>
            </button>
            <button
              className={`filter-btn ${filter === 'pending' ? 'active' : ''}`}
              onClick={() => setFilter('pending')}
            >
              <i className="bi bi-clock-history"></i>
              Pendientes
              <span className="badge pending">{stats.total - stats.earned}</span>
            </button>
          </div>
        </div>
      </div>

      {/* Achievements Grid */}
      <div className="achievements-grid">
        {filtered.map(achievement => (
          <div key={achievement.id} className={`achievement-card ${achievement.earned ? 'earned' : 'locked'}`}>
            <div className="achievement-icon">
              {achievement.earned ? (
                <i className="bi bi-trophy-fill"></i>
              ) : (
                <i className="bi bi-lock-fill"></i>
              )}
            </div>
            
            <div className="achievement-content">
              <h3 className="achievement-title">{achievement.title}</h3>
              <p className="achievement-description">{achievement.description}</p>
              
              <div className="achievement-footer">
                <div className="points-badge">
                  <i className="bi bi-star-fill"></i>
                  <span>+{achievement.points} pts</span>
                </div>
                
                <div className="achievement-status">
                  {achievement.earned ? (
                    <>
                      <i className="bi bi-check-circle-fill"></i>
                      <span className="date">{achievement.earnedAt}</span>
                    </>
                  ) : (
                    <>
                      <i className="bi bi-hourglass-split"></i>
                      <span>Por desbloquear</span>
                    </>
                  )}
                </div>
              </div>
            </div>
            
            {achievement.earned && (
              <div className="earned-overlay">
                <i className="bi bi-check-circle-fill"></i>
              </div>
            )}
          </div>
        ))}

        {filtered.length === 0 && (
          <div className="empty-state">
            <i className="bi bi-inbox"></i>
            <h3>No hay logros para mostrar</h3>
            <p>Intenta cambiar el filtro para ver más logros.</p>
          </div>
        )}
      </div>
    </div>
  );
}