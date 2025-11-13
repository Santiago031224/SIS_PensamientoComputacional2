import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../services/AuthContext.jsx';
import '../styles/navbar.css';

export default function Navbar() {
  const { user, setToken } = useAuth();
  const navigate = useNavigate();

  function logout() {
    setToken(null);
    navigate('/login');
  }

  return (
    <nav className="navbar navbar-expand-lg app-navbar shadow-sm">
      <div className="container">
        <Link className="navbar-brand fw-semibold" to="/">Sistema de Gestión Académica</Link>
        <div className="d-flex align-items-center gap-3">
          <span className="small d-none d-md-inline text-white-50">
            {user ? `${user.name} ${user.lastName}` : 'Invitado'}
          </span>
          <button type="button" className="btn btn-outline-light btn-sm" onClick={logout}>Cerrar sesión</button>
        </div>
      </div>
    </nav>
  );
}
