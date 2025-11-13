import React, { useMemo } from 'react';
import { useAuth } from '../../services/AuthContext.jsx';
import { useStudentAnalytics } from '../../hooks/useStudentAnalytics.js';

export default function StudentLeaderboard() {
  const { user } = useAuth();
  const analytics = useStudentAnalytics(user);

  const leaderboard = useMemo(() => analytics.leaderboard || [], [analytics.leaderboard]);
  const top = leaderboard.slice(0, 3);
  const rest = leaderboard.slice(3);
  
  // Encontrar la posición del usuario actual
  const userRank = useMemo(() => {
    if (!analytics.student?.id) return null;
    const entry = leaderboard.find(e => e.studentId === analytics.student.id);
    return entry?.rank || null;
  }, [leaderboard, analytics.student]);

  if (analytics.loading) {
    return (
      <div className="leaderboard-container">
        <div className="loading-state">
          <div className="spinner"></div>
          <p>Calculando podio...</p>
        </div>
      </div>
    );
  }

  if (!analytics.student) {
    return (
      <div className="leaderboard-container">
        <div className="empty-state warning">
          <i className="bi bi-exclamation-triangle-fill"></i>
          <h3>Cuenta no asociada</h3>
          <p>Asocia tu cuenta a un estudiante para ver la clasificación.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="leaderboard-container">
      <link
        href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css"
        rel="stylesheet"
      />
      
      {/* Header */}
      <header className="leaderboard-header">
        <div className="header-content">
          <h1>
            <i className="bi bi-trophy-fill"></i>
            Podio y Clasificación
          </h1>
          <p>Revisa tu posición frente al resto del grupo</p>
        </div>
        
        {userRank && (
          <div className="user-rank-card">
            <div className="rank-icon">
              <i className="bi bi-person-fill"></i>
            </div>
            <div className="rank-info">
              <span className="rank-label">Tu posición</span>
              <span className="rank-value">#{userRank}</span>
            </div>
          </div>
        )}
      </header>

      {/* Podium - Top 3 */}
      <section className="podium-section">
        {top.length === 0 ? (
          <div className="empty-message">
            <i className="bi bi-hourglass-split"></i>
            <h3>Aún no hay puntajes registrados</h3>
            <p>Sé el primero en completar actividades y ganar puntos</p>
          </div>
        ) : (
          <div className="podium-container">
            {/* 2nd Place */}
            {top[1] && (
              <div className="podium-card second">
                <div className="podium-rank">
                  <span className="medal">🥈</span>
                  <span className="position">#2</span>
                </div>
                <div className="podium-avatar second-avatar">
                  {top[1].name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)}
                </div>
                <h3 className="podium-name">{top[1].name}</h3>
                <div className="podium-stats">
                  <div className="stat">
                    <i className="bi bi-star-fill"></i>
                    <span>{top[1].points.toFixed(1)} pts</span>
                  </div>
                  {top[1].group && (
                    <div className="stat-group">
                      <i className="bi bi-people-fill"></i>
                      <span>{top[1].group}</span>
                    </div>
                  )}
                </div>
                <div className="podium-base second-base"></div>
              </div>
            )}

            {/* 1st Place */}
            {top[0] && (
              <div className="podium-card first">
                <div className="crown">
                  <i className="bi bi-award-fill"></i>
                </div>
                <div className="podium-rank">
                  <span className="medal">🥇</span>
                  <span className="position">#1</span>
                </div>
                <div className="podium-avatar first-avatar">
                  {top[0].name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)}
                </div>
                <h3 className="podium-name">{top[0].name}</h3>
                <div className="podium-stats">
                  <div className="stat">
                    <i className="bi bi-star-fill"></i>
                    <span>{top[0].points.toFixed(1)} pts</span>
                  </div>
                  {top[0].group && (
                    <div className="stat-group">
                      <i className="bi bi-people-fill"></i>
                      <span>{top[0].group}</span>
                    </div>
                  )}
                </div>
                <div className="podium-base first-base"></div>
              </div>
            )}

            {/* 3rd Place */}
            {top[2] && (
              <div className="podium-card third">
                <div className="podium-rank">
                  <span className="medal">🥉</span>
                  <span className="position">#3</span>
                </div>
                <div className="podium-avatar third-avatar">
                  {top[2].name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)}
                </div>
                <h3 className="podium-name">{top[2].name}</h3>
                <div className="podium-stats">
                  <div className="stat">
                    <i className="bi bi-star-fill"></i>
                    <span>{top[2].points.toFixed(1)} pts</span>
                  </div>
                  {top[2].group && (
                    <div className="stat-group">
                      <i className="bi bi-people-fill"></i>
                      <span>{top[2].group}</span>
                    </div>
                  )}
                </div>
                <div className="podium-base third-base"></div>
              </div>
            )}
          </div>
        )}
      </section>

      {/* Full Rankings Table */}
      {leaderboard.length > 0 && (
        <section className="rankings-section">
          <div className="section-card">
            <div className="section-header">
              <h2>
                <i className="bi bi-list-ol"></i>
                Clasificación Completa
              </h2>
              <span className="total-count">{leaderboard.length} estudiantes</span>
            </div>

            <div className="rankings-list">
              {leaderboard.map((entry, index) => {
                const isUser = analytics.student?.id === entry.studentId;
                const isTopThree = index < 3;
                const medals = ['🥇', '🥈', '🥉'];
                
                return (
                  <div 
                    key={entry.studentId} 
                    className={`ranking-item ${isUser ? 'current-user' : ''} ${isTopThree ? 'top-three' : ''}`}
                  >
                    <div className="rank-section">
                      {isTopThree ? (
                        <span className="medal-icon">{medals[index]}</span>
                      ) : (
                        <span className="rank-number">#{entry.rank}</span>
                      )}
                    </div>
                    
                    <div className="student-avatar-small">
                      {entry.name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)}
                    </div>
                    
                    <div className="student-details">
                      <div className="student-name">
                        {entry.name}
                        {isUser && <span className="you-badge">Tú</span>}
                      </div>
                      {entry.group && (
                        <div className="student-group">
                          <i className="bi bi-people-fill"></i>
                          {entry.group}
                        </div>
                      )}
                    </div>
                    
                    <div className="student-points">
                      <i className="bi bi-star-fill"></i>
                      <span>{entry.points.toFixed(1)}</span>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </section>
      )}

      <style jsx>{`
        * {
          box-sizing: border-box;
          margin: 0;
          padding: 0;
        }

        .leaderboard-container {
          padding: 2rem;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          min-height: 100vh;
          font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', sans-serif;
        }

        /* Header */
        .leaderboard-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 2rem;
          gap: 2rem;
          flex-wrap: wrap;
        }

        .header-content h1 {
          font-size: 2.5rem;
          font-weight: 800;
          color: #000000;
          margin-bottom: 0.5rem;
          display: flex;
          align-items: center;
          gap: 0.75rem;
          text-shadow: 0 2px 10px rgba(255, 255, 255, 0.3);
        }

        .header-content h1 i {
          font-size: 2.75rem;
          color: #ffd700;
        }

        .header-content p {
          color: rgba(255, 255, 255, 0.9);
          font-size: 1rem;
        }

        .user-rank-card {
          background: white;
          border-radius: 16px;
          padding: 1.25rem 1.75rem;
          display: flex;
          align-items: center;
          gap: 1rem;
          box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }

        .rank-icon {
          width: 50px;
          height: 50px;
          border-radius: 12px;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 1.5rem;
        }

        .rank-info {
          display: flex;
          flex-direction: column;
        }

        .rank-label {
          font-size: 0.85rem;
          color: #7f8c8d;
          font-weight: 500;
        }

        .rank-value {
          font-size: 1.75rem;
          font-weight: 800;
          color: #2c3e50;
          line-height: 1;
        }

        /* Podium Section */
        .podium-section {
          margin-bottom: 2rem;
        }

        .podium-container {
          display: flex;
          justify-content: center;
          align-items: flex-end;
          gap: 1.5rem;
          padding: 2rem 0;
        }

        .podium-card {
          background: white;
          border-radius: 24px;
          padding: 2rem 1.5rem;
          text-align: center;
          position: relative;
          transition: all 0.3s ease;
          box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }

        .podium-card:hover {
          transform: translateY(-10px);
          box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
        }

        .podium-card.first {
          width: 300px;
          order: 2;
        }

        .podium-card.second {
          width: 280px;
          order: 1;
          margin-top: 3rem;
        }

        .podium-card.third {
          width: 280px;
          order: 3;
          margin-top: 4rem;
        }

        .crown {
          position: absolute;
          top: -30px;
          left: 50%;
          transform: translateX(-50%);
          font-size: 3rem;
          color: #ffd700;
          animation: float 2s ease-in-out infinite;
        }

        @keyframes float {
          0%, 100% { transform: translateX(-50%) translateY(0); }
          50% { transform: translateX(-50%) translateY(-10px); }
        }

        .podium-rank {
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 0.5rem;
          margin-bottom: 1rem;
        }

        .medal {
          font-size: 2.5rem;
        }

        .position {
          font-size: 1.25rem;
          font-weight: 700;
          color: #2c3e50;
        }

        .podium-avatar {
          width: 100px;
          height: 100px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 2rem;
          font-weight: 700;
          color: white;
          margin: 0 auto 1rem;
          box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
        }

        .first-avatar {
          background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
          width: 120px;
          height: 120px;
          font-size: 2.5rem;
        }

        .second-avatar {
          background: linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%);
        }

        .third-avatar {
          background: linear-gradient(135deg, #cd7f32 0%, #e9a76d 100%);
        }

        .podium-name {
          font-size: 1.25rem;
          font-weight: 700;
          color: #2c3e50;
          margin-bottom: 1rem;
          line-height: 1.3;
        }

        .podium-card.first .podium-name {
          font-size: 1.4rem;
        }

        .podium-stats {
          display: flex;
          flex-direction: column;
          gap: 0.5rem;
          margin-bottom: 1.5rem;
        }

        .stat {
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 0.5rem;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          padding: 0.75rem 1rem;
          border-radius: 12px;
          font-weight: 700;
          font-size: 1.1rem;
        }

        .stat-group {
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 0.5rem;
          color: #7f8c8d;
          font-size: 0.9rem;
        }

        .podium-base {
          height: 60px;
          margin: 0 -1.5rem -2rem;
          border-radius: 0 0 24px 24px;
        }

        .first-base {
          background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
          height: 80px;
        }

        .second-base {
          background: linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%);
        }

        .third-base {
          background: linear-gradient(135deg, #cd7f32 0%, #e9a76d 100%);
        }

        /* Rankings Section */
        .rankings-section {
          margin-bottom: 2rem;
        }

        .section-card {
          background: white;
          border-radius: 24px;
          padding: 2rem;
          box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }

        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 2rem;
          padding-bottom: 1.5rem;
          border-bottom: 2px solid #f0f0f0;
        }

        .section-header h2 {
          font-size: 1.75rem;
          font-weight: 700;
          color: #2c3e50;
          display: flex;
          align-items: center;
          gap: 0.75rem;
          margin: 0;
        }

        .section-header i {
          font-size: 2rem;
          color: #667eea;
        }

        .total-count {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          padding: 0.5rem 1.25rem;
          border-radius: 12px;
          font-weight: 700;
          font-size: 0.95rem;
        }

        .rankings-list {
          display: flex;
          flex-direction: column;
          gap: 0.75rem;
        }

        .ranking-item {
          display: flex;
          align-items: center;
          gap: 1rem;
          padding: 1.25rem;
          background: #f8f9fa;
          border-radius: 16px;
          transition: all 0.3s ease;
          border: 2px solid transparent;
        }

        .ranking-item:hover {
          transform: translateX(8px);
          box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
          background: #fff;
        }

        .ranking-item.current-user {
          background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
          border-color: #4caf50;
        }

        .ranking-item.top-three {
          background: linear-gradient(135deg, #fff9e6 0%, #fff3cc 100%);
        }

        .ranking-item.top-three.current-user {
          background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
        }

        .rank-section {
          width: 60px;
          display: flex;
          align-items: center;
          justify-content: center;
          flex-shrink: 0;
        }

        .medal-icon {
          font-size: 2rem;
        }

        .rank-number {
          font-size: 1.25rem;
          font-weight: 700;
          color: #495057;
        }

        .student-avatar-small {
          width: 50px;
          height: 50px;
          border-radius: 50%;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: 700;
          font-size: 1rem;
          flex-shrink: 0;
        }

        .student-details {
          flex: 1;
          min-width: 0;
        }

        .student-name {
          font-size: 1.1rem;
          font-weight: 700;
          color: #2c3e50;
          margin-bottom: 0.25rem;
          display: flex;
          align-items: center;
          gap: 0.5rem;
        }

        .you-badge {
          background: #4caf50;
          color: white;
          padding: 0.25rem 0.75rem;
          border-radius: 8px;
          font-size: 0.75rem;
          font-weight: 700;
        }

        .student-group {
          font-size: 0.85rem;
          color: #7f8c8d;
          display: flex;
          align-items: center;
          gap: 0.375rem;
        }

        .student-points {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          padding: 0.75rem 1.25rem;
          border-radius: 12px;
          font-weight: 700;
          font-size: 1.1rem;
          flex-shrink: 0;
        }

        .student-points i {
          font-size: 1rem;
        }

        /* Empty States */
        .empty-message {
          text-align: center;
          padding: 4rem 2rem;
          background: white;
          border-radius: 24px;
          box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }

        .empty-message i {
          font-size: 4rem;
          color: #bdc3c7;
          margin-bottom: 1.5rem;
        }

        .empty-message h3 {
          font-size: 1.75rem;
          color: #2c3e50;
          margin-bottom: 0.75rem;
        }

        .empty-message p {
          font-size: 1rem;
          color: #7f8c8d;
          margin: 0;
        }

        .empty-state {
          background: white;
          border-radius: 24px;
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
        @media (max-width: 992px) {
          .podium-container {
            flex-direction: column;
            align-items: center;
          }

          .podium-card {
            width: 100% !important;
            max-width: 350px;
            order: initial !important;
            margin-top: 0 !important;
          }

          .podium-card.second,
          .podium-card.third {
            margin-top: 0 !important;
          }
        }

        @media (max-width: 768px) {
          .leaderboard-container {
            padding: 1rem;
          }

          .leaderboard-header {
            flex-direction: column;
            align-items: stretch;
          }

          .header-content h1 {
            font-size: 1.75rem;
          }

          .user-rank-card {
            width: 100%;
          }

          .section-card {
            padding: 1.5rem;
          }

          .section-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 1rem;
          }

          .ranking-item {
            flex-wrap: wrap;
          }

          .student-details {
            flex-basis: 100%;
            order: 3;
            margin-top: 0.5rem;
          }

          .student-points {
            margin-left: auto;
          }
        }

        @media (max-width: 480px) {
          .podium-avatar {
            width: 80px;
            height: 80px;
            font-size: 1.5rem;
          }

          .first-avatar {
            width: 90px;
            height: 90px;
            font-size: 1.75rem;
          }

          .podium-name {
            font-size: 1.1rem;
          }

          .ranking-item {
            padding: 1rem;
          }

          .rank-section {
            width: 50px;
          }

          .student-avatar-small {
            width: 40px;
            height: 40px;
            font-size: 0.9rem;
          }

          .student-name {
            font-size: 1rem;
          }

          .student-points {
            padding: 0.5rem 1rem;
            font-size: 1rem;
          }
        }
      `}</style>
    </div>
  );
}