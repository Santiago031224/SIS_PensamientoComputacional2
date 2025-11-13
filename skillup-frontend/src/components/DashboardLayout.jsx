import React from 'react';
import { NavLink, Outlet, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../services/AuthContext.jsx';
import { isAdmin, isProfessor, isStudent } from '../services/roles.js';
import '../styles/layout.css'; 

function linkClassName(isActive) {
  return `app-nav-link${isActive ? ' active' : ''}`;
}

export default function DashboardLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();
  const roles = user?.roles || [];

  // Avoid noisy runtime logging in the layout
  // console.log('📍 Current route:', location.pathname);

  const isAdminRole = isAdmin(roles);
  const isProfessorRole = isProfessor(roles);
  const isStudentRole = isStudent(roles);
  function handleLogout() {
    logout();
    navigate('/login');
  }

  // Función para obtener iniciales del usuario
  const getUserInitials = () => {
    if (!user) return 'U';
    return `${user.name?.charAt(0) || ''}${user.lastName?.charAt(0) || ''}`.toUpperCase() || 'U';
  };

  // Función para obtener el rol principal con estilo
  const getMainRole = () => {
    if (isAdminRole) return { text: 'Administrador', color: 'text-danger', icon: 'bi-shield-check' };
    if (isProfessorRole) return { text: 'Profesor', color: 'text-primary', icon: 'bi-person-badge' };
    if (isStudentRole) return { text: 'Estudiante', color: 'text-success', icon: 'bi-mortarboard' };
    return { text: 'Usuario', color: 'text-secondary', icon: 'bi-person' };
  };

  const mainRole = getMainRole();

  return (
    <div className="app-shell d-flex">
      <aside className="app-sidebar d-flex flex-column p-4">
      

        {/* Información del Usuario */}
        <div className="user-card text-center p-3 mb-4 rounded-3">
          <div className="user-avatar mx-auto mb-2">
            {getUserInitials()}
          </div>
          <div className="user-info">
            <h6 className="user-name mb-1">{user ? `${user.name} ${user.lastName}` : 'Invitado'}</h6>
            <div className={`user-role small ${mainRole.color}`}>
              <i className={`${mainRole.icon} me-1`}></i>
              {mainRole.text}
            </div>
          </div>
        </div>

        {/* Navegación */}
        <nav className="sidebar-nav flex-grow-1">
          <div className="nav-section">
            <NavLink to="/" end className={({ isActive }) => linkClassName(isActive)}>
              <i className="bi bi-house me-2"></i>
              Inicio
            </NavLink>
          </div>

          {isStudentRole && (
            <div className="nav-section">
              <div className="app-nav-heading">
                <i className="bi bi-mortarboard me-2"></i>
                Estudiante
              </div>
              <NavLink to="/student/dashboard" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-speedometer2 me-2"></i>
                Actividades
              </NavLink>
              <NavLink to="/student/achievements" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-trophy me-2"></i>
                Logros
              </NavLink>
              <NavLink to="/student/leaderboard" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-bar-chart me-2"></i>
                Podio
              </NavLink>
            </div>
          )}

          {isProfessorRole && (
            <div className="nav-section">
              <div className="app-nav-heading">
                <i className="bi bi-person-badge me-2"></i>
                Profesor
              </div>
              <NavLink to="/professor/dashboard" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-graph-up me-2"></i>
                Desempeño
              </NavLink>
              <NavLink to="/professor/exercises" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-code-square me-2"></i>
                Ejercicios
              </NavLink>
              <NavLink to="/professor/assignments" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-journal-text me-2"></i>
                Asignaciones
              </NavLink>
              <NavLink to="/professor/grading" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-check-square me-2"></i>
                Calificaciones
              </NavLink>
              <NavLink to="/professor/codes" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-qr-code me-2"></i>
                Códigos QR
              </NavLink>
              
            </div>
          )}

          {isAdminRole && (
            <div className="nav-section">
              
              <NavLink to="/admin/dashboard" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-eye me-2"></i>
                Visión General
              </NavLink>
              <NavLink to="/admin/users" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-people me-2"></i>
                Usuarios
              </NavLink>
              <NavLink to="/admin/reports" className={({ isActive }) => linkClassName(isActive)}>
                <i className="bi bi-pie-chart me-2"></i>
                Diagramas
              </NavLink>
            </div>
          )}
        </nav>

        {/* Footer del Sidebar */}
        <div className="sidebar-footer mt-auto pt-3 border-top">
          <NavLink to="/profile" className={({ isActive }) => linkClassName(isActive)}>
            <i className="bi bi-person-circle me-2"></i>
            Mi Perfil
          </NavLink>
          <button 
            type="button" 
            className="btn btn-logout w-100 mt-3 d-flex align-items-center justify-content-center"
            onClick={handleLogout}
          >
            <i className="bi bi-box-arrow-right me-2"></i>
            Cerrar Sesión
          </button>
        </div>
      </aside>

      {/* Área Principal */}
      <main className="app-main flex-fill">
        <Outlet />
      </main>
    </div>
  );
}