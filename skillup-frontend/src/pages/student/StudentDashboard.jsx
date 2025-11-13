import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../services/AuthContext.jsx';
import { useStudentAnalytics } from '../../hooks/useStudentAnalytics.js';

export default function StudentDashboard(){
  const navigate = useNavigate();
  const { user } = useAuth();
  const analytics = useStudentAnalytics(user);

  if (analytics.loading) {
    return (
      <div className="dashboard-container">
        <div className="loading-state">
          <div className="spinner"></div>
          <p>Cargando tu panel...</p>
        </div>
      </div>
    );
  }

  if (!analytics.student) {
    return (
      <div className="dashboard-container">
        <div className="empty-state warning">
          <i className="bi bi-exclamation-triangle-fill"></i>
          <h3>Sin información de estudiante</h3>
          <p>No encontramos información de estudiante asociada a tu cuenta. Solicita soporte al administrador.</p>
        </div>
      </div>
    );
  }

  const { activities, achievements, leaderboard, practiceModules, totals } = analytics;
  const unlockedAchievements = achievements.filter(item => item.earned);
  const nextAchievement = achievements.find(item => !item.earned) || null;
  const topLeaderboard = leaderboard.slice(0, 3);

  return (
    <div className="dashboard-container">
      <link
        href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css"
        rel="stylesheet"
      />
      
      {/* Header */}
      <header className="dashboard-header">
        <div className="header-text">
          <h1>¡Bienvenido de vuelta!</h1>
          <p>Sesión activa: <strong>{user?.email || '-'}</strong></p>
        </div>
      </header>

      {/* Stats Cards */}
      <div className="stats-grid">
        <div className="stat-card activities">
          <div className="stat-icon">
            <i className="bi bi-list-check"></i>
          </div>
          <div className="stat-content">
            <div className="stat-label">Actividades</div>
            <div className="stat-value">{activities.length}</div>
            <div className="stat-sublabel">Asignadas</div>
          </div>
        </div>

        <div className="stat-card achievements">
          <div className="stat-icon">
            <i className="bi bi-trophy-fill"></i>
          </div>
          <div className="stat-content">
            <div className="stat-label">Logros</div>
            <div className="stat-value">{unlockedAchievements.length}</div>
            <div className="stat-sublabel">Obtenidos</div>
          </div>
        </div>

        <div className="stat-card points">
          <div className="stat-icon">
            <i className="bi bi-star-fill"></i>
          </div>
          <div className="stat-content">
            <div className="stat-label">Puntos</div>
            <div className="stat-value">{totals.points}</div>
            <div className="stat-sublabel">Acumulados</div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="content-grid">
        {/* Activities Section */}
        <section className="activities-section">
          <div className="section-card">
            <div className="section-header">
              <h2>
                <i className="bi bi-calendar-check"></i>
                Actividades Asignadas
              </h2>
              <span className="count-badge">{activities.length}</span>
            </div>

            {activities.length === 0 ? (
              <div className="empty-message">
                <i className="bi bi-inbox"></i>
                <p>No hay actividades asignadas por ahora</p>
              </div>
            ) : (
              <div className="activities-list">
                {activities.map((activity, idx) => {
                  const key = activity?.id ?? activity?.relationalId ?? `${activity?.title ?? 'activity'}-${idx}`;
                  return (
                    <div key={key} className="activity-item">
                      <div className="activity-info">
                        <h3>{activity.title}</h3>
                        <div className="activity-dates">
                          <i className="bi bi-calendar-event"></i>
                          <span>
                            {activity.startDate || activity?.schedule?.start_date?.['$date'] || 'Sin fecha'}
                            {' → '}
                            {activity.endDate || activity?.schedule?.end_date?.['$date'] || 'Sin fecha'}
                          </span>
                        </div>
                        <p className="activity-description">{activity.description}</p>
                      </div>
                      <button
                        className="btn-solve"
                        onClick={() => {
                          console.debug('Resolver clicked - activity object:', activity);
                          if (activity?.id) {
                            navigate(`/student/activity/${activity.id}`);
                            return;
                          }
                          const rel = activity?.relationalId;
                          if (rel && !Number.isNaN(Number(rel)) && Number.isFinite(Number(rel))) {
                            navigate(`/student/activity/${Number(rel)}`);
                            return;
                          }
                          import('../../services/stringUtils.js')
                            .then(({ slugify }) => {
                              const slug = slugify(activity?.title || activity?.description || String(Date.now()));
                              navigate(`/student/activity/${encodeURIComponent(slug)}`);
                            })
                            .catch(err => console.warn('Slug navigation failed', err));
                          console.warn('Cannot resolve activity - missing numeric id/relationalId', activity);
                          alert('Actividad no disponible. Contacta al administrador.');
                        }}
                      >
                        <i className="bi bi-play-circle-fill"></i>
                        Resolver
                      </button>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        </section>

        {/* Sidebar */}
        <aside className="sidebar">
          {/* Achievements Card */}
          <div className="section-card achievements-card">
            <div className="section-header">
              <h2>
                <i className="bi bi-trophy"></i>
                Mis Logros
              </h2>
            </div>

            <div className="achievements-list">
              {achievements.slice(0, 4).map((achievement, idx) => (
                <div key={achievement?.id ?? `${achievement?.title ?? 'ach'}-${idx}`} className="achievement-mini">
                  <div className="achievement-icon-mini">
                    {achievement.earned ? (
                      <i className="bi bi-check-circle-fill"></i>
                    ) : (
                      <i className="bi bi-lock-fill"></i>
                    )}
                  </div>
                  <div className="achievement-text">
                    <div className="achievement-title-mini">{achievement.title}</div>
                    <div className="achievement-desc-mini">{achievement.description}</div>
                  </div>
                  <div className="achievement-badge-mini">
                    {achievement.earned ? (
                      <>
                        <span className="points-earned">+{achievement.points}</span>
                        <span className="date-earned">{achievement.earnedAt}</span>
                      </>
                    ) : (
                      <span className="points-locked">Bloqueado</span>
                    )}
                  </div>
                </div>
              ))}
            </div>

            {nextAchievement && (
              <div className="next-achievement">
                <i className="bi bi-bullseye"></i>
                <div>
                  <strong>Próximo objetivo:</strong>
                  <div>{nextAchievement.title}</div>
                </div>
              </div>
            )}

            <button 
              className="btn-view-all"
              onClick={() => navigate('/student/achievements')}
            >
              Ver todos los logros
              <i className="bi bi-arrow-right"></i>
            </button>
          </div>

          {/* Leaderboard Card */}
          <div className="section-card leaderboard-card">
            <div className="section-header">
              <h2>
                <i className="bi bi-bar-chart-fill"></i>
                Podio del Grupo
              </h2>
            </div>

            {topLeaderboard.length === 0 ? (
              <div className="empty-message small">
                <i className="bi bi-hourglass-split"></i>
                <p>Aún no hay puntajes registrados</p>
              </div>
            ) : (
              <div className="leaderboard-list">
                {topLeaderboard.map((entry, idx) => {
                  const medals = ['🥇', '🥈', '🥉'];
                  return (
                    <div key={entry?.studentId ?? `${entry?.name ?? 'entry'}-${idx}`} className="leaderboard-item">
                      <div className="rank-section">
                        <span className="medal">{medals[idx]}</span>
                        <span className="rank-number">#{entry.rank}</span>
                      </div>
                      <div className="student-name">{entry.name}</div>
                      <div className="student-points">{entry.points.toFixed(1)} pts</div>
                    </div>
                  );
                })}
              </div>
            )}

            <button 
              className="btn-view-all"
              onClick={() => navigate('/student/leaderboard')}
            >
              Ver tabla completa
              <i className="bi bi-arrow-right"></i>
            </button>
          </div>
        </aside>
      </div>

      {/* Practice Modules */}
      <section className="practice-section">
        <div className="section-card">
          <div className="section-header">
            <div>
              <h2>
                <i className="bi bi-lightning-charge-fill"></i>
                Módulos de Práctica
              </h2>
              <p className="section-subtitle">Refuerza tus habilidades entre actividades</p>
            </div>
          </div>

          {practiceModules.length === 0 ? (
            <div className="empty-message">
              <i className="bi bi-puzzle"></i>
              <p>No hay ejercicios asociados a tus actividades todavía</p>
            </div>
          ) : (
            <div className="practice-grid">
              {practiceModules.map((module, idx) => (
                <div key={module?.id ?? `${module?.activityId ?? 'mod'}-${idx}`} className="practice-card">
                  <div className="practice-icon">
                    <i className="bi bi-code-square"></i>
                  </div>
                  <h3>{module.title}</h3>
                  <div className="difficulty-badge">
                    <i className="bi bi-speedometer"></i>
                    {module.difficulty}
                  </div>
                  <button
                    className="btn-practice"
                    onClick={() => navigate(`/student/activity/${module.activityId}`)}
                  >
                    <i className="bi bi-play-fill"></i>
                    Practicar
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      </section>

      <style jsx>{`
        * {
          box-sizing: border-box;
          margin: 0;
          padding: 0;
        }

        .dashboard-container {
          padding: 2rem;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          min-height: 100vh;
          font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', sans-serif;
        }

        /* Header */
        .dashboard-header {
          margin-bottom: 2rem;
        }

        .header-text h1 {
          font-size: 2.5rem;
          font-weight: 800;
          color: white;
          margin-bottom: 0.5rem;
          text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
        }

        .header-text p {
          color: rgba(255, 255, 255, 0.9);
          font-size: 1rem;
        }

        .header-text strong {
          font-weight: 700;
        }

        /* Stats Grid */
        .stats-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
          gap: 1.5rem;
          margin-bottom: 2rem;
        }

        .stat-card {
          background: white;
          border-radius: 20px;
          padding: 1.5rem;
          display: flex;
          align-items: center;
          gap: 1.25rem;
          box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
          transition: all 0.3s ease;
        }

        .stat-card:hover {
          transform: translateY(-5px);
          box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
        }

        .stat-icon {
          width: 70px;
          height: 70px;
          border-radius: 16px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 2rem;
          flex-shrink: 0;
        }

        .stat-card.activities .stat-icon {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
        }

        .stat-card.achievements .stat-icon {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          color: white;
        }

        .stat-card.points .stat-icon {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          color: white;
        }

        .stat-content {
          flex: 1;
        }

        .stat-label {
          font-size: 0.9rem;
          color: #6c757d;
          font-weight: 500;
          margin-bottom: 0.25rem;
        }

        .stat-value {
          font-size: 2.5rem;
          font-weight: 800;
          color: #2c3e50;
          line-height: 1;
          margin-bottom: 0.25rem;
        }

        .stat-sublabel {
          font-size: 0.8rem;
          color: #95a5a6;
        }

        /* Content Grid */
        .content-grid {
          display: grid;
          grid-template-columns: 1fr 400px;
          gap: 2rem;
          margin-bottom: 2rem;
        }

        /* Section Card */
        .section-card {
          background: white;
          border-radius: 20px;
          padding: 2rem;
          box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }

        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 1.5rem;
          padding-bottom: 1rem;
          border-bottom: 2px solid #f0f0f0;
        }

        .section-header h2 {
          font-size: 1.5rem;
          font-weight: 700;
          color: #2c3e50;
          display: flex;
          align-items: center;
          gap: 0.5rem;
          margin: 0;
        }

        .section-header i {
          font-size: 1.75rem;
          color: #667eea;
        }

        .section-subtitle {
          font-size: 0.9rem;
          color: #7f8c8d;
          margin-top: 0.25rem;
        }

        .count-badge {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          padding: 0.5rem 1rem;
          border-radius: 12px;
          font-weight: 700;
          font-size: 1.1rem;
        }

        /* Activities */
        .activities-section {
          min-height: 500px;
        }

        .activities-list {
          display: flex;
          flex-direction: column;
          gap: 1rem;
        }

        .activity-item {
          background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
          border-radius: 16px;
          padding: 1.5rem;
          display: flex;
          justify-content: space-between;
          align-items: center;
          gap: 1.5rem;
          transition: all 0.3s ease;
          border: 2px solid transparent;
        }

        .activity-item:hover {
          transform: translateX(5px);
          border-color: #667eea;
          box-shadow: 0 8px 20px rgba(102, 126, 234, 0.15);
        }

        .activity-info {
          flex: 1;
        }

        .activity-info h3 {
          font-size: 1.2rem;
          font-weight: 700;
          color: #2c3e50;
          margin-bottom: 0.5rem;
        }

        .activity-dates {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          font-size: 0.85rem;
          color: #7f8c8d;
          margin-bottom: 0.75rem;
        }

        .activity-dates i {
          font-size: 1rem;
          color: #667eea;
        }

        .activity-description {
          font-size: 0.95rem;
          color: #495057;
          line-height: 1.5;
          margin: 0;
        }

        .btn-solve {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          border: none;
          padding: 0.875rem 1.75rem;
          border-radius: 12px;
          font-weight: 600;
          font-size: 0.95rem;
          cursor: pointer;
          display: flex;
          align-items: center;
          gap: 0.5rem;
          transition: all 0.3s ease;
          white-space: nowrap;
        }

        .btn-solve:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-solve i {
          font-size: 1.2rem;
        }

        /* Achievements Card */
        .achievements-card {
          margin-bottom: 1.5rem;
        }

        .achievements-list {
          display: flex;
          flex-direction: column;
          gap: 1rem;
          margin-bottom: 1.5rem;
        }

        .achievement-mini {
          display: flex;
          align-items: flex-start;
          gap: 0.75rem;
          padding: 0.75rem;
          background: #f8f9fa;
          border-radius: 12px;
          transition: all 0.2s ease;
        }

        .achievement-mini:hover {
          background: #e9ecef;
        }

        .achievement-icon-mini {
          width: 40px;
          height: 40px;
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 1.25rem;
          flex-shrink: 0;
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          color: white;
        }

        .achievement-mini:has(.bi-lock-fill) .achievement-icon-mini {
          background: #95a5a6;
        }

        .achievement-text {
          flex: 1;
          min-width: 0;
        }

        .achievement-title-mini {
          font-weight: 600;
          font-size: 0.9rem;
          color: #2c3e50;
          margin-bottom: 0.25rem;
        }

        .achievement-desc-mini {
          font-size: 0.8rem;
          color: #7f8c8d;
          line-height: 1.3;
        }

        .achievement-badge-mini {
          text-align: right;
          flex-shrink: 0;
        }

        .points-earned {
          display: block;
          font-weight: 700;
          font-size: 0.9rem;
          color: #27ae60;
        }

        .date-earned {
          display: block;
          font-size: 0.7rem;
          color: #95a5a6;
          margin-top: 0.25rem;
        }

        .points-locked {
          font-size: 0.8rem;
          color: #95a5a6;
          font-weight: 500;
        }

        .next-achievement {
          background: linear-gradient(135deg, #fff3cd 0%, #ffe8a1 100%);
          padding: 1rem;
          border-radius: 12px;
          display: flex;
          align-items: center;
          gap: 0.75rem;
          margin-bottom: 1rem;
        }

        .next-achievement i {
          font-size: 1.5rem;
          color: #f39c12;
        }

        .next-achievement strong {
          display: block;
          color: #856404;
          font-size: 0.85rem;
          margin-bottom: 0.25rem;
        }

        .next-achievement div:last-child {
          color: #856404;
          font-size: 0.9rem;
        }

        /* Leaderboard */
        .leaderboard-card {
          height: fit-content;
        }

        .leaderboard-list {
          display: flex;
          flex-direction: column;
          gap: 0.75rem;
          margin-bottom: 1.5rem;
        }

        .leaderboard-item {
          display: flex;
          align-items: center;
          gap: 1rem;
          padding: 1rem;
          background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
          border-radius: 12px;
          transition: all 0.2s ease;
        }

        .leaderboard-item:hover {
          transform: translateX(5px);
          box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        .rank-section {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          flex-shrink: 0;
        }

        .medal {
          font-size: 1.5rem;
        }

        .rank-number {
          font-weight: 700;
          color: #495057;
          font-size: 0.9rem;
        }

        .student-name {
          flex: 1;
          font-weight: 600;
          color: #2c3e50;
          font-size: 0.95rem;
        }

        .student-points {
          font-weight: 700;
          color: #667eea;
          font-size: 1rem;
        }

        /* View All Button */
        .btn-view-all {
          width: 100%;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          border: none;
          padding: 0.875rem;
          border-radius: 12px;
          font-weight: 600;
          font-size: 0.9rem;
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 0.5rem;
          transition: all 0.3s ease;
        }

        .btn-view-all:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
        }

        /* Practice Section */
        .practice-section {
          margin-bottom: 2rem;
        }

        .practice-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
          gap: 1.5rem;
        }

        .practice-card {
          background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
          border-radius: 16px;
          padding: 1.5rem;
          text-align: center;
          transition: all 0.3s ease;
          border: 2px solid transparent;
        }

        .practice-card:hover {
          transform: translateY(-5px);
          border-color: #667eea;
          box-shadow: 0 10px 25px rgba(102, 126, 234, 0.15);
        }

        .practice-icon {
          width: 70px;
          height: 70px;
          border-radius: 16px;
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          color: white;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 2rem;
          margin: 0 auto 1rem;
        }

        .practice-card h3 {
          font-size: 1.1rem;
          font-weight: 700;
          color: #2c3e50;
          margin-bottom: 0.75rem;
        }

        .difficulty-badge {
          display: inline-flex;
          align-items: center;
          gap: 0.375rem;
          background: white;
          padding: 0.5rem 1rem;
          border-radius: 10px;
          font-size: 0.85rem;
          font-weight: 600;
          color: #667eea;
          margin-bottom: 1rem;
        }

        .btn-practice {
          width: 100%;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          border: none;
          padding: 0.75rem;
          border-radius: 12px;
          font-weight: 600;
          font-size: 0.9rem;
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 0.5rem;
          transition: all 0.3s ease;
        }

        .btn-practice:hover {
          transform: translateY(-2px);
          box-shadow: 0 6px 15px rgba(102, 126, 234, 0.4);
        }

        /* Empty States */
        .empty-message {
          text-align: center;
          padding: 3rem 2rem;
          color: #7f8c8d;
        }

        .empty-message.small {
          padding: 2rem 1rem;
        }

        .empty-message i {
          font-size: 3rem;
          color: #bdc3c7;
          margin-bottom: 1rem;
          display: block;
        }

        .empty-message.small i {
          font-size: 2rem;
        }

        .empty-message p {
          font-size: 1rem;
          margin: 0;
        }

        .empty-state {
          background: white;
          border-radius: 20px;
          padding: 4rem 2rem;
          text-align: center;
          box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }

        .empty-state.warning {
          background: linear-gradient(135deg, #fff3cd 0%, #ffe8a1 100%);
        }

        .empty-state i {
          font-size: 4rem;
          color: #f39c12;
          margin-bottom: 1.5rem;
        }

        .empty-state h3 {
          font-size: 1.75rem;
          color: #2c3e50;
          margin-bottom: 0.75rem;
        }

        .empty-state p {
          font-size: 1rem;
          color: #7f8c8d;
          margin: 0;
        }

        /* Loading State */
        .loading-state {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          min-height: 60vh;
        }

        .spinner {
          width: 60px;
          height: 60px;
          border: 5px solid rgba(255, 255, 255, 0.3);
          border-top-color: white;
          border-radius: 50%;
          animation: spin 1s linear infinite;
          margin-bottom: 1.5rem;
        }

        @keyframes spin {
          to { transform: rotate(360deg); }
        }

        .loading-state p {
          font-size: 1.25rem;
          font-weight: 600;
          color: white;
        }

        /* Responsive */
        @media (max-width: 1200px) {
          .content-grid {
            grid-template-columns: 1fr 350px;
          }
        }

        @media (max-width: 992px) {
          .content-grid {
            grid-template-columns: 1fr;
          }

          .sidebar {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1.5rem;
          }
        }

        @media (max-width: 768px) {
          .dashboard-container {
            padding: 1rem;
          }

          .header-text h1 {
            font-size: 1.75rem;
          }

          .stats-grid {
            grid-template-columns: 1fr;
          }

          .sidebar {
            grid-template-columns: 1fr;
          }

          .section-card {
            padding: 1.5rem;
          }

          .activity-item {
            flex-direction: column;
            align-items: stretch;
          }

          .btn-solve {
            width: 100%;
          }

          .practice-grid {
            grid-template-columns: 1fr;
          }
        }

        @media (max-width: 480px) {
          .section-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 1rem;
          }

          .count-badge {
            align-self: flex-start;
          }

          .stat-value {
            font-size: 2rem;
          }

          .stat-icon {
            width: 60px;
            height: 60px;
            font-size: 1.75rem;
          }
        }
      `}</style>
    </div>
  );
}