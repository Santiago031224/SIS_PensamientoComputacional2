import React, { useEffect, useMemo, useState } from 'react';
import QRCode from 'qrcode';
import { useAuth } from '../../services/AuthContext';
import { useProfessorAnalytics } from '../../hooks/useProfessorAnalytics';
import { ExerciseDocuments } from '../../services/exerciseDocuments';
import { PointCodeDocuments } from '../../services/pointCodeDocuments';
import '../../styles/codeqr.css';

function formatDateTime(value) {
  if (!value) return '—';
  const d = new Date(value);
  return Number.isNaN(d.getTime()) ? String(value) : d.toLocaleString();
}

export default function ProfessorCodes() {
  const { user } = useAuth();
  const { loading, professor, assignments, groups } = useProfessorAnalytics(user);
  const [exercises, setExercises] = useState([]);
  const [codes, setCodes] = useState([]);
  const [creating, setCreating] = useState(false);
  const [qr, setQr] = useState({ code: '', dataUrl: '' });
  const [form, setForm] = useState({ assignmentId: '', exerciseId: '', points: 1, expiresAt: '' });

  useEffect(() => {
    let mounted = true;
    ExerciseDocuments.list().then(list => { if (mounted) setExercises(Array.isArray(list) ? list : []); });
    PointCodeDocuments.list().then(list => { if (mounted) setCodes(Array.isArray(list) ? list : []); });
    return () => { mounted = false; };
  }, []);

  useEffect(() => {
    if (!form.assignmentId && assignments.length) setForm(prev => ({ ...prev, assignmentId: String(assignments[0].id) }));
  }, [assignments, form.assignmentId]);

  const groupsById = useMemo(() => new Map(groups.map(g => [g.id, g])), [groups]);

  function handleChange(e) {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: name === 'points' ? Number(value) : value }));
  }

  async function handleCreate(e) {
    e.preventDefault();
    if (!professor || !form.assignmentId || !form.exerciseId || !form.points) return;
    setCreating(true);
    try {
      const payload = {
        teachingAssignmentId: Number(form.assignmentId),
        exerciseId: Number(form.exerciseId),
        points: Number(form.points),
        expiresAt: form.expiresAt || null,
        createdByProfessorId: professor.id,
      };
      const created = await PointCodeDocuments.create(payload);

    // Agregar expiresAt al objeto para que se vea en la tabla
    const createdWithExpires = { ...created, expiresAt: payload.expiresAt };
    setCodes(prev => [createdWithExpires, ...prev]);
      setCodes(prev => [created, ...prev]); 

      // Generar QR automáticamente
      const codeValue = created.code || created.id;
      if (codeValue) {
        const dataUrl = await QRCode.toDataURL(String(codeValue), { errorCorrectionLevel: 'M', margin: 1, width: 240 });
        setQr({ code: codeValue, dataUrl });
      }
    } catch (err) {
      console.error('Error creando código', err);
      alert('❌ No se pudo crear el código');
    } finally {
      setCreating(false);
    }
  }

  async function handleDeactivate(code) {
    try {
      await PointCodeDocuments.deactivate(code);
      setCodes(prev => prev.map(c => (c.code === code ? { ...c, active: false } : c)));
    } catch (err) {
      console.error('No se pudo desactivar el código', err);
      alert('❌ No se pudo desactivar el código');
    }
  }

  async function handleViewQr(codeValue) {
    const dataUrl = await QRCode.toDataURL(String(codeValue), { errorCorrectionLevel: 'M', margin: 1, width: 240 });
    setQr({ code: codeValue, dataUrl });
  }

  return (
    <div className="container-fluid professor-codes">
      <div className="header p-4 mb-4 rounded-4 shadow-sm bg-gradient">
        <h2 className="text-white mb-1">🎯 Códigos y QR de puntos</h2>
        <p className="text-light">Genera códigos alfanuméricos o QR para asignar puntos rápidamente en clase</p>
      </div>

      <div className="row g-4">
        {/* Formulario */}
        <section className="col-lg-5">
          <div className="card h-100 shadow-sm border-0">
            <div className="card-body">
              <h5 className="card-title mb-3">
                <i className="bi bi-qr-code text-success me-2"></i> Generar Código
              </h5>
              <form onSubmit={handleCreate}>
                <div className="mb-3">
                  <label className="form-label">Asignación/Grupo</label>
                  <select className="form-select" name="assignmentId" value={form.assignmentId} onChange={handleChange}>
                    <option value="">Selecciona</option>
                    {assignments.map(a => {
                      const g = groupsById.get(a.groupId);
                      return <option key={a.id} value={a.id}>{g?.name || 'Grupo'}</option>;
                    })}
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Ejercicio</label>
                  <select className="form-select" name="exerciseId" value={form.exerciseId} onChange={handleChange}>
                    <option value="">Selecciona un ejercicio</option>
                    {exercises.map(ex => (
                      <option key={ex.id} value={ex.id}>{ex.description || `Ejercicio ${ex.id}`}</option>
                    ))}
                  </select>
                </div>

                <div className="row g-3 mb-3">
                  <div className="col-6">
                    <label className="form-label">Puntos</label>
                    <input type="number" min="0" name="points" className="form-control" value={form.points} onChange={handleChange} />
                  </div>
                  <div className="col-6">
                    <label className="form-label">Expira (opcional)</label>
                    <input type="datetime-local" name="expiresAt" className="form-control" value={form.expiresAt} onChange={handleChange} />
                  </div>
                </div>

                <div className="text-end">
                  <button type="submit" className="btn btn-success btn-lg" disabled={creating}>
                    {creating ? 'Creando…' : 'Generar Código'}
                  </button>
                </div>
              </form>

              {/* QR */}
              {qr.dataUrl && (
                <div className="mt-4 text-center p-3 border rounded-3 shadow-sm bg-light">
                  <h6 className="mb-2 text-success"><i className="bi bi-qr-code-scan me-1"></i> QR Generado</h6>
                  <img src={qr.dataUrl} alt={`QR ${qr.code}`} className="mb-2" style={{ maxWidth: '180px' }} />
                  <div>
                    <span className="badge bg-dark fs-6">
                      <i className="bi bi-key me-1"></i>
                      Código: <code className="text-warning">{qr.code}</code>
                    </span>
                  </div>
                </div>
              )}
            </div>
          </div>
        </section>

        {/* Tabla de códigos */}
        <section className="col-lg-7">
          <div className="card h-100 shadow-sm border-0">
            <div className="card-body">
              <h5 className="card-title mb-3">
                <i className="bi bi-clock-history text-primary me-2"></i> Códigos recientes
              </h5>

              <div className="table-responsive">
                <table className="table align-middle table-hover">
                  <thead className="table-light">
                    <tr>
                      <th>Código</th>
                      <th>Ejercicio</th>
                      <th>Pts</th>
                      <th>Estado</th>
                      <th>Expira</th>
                      <th className="text-end">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                      {codes.length === 0 ? (
                        <tr><td colSpan={6} className="text-center text-muted">No hay códigos generados</td></tr>
                      ) : codes.map(c => {
                          console.log('Código:', c, 'ExpiresAt:', c.expiresAt); 
                          return (
                            <tr key={c.id}>
                              <td><code>{c.code || c.id}</code></td>
                              <td>{c.exerciseId != null ? `Ejercicio ${c.exerciseId}` : '—'}</td>
                              <td>{c.points ?? '—'}</td>
                              <td>{c.active === false ? 'Inactivo' : (c.redeemedBy ? 'Redimido' : 'Activo')}</td>
                              <td>{formatDateTime(c.expiresAt)}</td>
                              <td className="text-end">
                                {c.active !== false && !c.redeemedBy && (
                                  <>
                                    <button className="btn btn-sm btn-outline-danger me-2" onClick={() => handleDeactivate(c.id)}>Desactivar</button>
                                    <button className="btn btn-sm btn-outline-success" onClick={() => handleViewQr(c.code || c.id)}>Ver QR</button>
                                  </>
                                )}
                              </td>
                            </tr>
                          );
                      })}
                    </tbody>

                </table>
              </div>

            </div>
          </div>
        </section>
      </div>
    </div>
  );
}
