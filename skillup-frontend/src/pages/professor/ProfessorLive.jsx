import React, { useEffect, useState } from 'react';
import { scoreboardRealtime } from '../../services/realtimeScoreboard';
import { useProfessorAnalytics } from '../../hooks/useProfessorAnalytics';
import { useAuth } from '../../services/AuthContext';
import '../../styles/professor.css';

export default function ProfessorLive() {
  const { user } = useAuth();
  const { professor } = useProfessorAnalytics(user);
  const [events, setEvents] = useState([]);

  useEffect(() => {
    const unsubscribe = scoreboardRealtime.subscribe(evt => {
      if (evt?.type === 'EXERCISE_SOLVED') {
        setEvents(prev => [evt, ...prev.slice(0, 49)]); // mantener 50 ultimos
      }
    });
    return unsubscribe;
  }, []);

  return (
    <div className="container-fluid">
      <div className="d-flex flex-wrap justify-content-between align-items-center mb-4 p-4 bg-light rounded-3 shadow-sm">
        <div>
          <h2 className="mb-1 text-gradient">🎮 Scoreboard en tiempo real</h2>
          <p className="text-muted mb-0">Observa resoluciones de ejercicios conforme suceden durante la actividad activa.</p>
        </div>
        <div>
          <button type="button" className="btn btn-outline-secondary btn-sm" onClick={() => scoreboardRealtime.emitTestSolved({ studentName: 'Prueba Local', exerciseId: 99, activityTitle: 'Actividad Demo' })}>
            <i className="bi bi-play-circle me-2"></i>Evento de prueba
          </button>
        </div>
      </div>

      <div className="card border-0 shadow-sm">
        <div className="card-body">
          <h5 className="card-title d-flex align-items-center">
            <i className="bi bi-lightning-charge text-warning me-2"></i>
            Últimos eventos
          </h5>
          <ul className="list-group list-group-flush">
            {events.length === 0 && <li className="list-group-item text-muted">No hay eventos aún</li>}
            {events.map((e, idx) => (
              <li key={idx} className="list-group-item d-flex justify-content-between align-items-start">
                <div>
                  <div className="fw-semibold">{e.studentName || 'Estudiante'}</div>
                  <div className="small text-muted">Ejercicio {e.exerciseId} · {e.activityTitle || 'Actividad'} </div>
                </div>
                <div className="small text-muted">{new Date(e.timestamp || Date.now()).toLocaleTimeString()}</div>
              </li>
            ))}
          </ul>
        </div>
      </div>

      {!professor && (
        <div className="alert alert-warning mt-4">No se detectó perfil de profesor, algunas funciones pueden estar limitadas.</div>
      )}
    </div>
  );
}
