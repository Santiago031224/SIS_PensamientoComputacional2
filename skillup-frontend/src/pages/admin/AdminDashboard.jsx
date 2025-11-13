import React, { useMemo, useState, useEffect } from 'react';
import { useAdminAnalytics } from '../../hooks/useAdminAnalytics';
import { PeriodsApi } from '../../services/periods';
import '../../styles/adminUser.css';

export default function AdminDashboard() {
  const [periods, setPeriods] = useState([]);
  const [selectedPeriod, setSelectedPeriod] = useState('');
  const [selectedYear, setSelectedYear] = useState('');
  const [loadingPeriods, setLoadingPeriods] = useState(false);

  useEffect(() => {
    let mounted = true;
    async function loadPeriods() {
      setLoadingPeriods(true);
      try {
        const periodsList = await PeriodsApi.list();
        if (mounted) setPeriods(periodsList || []);
      } catch (error) {
        console.error('Failed to fetch periods', error);
        if (mounted) setPeriods([]);
      } finally {
        if (mounted) setLoadingPeriods(false);
      }
    }
    loadPeriods();
    return () => { mounted = false; };
  }, []);

  const { loading, metrics, weeklyActivity, heatmap, alerts } = useAdminAnalytics(selectedPeriod, selectedYear);
  
  const maxWeeklyValue = useMemo(() => Math.max(0, ...weeklyActivity.map(item => item.value)), [weeklyActivity]);
  
  const availableYears = useMemo(() => {
    const years = periods.map(p => p.year).filter(Boolean);
    return [...new Set(years)].sort((a, b) => b - a);
  }, [periods]);

  if (loading) {
    return (
      <div className="container-fluid py-5 text-center">
        <div className="spinner-border text-primary" role="status" aria-label="Cargando resumen administrativo" />
      </div>
    );
  }

  return (
    <div className="container-fluid">
      {/* Header Mejorado */}
      <div className="d-flex flex-wrap justify-content-between align-items-center mb-4 p-4 bg-light rounded-3 shadow-sm">
        <div>
          <h2 className="mb-1 text-gradient">📊 Panel Administrativo</h2>
          <p className="text-muted mb-0">Control de usuarios, roles y vista general de desempeño con datos reales</p>
        </div>
        <div className="d-flex gap-2">
          <div className="input-group" style={{ width: '160px' }}>
            <span className="input-group-text bg-white border-end-0">
              <i className="bi bi-calendar text-muted"></i>
            </span>
            <select 
              className="form-control border-start-0 shadow-sm"
              value={selectedYear}
              onChange={(e) => setSelectedYear(e.target.value)}
              disabled={loadingPeriods}
            >
              <option value="">Todos los años</option>
              {availableYears.map(year => (
                <option key={year} value={year}>{year}</option>
              ))}
            </select>
          </div>
          <div className="input-group" style={{ width: '200px' }}>
            <span className="input-group-text bg-white border-end-0">
              <i className="bi bi-clock text-muted"></i>
            </span>
            <select 
              className="form-control border-start-0 shadow-sm"
              value={selectedPeriod}
              onChange={(e) => setSelectedPeriod(e.target.value)}
              disabled={loadingPeriods}
            >
              <option value="">Todos los períodos</option>
              {periods
                .filter(p => !selectedYear || p.year?.toString() === selectedYear)
                .map(period => (
                  <option key={period.id} value={period.id}>
                    {period.name} {period.year ? `- ${period.year}` : ''}
                  </option>
                ))}
            </select>
          </div>
        </div>
      </div>

      {/* Métricas */}
      <div className="row g-4 mb-4">
        {(metrics.length ? metrics : [{ id: 'empty', label: 'Sin datos', value: 0, change: 'Aún no hay registros' }]).map(metric => (
          <div key={metric.id} className="col-md-3">
            <div className="card metric-card shadow-sm h-100 border-0">
              <div className="card-body p-4">
                <div className="text-muted small text-uppercase fw-semibold mb-2">{metric.label}</div>
                <div className="display-6 fw-bold text-gradient mb-2">{metric.value}</div>
                <div className="text-muted small d-flex align-items-center">
                  <i className="bi bi-arrow-up-right text-success me-1"></i>
                  {metric.change}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="row g-4">
        {/* Gráfico de Actividad Semanal */}
        <section className="col-lg-8">
          <div className="card h-100 border-0 shadow-sm">
            <div className="card-header bg-white border-0 py-3">
              <h5 className="card-title mb-0 d-flex align-items-center">
                <i className="bi bi-bar-chart-line text-primary me-2"></i>
                Actividad Semanal
              </h5>
            </div>
            <div className="card-body">
              {weeklyActivity.length === 0 ? (
                <div className="text-center py-5">
                  <i className="bi bi-graph-up display-4 text-muted d-block mb-2"></i>
                  <p className="text-muted mb-0">No se registran actividades en las últimas semanas.</p>
                </div>
              ) : (
                <div className="chart-container bg-light rounded-3" style={{ height: 300, position: 'relative' }}>
                  <div className="d-flex align-items-end justify-content-around h-100 p-4">
                    {weeklyActivity.map(item => {
                      const ratio = maxWeeklyValue ? item.value / maxWeeklyValue : 0;
                      const barHeightPx = Math.max(ratio * 200, 40); // Altura en píxeles, mínimo 40px
                      return (
                        <div key={item.key} className="text-center" style={{ width: 60, position: 'relative' }}>
                          <div
                            className="chart-bar rounded-top mx-auto"
                            style={{ 
                              height: `${barHeightPx}px`,
                              width: '40px',
                              backgroundColor: '#0d6efd',
                              border: '2px solid #0a58ca',
                              transition: 'height 0.3s ease',
                              cursor: 'pointer'
                            }}
                            title={`${item.value} actividades`}
                          >
                            <div className="position-absolute start-50 translate-middle-x fw-bold" style={{ top: '-25px', color: '#000', fontSize: '16px' }}>
                              {item.value}
                            </div>
                          </div>
                          <div className="small text-muted mt-2 fw-semibold" style={{ wordBreak: 'break-word', fontSize: '9px', lineHeight: '1.2' }}>
                            {item.key}
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              )}
            </div>
          </div>
        </section>

        {/* Promedios por Grupo */}
        <section className="col-lg-4">
          <div className="card h-100 border-0 shadow-sm">
            <div className="card-header bg-white border-0 py-3">
              <h5 className="card-title mb-0 d-flex align-items-center">
                <i className="bi bi-speedometer2 text-success me-2"></i>
                Promedio por Grupo
              </h5>
            </div>
            <div className="card-body">
              {heatmap.length === 0 ? (
                <div className="text-center py-4">
                  <i className="bi bi-clipboard-data display-4 text-muted d-block mb-2"></i>
                  <p className="text-muted mb-0">No hay calificaciones disponibles para calcular promedios.</p>
                </div>
              ) : (
                <div className="list-group list-group-flush">
                  {heatmap.map(row => (
                    <div key={row.label} className="list-group-item px-0 py-3 border-0">
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <span className="fw-semibold">{row.label}</span>
                        <span className="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25">
                          {row.average.toFixed(2)}
                        </span>
                      </div>
                      <div className="progress" style={{ height: 8, borderRadius: 10 }}>
                        <div 
                          className="progress-bar bg-success" 
                          role="progressbar" 
                          style={{ 
                            width: `${Math.min(row.value, 100)}%`,
                            borderRadius: 10
                          }}
                        />
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </section>
      </div>

      {/* Alertas */}
      <section className="mt-4">
        <div className="card border-0 shadow-sm">
          <div className="card-header bg-white border-0 py-3">
            <h5 className="card-title mb-0 d-flex align-items-center">
              <i className="bi bi-bell text-warning me-2"></i>
              Alertas del Sistema
            </h5>
          </div>
          <div className="card-body">
            {alerts.length === 0 ? (
              <div className="text-center py-3">
                <i className="bi bi-check-circle display-4 text-success d-block mb-2"></i>
                <p className="text-muted mb-0">No hay alertas pendientes. ¡Todo está bajo control!</p>
              </div>
            ) : (
              <div className="list-group list-group-flush">
                {alerts.map((alert, index) => (
                  <div key={index} className="list-group-item px-0 py-3 border-0 alert-item">
                    <div className="d-flex align-items-center">
                      <div className="alert-icon bg-warning bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3">
                        <i className="bi bi-exclamation-triangle text-warning"></i>
                      </div>
                      <div className="flex-grow-1">
                        <span className="fw-medium">{alert}</span>
                      </div>
                      <button className="btn btn-outline-secondary btn-sm">
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