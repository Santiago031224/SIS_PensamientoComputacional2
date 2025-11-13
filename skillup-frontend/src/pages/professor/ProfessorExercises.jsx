import React, { useState, useEffect } from 'react';
import { ExercisesApi } from '../../services/exercises';
import '../../styles/professorExercises.css';

export default function ProfessorExercises() {
  const [exercises, setExercises] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingExercise, setEditingExercise] = useState(null);

  const [formData, setFormData] = useState({
    title: '',
    description: '',
    difficulty: '1',
    programming_language: '',
  });

  useEffect(() => { 
    loadExercises(); 
  }, []);

  const loadExercises = async () => {
    try {
      setLoading(true);
      setError('');
      const data = await ExercisesApi.list();
      setExercises(data);
    } catch (err) {
      setError('Error al cargar ejercicios: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

const getExerciseId = (exercise) => exercise.relationalId || exercise.mongoId;




  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.title.trim() || !formData.description.trim()) {
      setError('El título y la descripción son requeridos');
      return;
    }
    try {
      setLoading(true);
      setError('');
      
      if (editingExercise) {
  const exerciseId = getExerciseId(editingExercise); // relationalId
  const payload = { ...formData, id: exerciseId };   // <- importante
  await ExercisesApi.update(exerciseId, payload);
} else {
  await ExercisesApi.create(formData);
}


      
      setFormData({ title: '', description: '', difficulty: '1', programming_language: '' });
      setShowForm(false);
      setEditingExercise(null);
      await loadExercises();
    } catch (err) {
      console.error('Error saving exercise:', err);
      setError('Error al guardar ejercicio: ' + (err.response?.data?.message || err.message));
    } finally { 
      setLoading(false); 
    }
  };

  const handleEdit = (exercise) => {
    setEditingExercise(exercise);
    setFormData({
      title: exercise.title || '',
      description: exercise.description || '',
      difficulty: exercise.difficulty?.toString() || '1',
      programming_language: exercise.programming_language || '',
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Está seguro de eliminar este ejercicio?')) return;
    try {
      setLoading(true);
      setError('');
      await ExercisesApi.delete(id);
      await loadExercises();
    } catch (err) {
      console.error('Error deleting exercise:', err);
      setError('Error al eliminar ejercicio: ' + (err.response?.data?.message || err.message));
    } finally { 
      setLoading(false); 
    }
  };

  const handleCancel = () => {
    setShowForm(false);
    setEditingExercise(null);
    setFormData({ title: '', description: '', difficulty: '1', programming_language: '' });
    setError('');
  };

  const getDifficultyBadgeClass = (difficulty) => {
    switch (difficulty) {
      case '1': return 'badge-success';
      case '2': return 'badge-success';
      case '3': return 'badge-success';
      case '4': return 'badge-success';
      case '5': return 'badge-warning';
      case '6': return 'badge-warning';
      case '7': return 'badge-warning';
      case '8': return 'badge-danger';
      case '9': return 'badge-danger';
      case '10': return 'badge-danger';
      default: return 'badge-secondary';
    }
  };

  return (
    <div className="professor-container">
      <div className="professor-header">
        <h2>📝 Gestión de Ejercicios</h2>
        <button
          className="btn btn-primary"
          onClick={() => setShowForm(!showForm)}
          disabled={loading}
        >
          {showForm ? '❌ Cancelar' : '➕ Nuevo Ejercicio'}
        </button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      {showForm && (
        <div className="card mb-4">
          <div className="card-header">
            <h5>{editingExercise ? '✏️ Editar Ejercicio' : '➕ Crear Nuevo Ejercicio'}</h5>
          </div>
          <div className="card-body">
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="title">Título *</label>
                <input
                  id="title"
                  name="title"
                  type="text"
                  className="form-control"
                  value={formData.title}
                  onChange={handleInputChange}
                  placeholder="Ingresa el título del ejercicio"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="description">Descripción *</label>
                <textarea
                  id="description"
                  name="description"
                  className="form-control"
                  rows="4"
                  value={formData.description}
                  onChange={handleInputChange}
                  placeholder="Describe el ejercicio..."
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="difficulty">Dificultad (1-10)</label>
                <select
                  id="difficulty"
                  name="difficulty"
                  className="form-control"
                  value={formData.difficulty}
                  onChange={handleInputChange}
                >
                  {[1,2,3,4,5,6,7,8,9,10].map(num => (
                    <option key={num} value={num.toString()}>{num}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="programming_language">Lenguaje de Programación</label>
                <input
                  id="programming_language"
                  name="programming_language"
                  type="text"
                  className="form-control"
                  value={formData.programming_language}
                  onChange={handleInputChange}
                  placeholder="Ej: Python, JavaScript"
                />
              </div>

              <div className="form-actions mt-2">
                <button type="submit" className="btn btn-success" disabled={loading}>
                  {loading ? '⏳ Guardando...' : '💾 Guardar'}
                </button>
                <button type="button" className="btn btn-secondary ml-2" onClick={handleCancel} disabled={loading}>
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="card">
        <div className="card-header">
          <h5>📚 Listado de Ejercicios ({exercises.length})</h5>
        </div>
        <div className="card-body">
          {loading && !showForm ? (
            <div className="text-center">
              <div className="spinner-border" role="status">
                <span className="sr-only">Cargando...</span>
              </div>
            </div>
          ) : exercises.length === 0 ? (
            <div className="alert alert-info">No hay ejercicios creados.</div>
          ) : (
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Título / Descripción</th>
                    <th>Dificultad</th>
                    <th>Lenguaje</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {exercises.map((exercise, index) => {
                    const exerciseId = getExerciseId(exercise);
                    return (
                      <tr key={exerciseId || `exercise-${index}`}>
                        <td>{exerciseId || '-'}</td>
                        <td style={{ maxWidth: '400px' }}>
                          <strong>{exercise.title}</strong>
                          {exercise.description && (
                            <div className="text-muted small mt-1">
                              {exercise.description.length > 100 
                                ? `${exercise.description.substring(0, 100)}...`
                                : exercise.description
                              }
                            </div>
                          )}
                        </td>
                        <td>
                          <span className={`badge ${getDifficultyBadgeClass(exercise.difficulty)}`}>
                            {exercise.difficulty || '-'}
                          </span>
                        </td>
                        <td>{exercise.programming_language || '-'}</td>
                        <td>
                          <button 
                            className="btn btn-sm btn-info mr-2" 
                            onClick={() => handleEdit(exercise)} 
                            disabled={loading}
                          >
                            ✏️ Editar
                          </button>
                          <button 
                            className="btn btn-sm btn-danger" 
                            onClick={() => handleDelete(exerciseId)} 
                            disabled={loading}
                          >
                            🗑️ Eliminar
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}