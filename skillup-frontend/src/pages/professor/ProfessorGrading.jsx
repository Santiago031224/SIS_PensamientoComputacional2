import React, { useEffect, useMemo, useState } from 'react';
import { useAuth } from '../../services/AuthContext';
import { useProfessorAnalytics } from '../../hooks/useProfessorAnalytics';
import { ScoresApi } from '../../services/scores';
import '../../styles/professor.css';

function formatDateTime(value) {
  if (!value) return 'Sin fecha';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleString();
}

export default function ProfessorGrading() {
  const { user } = useAuth();
  const { loading, professor, gradingQueue } = useProfessorAnalytics(user);
  const [drafts, setDrafts] = useState([]);

  useEffect(() => {
    if (!loading) {
      setDrafts(
        gradingQueue.map(item => ({
          ...item,
          scoreDraft: item.score ?? '',
          feedbackDraft: '',
        }))
      );
    }
  }, [loading, gradingQueue]);

  const hasData = useMemo(() => drafts.length > 0, [drafts]);

  function updateDraft(id, updater) {
    setDrafts(current => current.map(item => (item.id === id ? { ...item, ...updater(item) } : item)));
  }

  function handleScoreChange(id, value) {
    updateDraft(id, () => ({ scoreDraft: value === '' ? '' : Number(value) }));
  }

  function handleFeedbackChange(id, value) {
    updateDraft(id, () => ({ feedbackDraft: value }));
  }

  async function handleSave(submissionId) {
    const draft = drafts.find(d => d.id === submissionId);
    if (!draft) return;
    if (draft.scoreDraft === '' || typeof draft.scoreDraft !== 'number') {
      alert('Ingresa una calificacion valida');
      return;
    }
    try {
      await ScoresApi.create({ pointsAwarded: draft.scoreDraft, submissionId });
      updateDraft(submissionId, () => ({ score: draft.scoreDraft }));
    } catch (e) {
      console.error('Error guardando calificacion', e);
      alert('No se pudo guardar la calificacion');
    }
  }

  if (loading) {
    return (
      <div className="container-fluid py-5 text-center">
        <div className="spinner-border text-primary" role="status" aria-label="Cargando envios de estudiantes" />
      </div>
    );
  }

  if (!professor) {
    return (
      <div className="container-fluid py-5">
        <div className="alert alert-warning" role="alert">
          No encontramos informacion del profesor asociada al usuario actual. Verifica tu cuenta o contacta al administrador.
        </div>
      </div>
    );
  }

  return (
    <div className="container-fluid">
      <div className="mb-4 p-4 bg-light rounded-3 shadow-sm">
        <h2 className="mb-1 text-gradient">✏️ Calificaciones en curso</h2>
        <p className="text-muted mb-0">Revisa envíos recientes desde el backend, asigna nota y registra retroalimentación.</p>
      </div>

      <div className="card border-0 shadow-sm">
        <div className="card-body">
          <div className="table-responsive">
            <table className="table align-middle">
              <thead>
                <tr>
                  <th scope="col">Estudiante</th>
                  <th scope="col">Actividad</th>
                  <th scope="col">Fecha de envío</th>
                  <th scope="col">Estado</th>
                  <th scope="col">Calificación</th>
                  <th scope="col">Retroalimentación</th>
                  <th scope="col" className="text-end">Acciones</th>
                </tr>
              </thead>
              <tbody>
                {!hasData ? (
                  <tr>
                    <td colSpan={7} className="text-center text-muted">No hay envios pendientes de calificar.</td>
                  </tr>
                ) : (
                  drafts.map(item => (
                    <tr key={item.id}>
                      <td>{item.student}</td>
                      <td>
                        <div className="fw-semibold">{item.activity || 'Actividad sin titulo'}</div>
                        <div className="small text-muted">{item.exercise}</div>
                      </td>
                      <td>{formatDateTime(item.submittedAt)}</td>
                      <td>
                        <span className="badge bg-light text-dark border">{item.status || 'SIN ESTADO'}</span>
                      </td>
                      <td style={{ width: 120 }}>
                        <input
                          type="number"
                          step="0.1"
                          min="0"
                          max="5"
                          className="form-control form-control-sm"
                          value={item.scoreDraft}
                          onChange={event => handleScoreChange(item.id, event.target.value)}
                        />
                      </td>
                      <td style={{ width: 320 }}>
                        <textarea
                          className="form-control form-control-sm"
                          rows={2}
                          value={item.feedbackDraft}
                          onChange={event => handleFeedbackChange(item.id, event.target.value)}
                        />
                      </td>
                      <td className="text-end">
                        <button
                          type="button"
                          className="btn btn-outline-primary btn-sm"
                          disabled={drafts.find(d => d.id === item.id)?.scoreDraft === ''}
                          onClick={() => handleSave(item.id)}
                        >
                          Guardar
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

      <div className="alert alert-secondary mt-4 mb-0">
        Esta vista es un prototipo. Conecta el evento "Guardar" con el endpoint de calificaciones cuando este disponible.
      </div>
    </div>
  );
}
