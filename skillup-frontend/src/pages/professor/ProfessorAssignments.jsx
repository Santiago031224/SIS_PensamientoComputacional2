import React, { useEffect, useMemo, useState } from 'react';
import { useAuth } from '../../services/AuthContext';
import { useProfessorAnalytics } from '../../hooks/useProfessorAnalytics';
import { ExerciseDocuments } from '../../services/exerciseDocuments';
import { ActivityDocuments } from '../../services/activityDocuments';
import { PointCodeDocuments } from '../../services/pointCodeDocuments';
import { PeriodsApi } from '../../services/periods';
import '../../styles/professorAssignments.css';

function formatDate(value) {
  if (!value) return 'Sin fecha';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleDateString('es-CO', { year: 'numeric', month: 'short', day: 'numeric' });
}

export default function ProfessorAssignments() {
  const { user } = useAuth();
  const { loading, professor, assignments, activities, groups } = useProfessorAnalytics(user);
  


  const [form, setForm] = useState({
    title: '',
    description: '',
    assignmentIds: [],
    startDate: '',
    endDate: '',
    totalPoints: 50,
  });
  const [allExercises, setAllExercises] = useState([]);
  const [newExercise, setNewExercise] = useState({ description: '', difficulty: 0 });
  const [creatingExercise, setCreatingExercise] = useState(false);
  const [picker, setPicker] = useState({ exerciseId: '', points: 0 });
  const [selectedExercises, setSelectedExercises] = useState([]);
  const [submitting, setSubmitting] = useState(false);
  const [activeTab, setActiveTab] = useState('create');
  const [currentPeriod, setCurrentPeriod] = useState(null);
  const [mongoActivities, setMongoActivities] = useState([]);
  const [loadingMongoActivities, setLoadingMongoActivities] = useState(false);

 useEffect(() => {
  let mounted = true;

  async function loadMongoActivities() {
    setLoadingMongoActivities(true);
    try {
      const list = await ActivityDocuments.list(); // Ajusta según tu servicio
      if (mounted && Array.isArray(list)) {
        setMongoActivities(list);
      }
    } catch (err) {
      console.error('Error cargando actividades desde Mongo:', err);
    } finally {
      if (mounted) setLoadingMongoActivities(false);
    }
  }

  loadMongoActivities();

  return () => { mounted = false; };
}, []);


  // Cargar ejercicios
  useEffect(() => {
    let mounted = true;
    async function loadExercises() {
      try {
        const list = await ExerciseDocuments.list();
        if (mounted) setAllExercises(Array.isArray(list) ? list : []);
      } catch (e) { console.error(e); }
    }
    loadExercises();
    return () => { mounted = false; };
  }, []);

  // Crear nuevo ejercicio
  // En tu componente - FUNCIÓN CORREGIDA
async function handleCreateExercise(e) {
  e.preventDefault();
  
  // Validaciones
  if (!newExercise.description.trim()) {
    alert('❌ La descripción del ejercicio es obligatoria');
    return;
  }

  const payload = {
    description: newExercise.description.trim(),
    difficulty: Number(newExercise.difficulty) || 0,
    // NO incluir sql_id - el backend lo genera
  };
  
  setCreatingExercise(true);
  try {
    const created = await ExerciseDocuments.create(payload);
    
    // Actualizar la lista de ejercicios
    setAllExercises(prev => [created, ...prev]);
    
    // Resetear el formulario
    setNewExercise({ description: '', difficulty: 0 });
    
    alert('✅ Ejercicio creado correctamente');
    
  } catch (err) {
    console.error('💥 Error completo:', err);
    
    // Manejo detallado de errores
    if (err.response?.status === 400) {
      const errorData = err.response?.data;
      if (typeof errorData === 'string') {
        alert(`❌ Error: ${errorData}`);
      } else if (errorData?.message) {
        alert(`❌ Error: ${errorData.message}`);
      } else {
        alert('❌ Error: Datos inválidos. Verifica los campos.');
      }
    } else if (err.response?.status === 404) {
      alert('❌ Error: Endpoint no encontrado. Verifica la configuración del servidor.');
    } else if (err.response?.status === 500) {
      alert('❌ Error interno del servidor.');
    } else {
      alert(`❌ Error: ${err.message || 'No se pudo crear el ejercicio'}`);
    }
  } finally {
    setCreatingExercise(false);
  }
}

  function handleChange(event) {
    const { name, value } = event.target;
    if (name === 'assignmentIds') {
      const selected = Array.from(event.target.selectedOptions).map(opt => Number(opt.value));
      setForm(prev => ({ ...prev, assignmentIds: selected }));
      return;
    }
    setForm(prev => ({ ...prev, [name]: name === 'totalPoints' ? Number(value) : value }));
  }

  function handlePickerChange(event) {
    const { name, value } = event.target;
    setPicker(prev => ({ ...prev, [name]: name === 'points' ? Number(value) : value }));
  }

  function addExercise() {
    if (!picker.exerciseId) return;
    const exerciseId = Number(picker.exerciseId);
    if (selectedExercises.some(e => e.exerciseId === exerciseId)) return;
    setSelectedExercises(prev => [...prev, { exerciseId, points: Number(picker.points) || 0 }]);
    setPicker({ exerciseId: '', points: 0 });
  }

  function removeExercise(exerciseId) {
    setSelectedExercises(prev => prev.filter(e => e.exerciseId !== exerciseId));
  }

  const pointsAssigned = useMemo(
    () => selectedExercises.reduce((s, e) => s + (e.points || 0), 0),
    [selectedExercises]
  );
  const pointsOk = useMemo(
    () => Number(form.totalPoints || 0) === pointsAssigned,
    [form.totalPoints, pointsAssigned]
  );

  // Preparar assignments con metadatos
  const assignmentsWithMetadata = useMemo(() => {
    if (!assignments.length) return [];
    const groupsById = new Map(groups.map(g => [g.id, g]));
    const activitiesByAssignmentId = activities.reduce((acc, a) => {
      if (!acc.has(a.teachingAssignmentId)) acc.set(a.teachingAssignmentId, []);
      acc.get(a.teachingAssignmentId).push(a);
      return acc;
    }, new Map());

    return assignments.map(a => {
      const group = groupsById.get(a.groupId);
      const relatedActivities = activitiesByAssignmentId.get(a.id) || [];
      const weightDecimal = a.evaluationWeight ?? 0;
      const dueDate = a.dueDate || (relatedActivities.length ? relatedActivities[0].endDate : null);
      return {
        ...a,
        groupName: group?.name || 'Sin grupo',
        weightPercent: weightDecimal * 100,
        dueDate,
        exercises: relatedActivities.flatMap(act => act.exercises || [])
      };
    });
  }, [assignments, activities, groups]);

  const totalWeight = useMemo(
    () => assignmentsWithMetadata.reduce((sum, item) => sum + item.weightPercent, 0),
    [assignmentsWithMetadata]
  );
  const totalWeightDisplay = Math.round(totalWeight);

  // Enviar actividad
  async function handleSubmit(event) {
    event.preventDefault();
    if (!form.title.trim() || !professor || !form.assignmentIds.length) return alert('Formulario incompleto');
    if (!pointsOk) return alert('La suma de puntos por ejercicio debe ser igual al total de la actividad');

    const basePayload = {
      title: form.title,
      description: form.description,
      startDate: form.startDate || null,
      endDate: form.endDate || null,
      professor_id: professor.id,
      totalPoints: Number(form.totalPoints),
      periods: currentPeriod ? [currentPeriod.id] : [],
      exercises: selectedExercises.map((e, idx) => ({ exerciseId: e.exerciseId, points: e.points, position: idx + 1 })),
      groups: form.assignmentIds.map(id => Number(id))
    };

    setSubmitting(true);
    try {
      for (const assignmentId of form.assignmentIds) {
        await ActivityDocuments.create({ ...basePayload, teachingAssignmentId: Number(assignmentId) });
      }
      alert('✅ Actividades creadas con éxito');
      setForm({ title: '', description: '', assignmentIds: [], startDate: '', endDate: '', totalPoints: 50 });
      setSelectedExercises([]);
      window.location.reload();
    } catch (e) {
      console.error('Error creando actividad', e);
      alert('❌ No se pudo crear la actividad');
    } finally {
      setSubmitting(false);
    }
  }

  async function generateCodesForActivity(activity, exercisesInActivity) {
    if (!professor || !exercisesInActivity.length) return alert('La actividad no tiene ejercicios agregados');
    if (!window.confirm('¿Generar un código por ejercicio para calificación rápida?')) return;

    try {
      const created = [];
      for (const ex of exercisesInActivity) {
        const codePayload = {
          teachingAssignmentId: activity.teachingAssignmentId,
          exerciseId: ex.exerciseId,
          points: ex.points,
          createdByProfessorId: professor.id,
        };
        const codeDoc = await PointCodeDocuments.create(codePayload);
        created.push(codeDoc);
      }
      alert(`✅ Códigos generados: ${created.length}`);
    } catch (err) {
      console.error('Error generando códigos', err);
      alert('❌ No se pudieron generar los códigos');
    }
  }

  if (loading) return (
    <div className="container-fluid py-5 text-center">
      <div className="spinner-border text-primary" role="status" aria-label="Cargando asignaciones del profesor" />
    </div>
  );

  if (!professor) return (
    <div className="container-fluid py-5">
      <div className="alert alert-warning text-center" role="alert">
        <i className="bi bi-exclamation-triangle me-2"></i>
        No encontramos asignaciones asociadas al usuario actual.
      </div>
    </div>
  );

  return (
    <div className="container-fluid professor-assignments">
      {/* Header */}
      <div className="dashboard-header">
        <div className="header-content">
          <div className="title-section">
            <h1 className="page-title"><i className="bi bi-journal-bookmark-fill"></i> Gestión de Actividades</h1>
            <p className="page-subtitle">Crea, programa y gestiona actividades académicas</p>
          </div>
          <div className="stats-card">
            <div className="stat-icon"><i className="bi bi-speedometer2"></i></div>
            <div className="stat-content">
              <div className="stat-value">{totalWeightDisplay}%</div>
              <div className="stat-label">Peso Académico</div>
            </div>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="navigation-tabs">
        <button className={`nav-tab ${activeTab === 'create' ? 'active' : ''}`} onClick={() => setActiveTab('create')}>
          <i className="bi bi-plus-circle"></i> Nueva Actividad
        </button>
        <button className={`nav-tab ${activeTab === 'manage' ? 'active' : ''}`} onClick={() => setActiveTab('manage')}>
          <i className="bi bi-list-task"></i> Mis Actividades
        </button>
      </div>

      {/* Content */}
      <div className="content-grid">
        {/* Panel Creación */}
        {activeTab === 'create' && (
          <div className="creation-panel">
            <div className="form-card">
              <div className="card-header">
                <h3><i className="bi bi-magic"></i> Crear Nueva Actividad</h3>
                <p>Completa la información básica y asigna ejercicios</p>
              </div>

              <form onSubmit={handleSubmit} className="activity-form">
                {/* Información básica */}
                <div className="form-section">
                  <h4>Información Básica</h4>
                  <div className="row g-3">
                    <div className="col-md-6">
                      <label className="form-label">Título *</label>
                      <input
                        name="title"
                        value={form.title}
                        onChange={handleChange}
                        className="form-control"
                        placeholder="Ej: Introducción a Algoritmos"
                        required
                      />
                    </div>
                    <div className="col-md-6">
                      <label className="form-label">Puntos Totales *</label>
                      <input
                        name="totalPoints"
                        type="number"
                        min="0"
                        value={form.totalPoints}
                        onChange={handleChange}
                        className="form-control"
                        required
                      />
                    </div>
                  </div>
                  <div className="form-group mt-3">
                    <label className="form-label">Descripción</label>
                    <textarea
                      name="description"
                      value={form.description}
                      onChange={handleChange}
                      className="form-control"
                      rows={3}
                      placeholder="Describe los objetivos y criterios de evaluación..."
                    />
                  </div>
                  <div className="row g-3 mt-2">
                    <div className="col-md-6">
                      <label className="form-label">Fecha de Inicio</label>
                      <input 
                        name="startDate" 
                        type="date" 
                        className="form-control" 
                        value={form.startDate} 
                        onChange={handleChange} 
                      />
                    </div>
                    <div className="col-md-6">
                      <label className="form-label">Fecha de Entrega</label>
                      <input 
                        name="endDate" 
                        type="date" 
                        className="form-control" 
                        value={form.endDate} 
                        onChange={handleChange} 
                      />
                    </div>
                  </div>
                </div>

                {/* Grupos */}
                <div className="form-section mt-4">
                  <h4>Grupos Destinatarios</h4>
                  <select
                    name="assignmentIds"
                    multiple
                    value={form.assignmentIds.map(String)}
                    onChange={handleChange}
                    className="form-select"
                    size={4}
                  >
                    {assignments.map(a => {
                      const g = groups.find(gr => gr.id === a.groupId);
                      return <option key={`${a.id}-${a.groupId}`} value={a.id}>
                                {g?.name || 'Grupo'} · Peso {Math.round((a.evaluationWeight||0)*100)}%
                              </option>

                    })}
                  </select>
                  <div className="form-text">Mantén Ctrl/Cmd para seleccionar múltiples grupos</div>
                </div>

                {/* Ejercicios */}
                <div className="form-section mt-4">
                  <h4>Gestión de Ejercicios</h4>
                  {/* Selector existente */}
                  <div className="exercise-selector row g-2 mb-3">
                    <div className="col-md-6">
                      <select name="exerciseId" className="form-select" value={picker.exerciseId} onChange={handlePickerChange}>
                        <option value="">Selecciona un ejercicio...</option>
                        {allExercises.map((ex, index) => (
                          <option key={`ex-${ex.id ?? index}`} value={ex.id ?? ""}>

                            {ex.description || `Ejercicio ${ex.id ?? index + 1}`} {ex.difficulty > 0 ? ` · Dificultad: ${ex.difficulty}/10` : ''}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div className="col-md-3">
                      <input name="points" type="number" min="0" className="form-control" placeholder="Puntos" value={picker.points} onChange={handlePickerChange} />
                    </div>
                    <div className="col-md-3">
                      <button type="button" className="btn btn-primary w-100" onClick={addExercise} disabled={!picker.exerciseId}>
                        <i className="bi bi-plus-lg"></i> Agregar
                      </button>
                    </div>
                  </div>

                  {/* Crear nuevo */}
                  <details className="mb-3">
                    <summary><i className="bi bi-plus-square"></i> Crear Nuevo Ejercicio</summary>
                    <div className="row g-2 mt-2">
                      <div className="col-md-8">
                        <input className="form-control" placeholder="Descripción del ejercicio..." value={newExercise.description} onChange={e => setNewExercise(prev => ({ ...prev, description: e.target.value }))} />
                      </div>
                      <div className="col-md-2">
                        <input type="number" min="0" max="10" className="form-control" placeholder="Dificultad" value={newExercise.difficulty} onChange={e => setNewExercise(prev => ({ ...prev, difficulty: Number(e.target.value) }))} />
                      </div>
                      <div className="col-md-2">
                        <button type="button" className="btn btn-outline-primary w-100" disabled={creatingExercise || !newExercise.description.trim()} onClick={handleCreateExercise}>
                          {creatingExercise ? '...' : 'Crear'}
                        </button>
                      </div>
                    </div>
                  </details>

                  {/* Ejercicios seleccionados */}
                  <div className="selected-exercises">
                    <div className="d-flex justify-content-between align-items-center mb-2">
                      <h5>Ejercicios Asignados</h5>
                      <div className={`points-status ${pointsOk ? 'valid' : 'invalid'}`}>{pointsAssigned} / {form.totalPoints} puntos</div>
                    </div>
                    {selectedExercises.length === 0 ? (
                      <div className="empty-state text-center"><i className="bi bi-inbox"></i> No hay ejercicios agregados</div>
                    ) : (
                      <div className="exercises-list">
                        {selectedExercises.map(rel => {
                          const ex = allExercises.find(e => e.id === rel.exerciseId);
                          return (
                           <div key={`selected-${rel.exerciseId}-${index}`} className="exercise-item">

                              <div>
                                {ex?.description || `Ejercicio ${rel.exerciseId}`} {ex?.difficulty > 0 && `(Dificultad: ${ex.difficulty}/10)`}
                              </div>
                              <div>
                                <span className="badge bg-secondary me-2">{rel.points} pts</span>
                                <button type="button" className="btn btn-sm btn-outline-danger" onClick={() => removeExercise(rel.exerciseId)}>
                                  <i className="bi bi-x-lg"></i>
                                </button>
                              </div>
                            </div>
                          )
                        })}
                      </div>
                    )}
                  </div>
                </div>

                {/* Botón submit */}
                <div className="form-actions mt-4">
                  <button type="submit" className="btn btn-primary btn-lg" disabled={!pointsOk || submitting || !form.title.trim() || !form.assignmentIds.length}>
                    {submitting ? (
                      <>
                        <div className="spinner-border spinner-border-sm me-2"></div> Creando Actividades...
                      </>
                    ) : (
                      <>
                        <i className="bi bi-check-lg me-2"></i> Crear Actividades
                      </>
                    )}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* Panel Gestión */}
{activeTab === 'manage' && (
  <div className="management-panel">
    <div className="activities-card">
      <div className="card-header">
        <h3><i className="bi bi-grid-3x3-gap"></i> Mis Actividades</h3>
        <p>Gestiona las actividades creadas por grupo</p>
      </div>
      <div className="activities-table-container">
        {loadingMongoActivities ? (
          <div className="text-center py-3">
            <div className="spinner-border" role="status"></div>
          </div>
        ) : mongoActivities.length === 0 ? (
          <div className="empty-state text-center">
            <i className="bi bi-journal-x"></i>
            <h4>No hay actividades registradas</h4>
          </div>
        ) : (
          <div className="activities-grid">
            {mongoActivities.map((item, index) => (
              <div key={item._id ?? `activity-${index}`} className="activity-card">

                <div className="activity-header d-flex justify-content-between align-items-center">
                  <h4>{item.title}</h4>
                  <span className="weight-badge">{item.max_points || 0} pts</span>
                </div>
                <div className="activity-body">
                  <p>{item.description || 'Sin descripción asignada'}</p>

                  <div className="activity-meta d-flex flex-wrap gap-2 mb-2">
  <div className="meta-item"><i className="bi bi-person"></i> {item.professor_name}</div>
  <div className="meta-item">
    <i className="bi bi-people"></i> {Array.isArray(item.groups) ? item.groups.map(id => `Grupo ${id}`).join(', ') : 'Sin grupo'}
  </div>
  <div className="meta-item">
    <i className="bi bi-calendar-event"></i> {Array.isArray(item.periods) ? item.periods.join(', ') : 'Sin periodo'}
  </div>
  <div className="meta-item"><i className="bi bi-calendar-check"></i> {formatDate(item.schedule?.start_date)}</div>
  <div className="meta-item"><i className="bi bi-calendar-x"></i> {formatDate(item.schedule?.end_date)}</div>
  <div className="meta-item"><i className="bi bi-circle"></i> {item.schedule?.status || 'Programada'}</div>
</div>


                  {/* Ejercicios asignados */}
                  {Array.isArray(item.exercises) && item.exercises.length > 0 && (
                    <div className="assigned-exercises mt-2">
                      <h5>Ejercicios Asignados</h5>
                      {item.exercises.map((ex, idx) => (
                          <div key={`activity-${item._id}-exercise-${ex.exercise_id}-${idx}`} className="exercise-item">

                          <div>
                            <strong>{ex.title}</strong> - {ex.description} {ex.difficulty > 0 && `(Dificultad: ${ex.difficulty}/10)`}
                          </div>
                          <div>
                            <span className="badge bg-secondary me-2">{ex.point_value} pts</span>
                            <span className="badge bg-info">{`Posición: ${ex.position}`}</span>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>

                <div className="activity-actions mt-2">
                  <button
                    type="button"
                    className="btn btn-outline-primary"
                    onClick={() => generateCodesForActivity(item, item.exercises || [])}
                    disabled={!Array.isArray(item.exercises) || item.exercises.length === 0}
                  >
                    <i className="bi bi-qr-code"></i> Generar Códigos
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div> {/* activities-table-container */}
    </div> {/* activities-card */}
  </div> /* management-panel */
)}

      </div> {/* content-grid */}
    </div>
  );
}