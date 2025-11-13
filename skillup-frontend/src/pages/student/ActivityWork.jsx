import React, { useEffect, useMemo, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
// Using document-based submission service; create fallback create if not present
import { SubmissionDocuments } from '../../services/submissionDocuments.js';
import { StudentsApi } from '../../services/students.js';
import { ActivityDocuments } from '../../services/activityDocuments.js';
import { slugify } from '../../services/stringUtils.js';
import { useAuth } from '../../services/AuthContext.jsx';

export default function ActivityWork(){
  const { id } = useParams();
  const navigate = useNavigate();
  const [activity, setActivity] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedExercise, setSelectedExercise] = useState(null);
  const [code, setCode] = useState('// Escribe tu solución aquí');
  const [link, setLink] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const { user } = useAuth();

  useEffect(() => {
    let mounted = true;
    async function load(){
      setLoading(true);
      try{
        // Attempt to interpret route `id` as a numeric id first.
        const parsed = Number(id);
        if (id && !Number.isNaN(parsed) && Number.isFinite(parsed)) {
          try {
            const doc = await ActivityDocuments.getById(parsed);
            if (mounted) {
              setActivity(doc);
              // Auto-select first exercise if available
              if (doc?.exercises?.length > 0) {
                setSelectedExercise(doc.exercises[0]);
              }
            }
            return;
          } catch (err) {
            // If backend returns 400/404 for numeric id, fall through to relational lookup
            console.warn('ActivityWork - failed to load by numeric id, will try relational lookup:', parsed, err);
          }
        }

        // If id is not numeric or numeric lookup failed, treat id as relationalId (string) and try to find matching activity.
        if (id) {
          try {
            const all = await ActivityDocuments.list();
            const decodedId = decodeURIComponent(String(id));
            const found = (all || []).find(a =>
              String(a?.relationalId) === decodedId ||
              String(a?.id) === decodedId ||
              (a?.title && slugify(a.title) === decodedId)
            );
            if (found) {
              if (mounted) {
                setActivity(found);
                if (found?.exercises?.length > 0) {
                  setSelectedExercise(found.exercises[0]);
                }
              }
              return;
            }
          } catch (err) {
            console.error('ActivityWork - relational lookup failed', err);
          }
        }

        // No activity found by either method
        console.warn('ActivityWork - activity not found for route id:', id);
        if (mounted) setActivity(null);
      }catch(e){ console.error(e); }
      finally{ if (mounted) setLoading(false); }
    }
    load();
    return () => { mounted = false; };
  }, [id]);

  async function handleSubmit(e){
    e.preventDefault();
    if (!selectedExercise) {
      alert('Por favor selecciona un ejercicio primero');
      return;
    }
    setSubmitting(true);
    try{
      // For now file upload is disabled; we send link (e.g. repo) and code as part of link or leave fileUrl null.
      // Try to resolve studentId from user -> students list
      let studentId = null;
      try{
        if (user?.id){
          const s = await StudentsApi.findByUserId(user.id);
          studentId = s?.id || null;
        }
      }catch(e){ console.warn('Could not resolve studentId', e); }

      const payload = {
        fileUrl: null,
        link: link || null,
        status: 'SUBMITTED',
        studentId: studentId || user?.id || null,
        exerciseId: Number(selectedExercise.exerciseId)
      };
      // Document endpoint may differ; attempt POST to documents base if available
      try {
        const created = await fetch('/api/submission-documents', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            studentId: payload.studentId,
            exerciseId: payload.exerciseId,
            submissionData: {
              link: payload.link,
              code,
              submittedAt: new Date().toISOString(),
            },
            status: 'SUBMITTED'
          })
        });
        if (!created.ok) throw new Error('POST /api/submission-documents failed');
      } catch (docErr) {
        console.warn('Document submission failed, falling back (no submission create implemented yet)', docErr);
      }
      alert('Tarea enviada (simulada).');
      navigate('/student');
    }catch(err){
      console.error('Submit failed', err);
      alert('Error al enviar la tarea. Revisa la consola.');
    }finally{ setSubmitting(false); }
  }

  const formatDate = (dateStr) => {
    if (!dateStr) return '-';
    const d = new Date(dateStr);
    return Number.isNaN(d.getTime()) ? '-' : d.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const getActivityStatus = () => {
    if (!activity) return null;
    const now = new Date();
    const start = activity.startDate ? new Date(activity.startDate) : null;
    const end = activity.endDate ? new Date(activity.endDate) : null;
    
    if (end && now > end) return { label: 'Vencida', class: 'danger' };
    if (start && now < start) return { label: 'No iniciada', class: 'secondary' };
    return { label: 'Activa', class: 'success' };
  };

  const getDifficultyBadge = (difficulty) => {
    const badges = {
      '1': { label: 'Fácil', class: 'success' },
      '2': { label: 'Medio', class: 'warning' },
      '3': { label: 'Difícil', class: 'danger' },
      'EASY': { label: 'Fácil', class: 'success' },
      'MEDIUM': { label: 'Medio', class: 'warning' },
      'HARD': { label: 'Difícil', class: 'danger' }
    };
    return badges[difficulty] || { label: difficulty || 'N/A', class: 'secondary' };
  };

  return (
    <div className="container-fluid mt-4">
      <button className="btn btn-link mb-3 p-0" onClick={() => navigate('/student/dashboard')}>
        ← Volver al panel
      </button>
      
      {loading && (
        <div className="alert alert-secondary">
          <div className="spinner-border spinner-border-sm me-2" role="status">
            <span className="visually-hidden">Cargando...</span>
          </div>
          Cargando actividad...
        </div>
      )}
      
      {!loading && !activity && (
        <div className="alert alert-warning">
          <h5 className="alert-heading">Actividad no encontrada</h5>
          <p className="mb-0">No se pudo cargar la información de esta actividad. Por favor verifica el enlace o contacta al profesor.</p>
        </div>
      )}
      
      {activity && (
        <>
          {/* Header de la actividad */}
          <div className="card shadow-sm mb-4">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-start mb-3">
                <div>
                  <h3 className="mb-2">{activity.title}</h3>
                  <p className="text-muted mb-2">{activity.description}</p>
                  {activity.professorName && (
                    <div className="d-flex align-items-center gap-2 mb-2">
                      <i className="bi bi-person-circle"></i>
                      <span className="small">
                        <strong>Profesor:</strong> {activity.professorName}
                      </span>
                    </div>
                  )}
                  <div className="d-flex gap-3 small text-muted">
                    <span>
                      <i className="bi bi-calendar-event me-1"></i>
                      Inicio: {formatDate(activity.startDate)}
                    </span>
                    <span>
                      <i className="bi bi-calendar-check me-1"></i>
                      Fin: {formatDate(activity.endDate)}
                    </span>
                  </div>
                </div>
                <div>
                  {(() => {
                    const status = getActivityStatus();
                    return status ? (
                      <span className={`badge bg-${status.class} fs-6 px-3 py-2`}>
                        {status.label}
                      </span>
                    ) : null;
                  })()}
                </div>
              </div>
            </div>
          </div>

          {/* Lista de ejercicios */}
          <div className="row">
            <div className="col-lg-5 mb-4">
              <div className="card shadow-sm">
                <div className="card-header bg-primary text-white">
                  <h5 className="mb-0">
                    <i className="bi bi-list-check me-2"></i>
                    Ejercicios de la actividad
                  </h5>
                </div>
                <div className="card-body p-0">
                  {!activity.exercises || activity.exercises.length === 0 ? (
                    <div className="p-4 text-center text-muted">
                      <i className="bi bi-inbox fs-1 d-block mb-2"></i>
                      No hay ejercicios asignados a esta actividad.
                    </div>
                  ) : (
                    <div className="list-group list-group-flush">
                      {activity.exercises
                        .sort((a, b) => (a.position || 0) - (b.position || 0))
                        .map((exercise, idx) => {
                          const isSelected = selectedExercise?.exerciseId === exercise.exerciseId;
                          const difficultyBadge = getDifficultyBadge(exercise.difficulty);
                          return (
                            <div
                              key={exercise.exerciseId || idx}
                              className={`list-group-item list-group-item-action ${isSelected ? 'active' : ''}`}
                              style={{ cursor: 'pointer' }}
                              onClick={() => setSelectedExercise(exercise)}
                            >
                              <div className="d-flex justify-content-between align-items-start">
                                <div className="flex-grow-1">
                                  <div className="d-flex align-items-center gap-2 mb-1">
                                    <span className="badge bg-secondary">#{exercise.position || idx + 1}</span>
                                    <h6 className="mb-0">{exercise.title || exercise.description || `Ejercicio ${exercise.exerciseId}`}</h6>
                                  </div>
                                  {exercise.description && exercise.title && (
                                    <p className="mb-2 small text-muted">{exercise.description}</p>
                                  )}
                                  <div className="d-flex gap-2 align-items-center">
                                    <span className={`badge bg-${difficultyBadge.class}`}>
                                      {difficultyBadge.label}
                                    </span>
                                    {exercise.pointValue && (
                                      <span className="badge bg-info">
                                        <i className="bi bi-star-fill me-1"></i>
                                        {exercise.pointValue} puntos
                                      </span>
                                    )}
                                  </div>
                                </div>
                                {isSelected && (
                                  <i className="bi bi-check-circle-fill text-white fs-4"></i>
                                )}
                              </div>
                            </div>
                          );
                        })}
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* Área de trabajo del ejercicio seleccionado */}
            <div className="col-lg-7">
              {!selectedExercise ? (
                <div className="card shadow-sm">
                  <div className="card-body text-center py-5">
                    <i className="bi bi-arrow-left-circle fs-1 text-muted d-block mb-3"></i>
                    <h5 className="text-muted">Selecciona un ejercicio para comenzar</h5>
                    <p className="text-muted mb-0">Haz clic en cualquier ejercicio de la lista para ver los detalles y enviar tu solución.</p>
                  </div>
                </div>
              ) : (
                <div className="card shadow-sm">
                  <div className="card-header bg-light">
                    <div className="d-flex justify-content-between align-items-center">
                      <h5 className="mb-0">
                        <i className="bi bi-code-slash me-2"></i>
                        {selectedExercise.title || selectedExercise.description || `Ejercicio ${selectedExercise.exerciseId}`}
                      </h5>
                      {selectedExercise.pointValue && (
                        <span className="badge bg-primary fs-6">
                          {selectedExercise.pointValue} puntos
                        </span>
                      )}
                    </div>
                  </div>
                  <div className="card-body">
                    {selectedExercise.description && selectedExercise.title && (
                      <div className="alert alert-info mb-3">
                        <strong>Descripción:</strong> {selectedExercise.description}
                      </div>
                    )}
                    
                    <form onSubmit={handleSubmit}>
                      <div className="mb-3">
                        <label className="form-label fw-bold">
                          <i className="bi bi-file-earmark-code me-2"></i>
                          Editor de código
                        </label>
                        <textarea
                          className="form-control font-monospace"
                          rows={14}
                          value={code}
                          onChange={e => setCode(e.target.value)}
                          placeholder="// Escribe tu solución aquí..."
                        />
                        <small className="form-text text-muted">
                          Este es un editor simple. Puedes escribir tu código o pegar tu solución aquí.
                        </small>
                      </div>

                      <div className="mb-3">
                        <label className="form-label fw-bold">
                          <i className="bi bi-link-45deg me-2"></i>
                          Link del repositorio (opcional)
                        </label>
                        <input
                          className="form-control"
                          type="url"
                          value={link}
                          onChange={e => setLink(e.target.value)}
                          placeholder="https://github.com/tu-usuario/tu-repo"
                        />
                        <small className="text-muted">Si prefieres, puedes compartir el enlace a tu repositorio de GitHub.</small>
                      </div>

                      <div className="d-flex gap-2">
                        {(() => {
                          const status = getActivityStatus();
                          const isDisabled = status?.label === 'Vencida' || status?.label === 'No iniciada';
                          return (
                            <>
                              <button
                                className="btn btn-success"
                                type="submit"
                                disabled={submitting || isDisabled}
                              >
                                {submitting ? (
                                  <>
                                    <span className="spinner-border spinner-border-sm me-2" role="status"></span>
                                    Enviando...
                                  </>
                                ) : (
                                  <>
                                    <i className="bi bi-send me-2"></i>
                                    Enviar solución
                                  </>
                                )}
                              </button>
                              {isDisabled && (
                                <span className="badge bg-warning text-dark align-self-center">
                                  {status.label === 'Vencida' ? 'Actividad vencida' : 'Actividad no disponible aún'}
                                </span>
                              )}
                              <button
                                type="button"
                                className="btn btn-outline-secondary"
                                onClick={() => { setCode('// Escribe tu solución aquí'); setLink(''); }}
                              >
                                <i className="bi bi-arrow-counterclockwise me-2"></i>
                                Reiniciar
                              </button>
                            </>
                          );
                        })()}
                      </div>
                    </form>
                  </div>
                </div>
              )}
            </div>
          </div>
        </>
      )}
    </div>
  );
}
