import React from 'react';
import { useAdminAnalytics } from '../../hooks/useAdminAnalytics';
import '../../styles/reportsadmin.css';

export default function AdminReports() {
  const { loading, weeklyActivity, heatmap, submissionsSummary, globalAverage, platformUsage, alerts } = useAdminAnalytics();

  if (loading) {
    return (
      <div className="container-fluid py-5 text-center">
        <div className="spinner-border text-primary" role="status" aria-label="Cargando reportes administrativos" />
      </div>
    );
  }

  const completionRate = submissionsSummary.total
    ? Math.round(((submissionsSummary.total - submissionsSummary.pending) / submissionsSummary.total) * 100)
    : 0;

  return (
    <div className="container-fluid">
      {/* Header Mejorado */}
      <div className="d-flex flex-wrap justify-content-between align-items-center mb-4 p-4 bg-light rounded-3 shadow-sm">
        <div>
          <h2 className="mb-1 text-gradient">📈 Diagramas y Reportes</h2>
          <p className="text-muted mb-0">Resumen generado a partir de la API del sistema</p>
        </div>
        <div className="d-flex gap-2">
          <div className="input-group search-box" style={{ maxWidth: 300 }}>
            <span className="input-group-text bg-white border-end-0">
              <i className="bi bi-calendar-range text-muted"></i>
            </span>
            <input
              type="text"
              className="form-control border-start-0 shadow-sm"
              placeholder="Filtrar por período..."
            />
          </div>
        </div>
      </div>

      <div className="row g-4">
        {/* Participación por Semana */}
        <div className="col-lg-4">
          <div className="card h-100 border-0 shadow-sm report-card">
            <div className="card-header bg-white border-0 py-3">
              <h5 className="card-title mb-0 d-flex align-items-center">
                <i className="bi bi-calendar-week text-primary me-2"></i>
                Participación por Semana
              </h5>
            </div>
            <div className="card-body">
              {weeklyActivity.length === 0 ? (
                <div className="text-center py-4">
                  <i className="bi bi-calendar-x display-4 text-muted d-block mb-2"></i>
                  <p className="text-muted mb-0">No hay actividades registradas en las últimas semanas.</p>
                </div>
              ) : (
                <div className="table-responsive">
                  <table className="table table-hover table-sm mb-0">
                    <thead className="table-light">
                      <tr>
                        <th className="border-0">Semana</th>
                        <th className="border-0 text-end">Actividades</th>
                      </tr>
                    </thead>
                    <tbody>
                      {weeklyActivity.map((item, index) => (
                        <tr key={item.key} className="activity-row">
                          <td className="fw-semibold">{item.key}</td>
                          <td className="text-end">
                            <span className="badge bg-primary bg-opacity-10 text-primary border border-primary border-opacity-25">
                              {item.value}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Tasa de Finalización */}
        <div className="col-lg-4">
          <div className="card h-100 border-0 shadow-sm report-card">
            <div className="card-header bg-white border-0 py-3">
              <h5 className="card-title mb-0 d-flex align-items-center">
                <i className="bi bi-check-circle text-success me-2"></i>
                Tasa de Finalización
              </h5>
            </div>
            <div className="card-body text-center">
              <div className="completion-chart mb-4">
                <div className="position-relative d-inline-block">
                  <div className="completion-circle">
                    <span className="completion-value display-4 fw-bold text-gradient">{completionRate}%</span>
                  </div>
                  <svg width="120" height="120" viewBox="0 0 120 120" className="completion-svg">
                    <circle cx="60" cy="60" r="54" fill="none" stroke="#e9ecef" strokeWidth="8" />
                    <circle 
                      cx="60" cy="60" r="54" fill="none" 
                      stroke="url(#completionGradient)" 
                      strokeWidth="8" 
                      strokeLinecap="round"
                      strokeDasharray={`${completionRate * 3.39} 339`}
                      transform="rotate(-90 60 60)"
                    />
                    <defs>
                      <linearGradient id="completionGradient" x1="0%" y1="0%" x2="100%" y2="0%">
                        <stop offset="0%" stopColor="#28a745" />
                        <stop offset="100%" stopColor="#20c997" />
                      </linearGradient>
                    </defs>
                  </svg>
                </div>
              </div>
              <div className="completion-stats">
                <div className="row text-center">
                  <div className="col-6">
                    <div className="stat-value text-success fw-bold">{submissionsSummary.total}</div>
                    <div className="stat-label text-muted small">Total</div>
                  </div>
                  <div className="col-6">
                    <div className="stat-value text-warning fw-bold">{submissionsSummary.pending}</div>
                    <div className="stat-label text-muted small">Pendientes</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Uso de la Plataforma */}
        <div className="col-lg-4">
          <div className="card h-100 border-0 shadow-sm report-card">
            <div className="card-header bg-white border-0 py-3">
              <h5 className="card-title mb-0 d-flex align-items-center">
                <i className="bi bi-people-fill text-info me-2"></i>
                Uso de la Plataforma
              </h5>
            </div>
            <div className="card-body">
              <div className="text-center mb-4">
                <div className="position-relative d-inline-block mb-3">
                  <div className="completion-circle">
                    <span className="completion-value display-4 fw-bold text-gradient">{platformUsage.percentage}%</span>
                  </div>
                  <svg width="120" height="120" viewBox="0 0 120 120" className="completion-svg">
                    <circle cx="60" cy="60" r="54" fill="none" stroke="#e9ecef" strokeWidth="8" />
                    <circle 
                      cx="60" cy="60" r="54" fill="none" 
                      stroke="url(#usageGradient)" 
                      strokeWidth="8" 
                      strokeLinecap="round"
                      strokeDasharray={`${platformUsage.percentage * 3.39} 339`}
                      transform="rotate(-90 60 60)"
                    />
                    <defs>
                      <linearGradient id="usageGradient" x1="0%" y1="0%" x2="100%" y2="0%">
                        <stop offset="0%" stopColor="#0dcaf0" />
                        <stop offset="100%" stopColor="#0d6efd" />
                      </linearGradient>
                    </defs>
                  </svg>
                </div>
                <p className="text-muted small mb-0">
                  <span className="fw-semibold text-info">{platformUsage.activeUsers}</span> de{' '}
                  <span className="fw-semibold">{platformUsage.totalUsers}</span> usuarios activos
                </p>
              </div>
              
              <div className="usage-stats border-top pt-3">
                <div className="row text-center">
                  <div className="col-6 border-end">
                    <div className="stat-value text-success fw-bold fs-4">{platformUsage.activeUsers}</div>
                    <div className="stat-label text-muted small">Activos</div>
                  </div>
                  <div className="col-6">
                    <div className="stat-value text-muted fw-bold fs-4">{platformUsage.totalUsers - platformUsage.activeUsers}</div>
                    <div className="stat-label text-muted small">Inactivos</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Alertas y Próximos Pasos */}
      <section className="mt-4">
        <div className="card border-0 shadow-sm">
          <div className="card-header bg-white border-0 py-3">
            <h5 className="card-title mb-0 d-flex align-items-center">
              <i className="bi bi-bell-fill text-warning me-2"></i>
              Alertas y Próximos Pasos
            </h5>
          </div>
          <div className="card-body">
            {alerts.length === 0 ? (
              <div className="text-center py-4">
                <i className="bi bi-check-circle display-4 text-success d-block mb-2"></i>
                <p className="text-muted mb-0">No hay alertas activas. Continúa monitoreando para detectar eventualidades.</p>
              </div>
            ) : (
              <div className="list-group list-group-flush">
                {alerts.map((alert, index) => (
                  <div key={index} className="list-group-item px-0 py-3 border-0 alert-item">
                    <div className="d-flex align-items-start">
                      <div className="alert-icon bg-warning bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3 flex-shrink-0">
                        <i className="bi bi-exclamation-triangle text-warning"></i>
                      </div>
                      <div className="flex-grow-1">
                        <p className="mb-1 fw-medium">{alert}</p>
                        <small className="text-muted">Revisar y tomar acción</small>
                      </div>
                      <button className="btn btn-outline-primary btn-sm flex-shrink-0">
                        <i className="bi bi-arrow-right"></i>
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </section>

    </div>
  );
}