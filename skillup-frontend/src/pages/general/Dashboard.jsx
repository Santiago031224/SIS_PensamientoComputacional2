import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../services/AuthContext.jsx';
import '../../styles/dashboard.css';

export default function Dashboard() {
  const { user } = useAuth();
  const roles = (user?.roles || []).map(role => (role.name || '').toUpperCase());

  const shortcuts = [];
  if (roles.some(role => ['STUDENT', 'ROLE_STUDENT', 'ESTUDIANTE', 'ROLE_ESTUDIANTE'].includes(role))) {
    shortcuts.push({ 
      title: 'Mis Actividades', 
      description: 'Lista de actividades asignadas y módulos de práctica.', 
      to: '/student/dashboard',
      icon: 'bi-journal-check',
      color: 'primary'
    });
    shortcuts.push({ 
      title: 'Mis Logros', 
      description: 'Revisa insignias y objetivos completados.', 
      to: '/student/achievements',
      icon: 'bi-trophy',
      color: 'success'
    });
  }
  if (roles.some(role => ['PROFESSOR', 'ROLE_PROFESSOR', 'TEACHER', 'PROFESOR', 'ROLE_PROFESOR'].includes(role))) {
    shortcuts.push({ 
      title: 'Panel del Profesor', 
      description: 'Seguimiento del desempeño por grupo.', 
      to: '/professor/dashboard',
      icon: 'bi-graph-up',
      color: 'info'
    });
    shortcuts.push({ 
      title: 'Gestionar Actividades', 
      description: 'Crear y programar nuevas actividades.', 
      to: '/professor/assignments',
      icon: 'bi-journal-plus',
      color: 'warning'
    });
  }
  if (roles.some(role => ['ADMIN', 'ROLE_ADMIN', 'ADMINISTRATOR', 'ADMINISTRADOR', 'ROLE_ADMINISTRADOR'].includes(role))) {
    shortcuts.push({ 
      title: 'Panel Administrativo', 
      description: 'Métricas generales del sistema.', 
      to: '/admin/dashboard',
      icon: 'bi-speedometer2',
      color: 'danger'
    });
    shortcuts.push({ 
      title: 'Gestionar Usuarios', 
      description: 'Administra roles y accesos.', 
      to: '/admin/users',
      icon: 'bi-people',
      color: 'secondary'
    });
  }

  // Función para obtener el saludo según la hora
  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Buenos días';
    if (hour < 18) return 'Buenas tardes';
    return 'Buenas noches';
  };

  return (
    <div className="dashboard-container">
      {/* Header */}
      <div className="dashboard-header">
        <div className="header-content">
          <div className="header-text">
            <h1 className="page-title">
              <i className="bi bi-house-door"></i>
              Tablero Principal
            </h1>
            <p className="page-subtitle">Selecciona la vista que necesites según tu rol</p>
          </div>
          <div className="user-greeting">
            <div className="greeting-text">{getGreeting()},</div>
            <div className="user-name">{user ? `${user.name || ''} ${user.lastName || ''}` : 'Usuario no cargado'}</div>
          </div>
        </div>
      </div>

      {/* Contenido Principal */}
      <div className="dashboard-content">
        {shortcuts.length > 0 ? (
          <div className="shortcuts-grid">
            {shortcuts.map(item => (
              <Link key={item.to} to={item.to} className="shortcut-card">
                <div className={`card-icon ${item.color}`}>
                  <i className={`bi ${item.icon}`}></i>
                </div>
                <div className="card-content">
                  <h3 className="card-title">{item.title}</h3>
                  <p className="card-description">{item.description}</p>
                </div>
                <div className="card-arrow">
                  <i className="bi bi-arrow-right"></i>
                </div>
              </Link>
            ))}
          </div>
        ) : (
          <div className="empty-state">
            <div className="empty-icon">
              <i className="bi bi-person-x"></i>
            </div>
            <h3>Sin roles asignados</h3>
            <p>Tu cuenta aún no tiene roles asignados. Solicita acceso al administrador.</p>
          </div>
        )}
      </div>
    </div>
  );
}