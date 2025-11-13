import React, { useEffect, useState } from 'react';
import { useAuth } from '../../services/AuthContext.jsx';
import Navbar from '../../components/Navbar.jsx';

export default function Landing(){
  const { token, api } = useAuth();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function load(){
      try {
        const res = await api.get('/users');
        setUsers(res.data);
      } catch (e){
        setError('Error cargando usuarios');
      } finally { setLoading(false); }
    }
    if (token) load();
  }, [token, api]);

  return (
    <div>
      <Navbar />
      <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h4>Panel Inicial</h4>
        <span className="badge bg-primary">Token activo</span>
      </div>
      {loading && <p>Cargando...</p>}
      {error && <div className="alert alert-danger">{error}</div>}
      {!loading && !error && (
        <table className="table table-sm table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Email</th>
              <th>Estado</th>
              <th>Roles</th>
            </tr>
          </thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.name} {u.lastName}</td>
                <td>{u.email}</td>
                <td>{u.status}</td>
                <td>{(u.roles||[]).map(r => r.name).join(', ') || '—'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      </div>
    </div>
  );
}
