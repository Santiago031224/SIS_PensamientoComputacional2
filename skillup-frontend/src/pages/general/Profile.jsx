import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../services/AuthContext.jsx';
import '../../styles/profile.css';
import { UsersApi } from '../../services/users';

export default function Profile() {
  const [isSaving, setIsSaving] = useState(false);
  const navigate = useNavigate();
  const { user, logout, updateUser } = useAuth();
  const [passwordForm, setPasswordForm] = useState({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
});

const [passwordMessage, setPasswordMessage] = useState('');

  const [activeTab, setActiveTab] = useState('personal');
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({
    name: user?.name || '',
    lastName: user?.lastName || '',
    email: user?.email || '',
    phone: '',
    bio: ''
  });

  if (!user) {
    return (
      <div className="profile-container">
        <div className="loading-state">
          <div className="spinner"></div>
          <p>Cargando información del usuario...</p>
        </div>
      </div>
    );
  }

  function handleLogout() {
    logout();
    navigate('/login');
  }

  const toggleTheme = (selectedTheme) => {
  setTheme(selectedTheme);
  localStorage.setItem('theme', selectedTheme);

  document.body.classList.remove('theme-light', 'theme-dark', 'theme-auto');
  document.body.classList.add(`theme-${selectedTheme}`);
};


  function handleEdit() {
    setIsEditing(true);
    setEditForm({
      name: user.name,
      lastName: user.lastName,
      email: user.email,
      phone: '',
      bio: ''
    });
  }

  const handleSave = async () => {
  if (isSaving) return;
  
  setIsSaving(true);
  try {
    const updateData = {
      name: editForm.name,
      lastName: editForm.lastName,
      email: editForm.email
    };

    const updatedUser = await UsersApi.update(user.id, updateData);

    if (updateUser) {
      updateUser(updatedUser);
    }
    setIsEditing(false);
    
    alert('Perfil actualizado correctamente ✅');
    
  } catch (error) {
    console.error('Error al actualizar perfil:', error);
    
    let errorMessage = 'Error al actualizar perfil';
    if (error.response?.data?.message) {
      errorMessage = error.response.data.message;
    } else if (error.message) {
      errorMessage = error.message;
    }
    
    alert(errorMessage);
  } finally {
    setIsSaving(false);
  }

  window.location.reload();
};

  function handleCancel() {
    setIsEditing(false);
    setEditForm({
      name: user.name,
      lastName: user.lastName,
      email: user.email,
      phone: '',
      bio: ''
    });
  }

  const getUserInitials = () => {
    return `${user.name?.charAt(0) || ''}${user.lastName?.charAt(0) || ''}`.toUpperCase() || 'U';
  };

const handleChangePassword = async () => {
  const { currentPassword, newPassword, confirmPassword } = passwordForm;

  // Validaciones frontend
  if (!currentPassword || !newPassword || !confirmPassword) {
    setPasswordMessage('❌ Todos los campos son obligatorios.');
    return;
  }

  if (newPassword !== confirmPassword) {
    setPasswordMessage('❌ La nueva contraseña y la confirmación no coinciden.');
    return;
  }

  if (newPassword.length < 6) {
    setPasswordMessage('❌ La contraseña debe tener al menos 6 caracteres.');
    return;
  }

  try {
    setPasswordMessage('🔄 Cambiando contraseña...');
    
    // Llamar al servicio para cambiar la contraseña
    await UsersApi.changePassword(user.id, {
      currentPassword,
      newPassword
    });

    // Éxito
    setPasswordMessage('✅ Contraseña actualizada correctamente');
    setPasswordForm({ 
      currentPassword: '', 
      newPassword: '', 
      confirmPassword: '' 
    });

    // Opcional: Cerrar sesión después de cambiar contraseña
    setTimeout(() => {
      alert('Por seguridad, se cerrará la sesión. Inicia sesión con tu nueva contraseña.');
      logout();
      navigate('/login');
    }, 2000);

  } catch (error) {
    console.error('Error al cambiar contraseña:', error);
    
    let errorMessage = '❌ Error al cambiar contraseña';
    if (error.response?.data?.message) {
      errorMessage = `❌ ${error.response.data.message}`;
    } else if (error.message) {
      errorMessage = `❌ ${error.message}`;
    }
    
    setPasswordMessage(errorMessage);
  }
};


  // Función para obtener el rol principal
  const getMainRole = () => {
    const roles = user.roles || [];
    if (roles.some(r => r.name === 'ADMIN')) return { name: 'Administrador', color: 'role-admin', icon: 'bi-shield-check' };
    if (roles.some(r => r.name === 'PROFESSOR')) return { name: 'Profesor', color: 'role-professor', icon: 'bi-person-badge' };
    if (roles.some(r => r.name === 'STUDENT')) return { name: 'Estudiante', color: 'role-student', icon: 'bi-mortarboard' };
    return { name: 'Usuario', color: 'role-default', icon: 'bi-person' };
  };

  const mainRole = getMainRole();

  return (
    <div className="profile-container">
      {/* Header del Perfil */}
      <div className="profile-header">
        <div className="header-content">
          <h1 className="page-title">
            <i className="bi bi-person-circle"></i>
            Mi Perfil
          </h1>
          <p className="page-subtitle">Gestiona tu información personal y preferencias</p>
        </div>
      </div>

      <div className="profile-content">
        {/* Sidebar de Navegación */}
        <div className="profile-sidebar">
          <div className="user-card">
            <div className="user-avatar-large">
              {getUserInitials()}
            </div>
            <div className="user-info">
              <h3 className="user-name">{user.name} {user.lastName}</h3>
              <div className="roles-list">
                        {(user.roles || []).map(role => (
                          <span key={role.id} className={`role-badge ${mainRole.color}`}>
                            {role.name}
                          </span>
                        ))}
              </div>
              <div className="user-email">{user.email}</div>
            </div>
          </div>

          <nav className="profile-nav">
            <button 
              className={`nav-item ${activeTab === 'personal' ? 'active' : ''}`}
              onClick={() => setActiveTab('personal')}
            >
              <i className="bi bi-person"></i>
              Información Personal
            </button>
            <button 
              className={`nav-item ${activeTab === 'security' ? 'active' : ''}`}
              onClick={() => setActiveTab('security')}
            >
              <i className="bi bi-shield-lock"></i>
              Seguridad
            </button>
              
          </nav>

          <div className="sidebar-footer">
            <button className="btn-logout" onClick={handleLogout}>
              <i className="bi bi-box-arrow-right"></i>
              Cerrar Sesión
            </button>
          </div>
        </div>

        {/* Contenido Principal */}
        <div className="profile-main">
          {activeTab === 'personal' && (
            <div className="tab-content">
              <div className="tab-header">
                <h2>
                  <i className="bi bi-person"></i>
                  Información Personal
                </h2>
                {!isEditing && (
                  <button className="btn-edit" onClick={handleEdit}>
                    <i className="bi bi-pencil"></i>
                    Editar Perfil
                  </button>
                )}
              </div>

              {isEditing ? (
                <div className="edit-form">
                  <div className="form-grid">
                    <div className="form-group">
                      <label className="form-label">Nombre</label>
                      <input
                        type="text"
                        className="form-input"
                        value={editForm.name}
                        onChange={(e) => setEditForm({...editForm, name: e.target.value})}
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Apellido</label>
                      <input
                        type="text"
                        className="form-input"
                        value={editForm.lastName}
                        onChange={(e) => setEditForm({...editForm, lastName: e.target.value})}
                      />
                    </div>
                    <div className="form-group full-width">
                      <label className="form-label">Email</label>
                      <input
                        type="email"
                        className="form-input"
                        value={editForm.email}
                        onChange={(e) => setEditForm({...editForm, email: e.target.value})}
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Teléfono</label>
                      <input
                        type="tel"
                        className="form-input"
                        value={editForm.phone}
                        onChange={(e) => setEditForm({...editForm, phone: e.target.value})}
                        placeholder="+57 300 123 4567"
                      />
                    </div>
                    <div className="form-group full-width">
                      <label className="form-label">Biografía</label>
                      <textarea
                        className="form-textarea"
                        value={editForm.bio}
                        onChange={(e) => setEditForm({...editForm, bio: e.target.value})}
                        placeholder="Cuéntanos algo sobre ti..."
                        rows="4"
                      />
                    </div>
                  </div>
                  <div className="form-actions">
                    <button className="btn btn-cancel" onClick={handleCancel}>
                      Cancelar
                    </button>
                    <button className="btn btn-save" onClick={handleSave}>
                      <i className="bi bi-check-lg"></i>
                      Guardar Cambios
                    </button>
                  </div>
                </div>
              ) : (
                <div className="info-grid">
                  <div className="info-card">
                    <div className="info-icon">
                      <i className="bi bi-person-badge"></i>
                    </div>
                    <div className="info-content">
                      <label>Nombre Completo</label>
                      <p>{user.name} {user.lastName}</p>
                    </div>
                  </div>

                  <div className="info-card">
                    <div className="info-icon">
                      <i className="bi bi-envelope"></i>
                    </div>
                    <div className="info-content">
                      <label>Email</label>
                      <p>{user.email}</p>
                    </div>
                  </div>

                  <div className="info-card">
                    <div className="info-icon">
                      <i className="bi bi-shield-check"></i>
                    </div>
                    <div className="info-content">
                      <label>Roles del Sistema</label>
                      <div className="roles-list">
                        {(user.roles || []).map(role => (
                          <span key={role.id} className={`role-badge ${mainRole.color}`}>
                            {role.name}
                          </span>
                        ))}
                      </div>
                    </div>
                  </div>

                  <div className="info-card">
                    <div className="info-icon">
                      <i className="bi bi-calendar"></i>
                    </div>
                    <div className="info-content">
                      <label>Miembro desde</label>
                      <p>
                        {user.createdAt 
                          ? new Date(user.createdAt).toLocaleDateString('es-CO', {
                              year: 'numeric',
                              month: 'long',
                              day: 'numeric'
                            })
                          : 'Sin fecha'}
                      </p>

                    </div>
                  </div>

                  <div className="info-card">
                    <div className="info-icon">
                      <i className="bi bi-clock-history"></i>
                    </div>
                    <div className="info-content">
                      <label>Último acceso</label>
                        <p>
                          {user.lastLogin
                            ? new Date(user.lastLogin).toLocaleString('es-CO', {
                                year: 'numeric',
                                month: 'long',
                                day: 'numeric',
                                hour: '2-digit',
                                minute: '2-digit'
                              })
                            : 'Sin registro'}
                        </p>
                    </div>
                  </div>

                  <div className="info-card">
                    <div className="info-icon">
                      <i className="bi bi-bell"></i>
                    </div>
                    <div className="info-content">
                      <label>Estado de la cuenta</label>
                      <p className="status-active">
                        <i className="bi bi-check-circle"></i>
                        Activa
                      </p>
                    </div>
                  </div>
                </div>
              )}
            </div>
          )}

          {activeTab === 'security' && (
  <div className="tab-content">
    <div className="tab-header">
      <h2>
        <i className="bi bi-shield-lock"></i>
        Seguridad y Privacidad
      </h2>
    </div>

    <div className="security-grid">
      <div className="security-card">
        <div className="security-icon password">
          <i className="bi bi-key"></i>
        </div>
        <div className="security-content">
          <h4>Cambiar Contraseña</h4>
          <p>Actualiza tu contraseña regularmente para mantener tu cuenta segura</p>
          <div className="change-password-form">
            <div className="form-group">
              <label>Contraseña Actual</label>
              <input
                type="password"
                className="form-input"
                placeholder="Ingresa tu contraseña actual"
                value={passwordForm.currentPassword}
                onChange={(e) => setPasswordForm({ ...passwordForm, currentPassword: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label>Nueva Contraseña</label>
              <input
                type="password"
                className="form-input"
                placeholder="Mínimo 6 caracteres"
                value={passwordForm.newPassword}
                onChange={(e) => setPasswordForm({ ...passwordForm, newPassword: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label>Confirmar Nueva Contraseña</label>
              <input
                type="password"
                className="form-input"
                placeholder="Repite la nueva contraseña"
                value={passwordForm.confirmPassword}
                onChange={(e) => setPasswordForm({ ...passwordForm, confirmPassword: e.target.value })}
              />
            </div>
            <button 
              className="btn btn-primary" 
              onClick={handleChangePassword}
              disabled={!passwordForm.currentPassword || !passwordForm.newPassword || !passwordForm.confirmPassword}
            >
              <i className="bi bi-key"></i>
              Cambiar Contraseña
            </button>
            {passwordMessage && (
              <div className={`password-message ${
                passwordMessage.includes('✅') ? 'success' : 
                passwordMessage.includes('🔄') ? 'loading' : 'error'
              }`}>
                {passwordMessage}
              </div>
            )}
          </div>
        </div>
      </div>

              </div>
            </div>
          )}

          {activeTab === 'preferences' && (
            <div className="tab-content">
              <div className="tab-header">
                <h2>
                  <i className="bi bi-gear"></i>
                  Preferencias
                </h2>
              </div>

              <div className="preferences-content">
                <div className="preference-section">
                  <h4>Notificaciones</h4>
                  <div className="preference-item">
                    <div className="preference-info">
                      <h5>Notificaciones por email</h5>
                      <p>Recibe notificaciones importantes por correo electrónico</p>
                    </div>
                    <label className="toggle-switch">
                      <input type="checkbox" defaultChecked />
                      <span className="toggle-slider"></span>
                    </label>
                  </div>

                  <div className="preference-item">
                    <div className="preference-info">
                      <h5>Recordatorios de actividad</h5>
                      <p>Notificaciones sobre actividades pendientes</p>
                    </div>
                    <label className="toggle-switch">
                      <input type="checkbox" defaultChecked />
                      <span className="toggle-slider"></span>
                    </label>
                  </div>
                </div>

                <div className="preference-section">
                  
                </div>
              </div>
            </div>
          )}

          </div>
      </div>
    </div>
  );
}