import React, { useEffect, useState } from 'react';
import { UsersApi } from '../../services/users';
import { RolesApi } from '../../services/roles';
import { ProfessorsApi } from '../../services/professors';
import { StudentsApi } from '../../services/students';
import '../../styles/adminUser.css';

export default function AdminUsers(){
  const [users, setUsers] = useState([]);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [q, setQ] = useState('');
  const [editingUser, setEditingUser] = useState(null);
  const [showEditModal, setShowEditModal] = useState(false);

  const [showCreateProfessorModal, setShowCreateProfessorModal] = useState(false);
  const [newUser, setNewUser] = useState({
    name: '',
    lastName: '',
    email: '',
    password: '',
    documentType: '',
    document: '',
    role: 'PROFESSOR', // rol por defecto
    // Campos de Student
    code: '',
    gpa: '',
    levelId: '',
    // Campos de Professor
    officeLocation: '',
    specialtyArea: '',
    academicRank: '',
    maxGroupsAllowed: '',
  });


  useEffect(() => {
    loadUsers();
    loadRoles();
  }, []);

  async function loadUsers(){
    setLoading(true);
    try{
      const list = await UsersApi.list();
      setUsers(list || []);
    }catch(error){
      console.error('Failed to fetch users', error);
      setUsers([]);
    }finally{
      setLoading(false);
    }
  }

const handleRegisterUser = async () => {
  try {
    // Validación básica
    if (!newUser.name || !newUser.lastName || !newUser.email || !newUser.password || !newUser.role) {
      alert('Por favor complete todos los campos obligatorios');
      return;
    }

    // Paso 1: Buscar el rol seleccionado
    const roleObj = roles.find(r => r.name === newUser.role);
    if (!roleObj) {
      alert('Rol no válido');
      return;
    }

    // Paso 2: Crear el usuario con su rol
    const userPayload = {
      name: newUser.name,
      lastName: newUser.lastName,
      email: newUser.email,
      password: newUser.password || '123456',
      documentType: newUser.documentType || 'CC',
      document: newUser.document || '0000000000',
      roleIds: [roleObj.id], // ✅ ESTO ES CRÍTICO - asignar el rol
    };

    const createdUser = await UsersApi.create(userPayload);

    // Paso 3: Si es STUDENT/ESTUDIANTE, crear el registro de estudiante
    if (newUser.role.toUpperCase() === 'STUDENT' || newUser.role.toUpperCase() === 'ESTUDIANTE') {
      if (!newUser.code) {
        alert('El código del estudiante es obligatorio');
        return;
      }

      const studentPayload = {
        code: newUser.code,
        gpa: parseFloat(newUser.gpa) || 0.0,
        userId: createdUser.id,
        levelId: newUser.levelId ? parseInt(newUser.levelId) : null,
      };

      const createdStudent = await StudentsApi.create(studentPayload);
    }

    // Paso 4: Si es PROFESSOR/PROFESOR, crear el registro de profesor
    if (newUser.role.toUpperCase() === 'PROFESSOR' || newUser.role.toUpperCase() === 'PROFESOR') {
      const professorPayload = {
        userId: createdUser.id,
        officeLocation: newUser.officeLocation || null,
        specialtyArea: newUser.specialtyArea || null,
        academicRank: newUser.academicRank || null,
        registrationDate: new Date().toISOString().split('T')[0], // Fecha actual
        maxGroupsAllowed: newUser.maxGroupsAllowed ? parseInt(newUser.maxGroupsAllowed) : null,
        ratingAverage: null, // Se calcula posteriormente
      };

      const createdProfessor = await ProfessorsApi.create(professorPayload);
    }

    // Paso 5: Si es ADMIN (o ADMINISTRATOR), crear el registro de administrador
    if (newUser.role.toUpperCase() === 'ADMIN' || newUser.role.toUpperCase() === 'ADMINISTRATOR') {
      const administratorPayload = {
        userId: createdUser.id,
        department: newUser.department || null,
      };

      const createdAdministrator = await AdministratorsApi.create(administratorPayload);
    }

    // Paso 6: Reset y cierre
    setShowCreateProfessorModal(false);
    setNewUser({
      name: '',
      lastName: '',
      email: '',
      password: '',
      documentType: '',
      document: '',
      role: 'PROFESSOR',
      // Campos de Student
      code: '',
      gpa: '',
      levelId: '',
      // Campos de Professor
      officeLocation: '',
      specialtyArea: '',
      academicRank: '',
      maxGroupsAllowed: '',
    });

    loadUsers();
    alert('✅ Usuario creado correctamente con todos sus datos');
  } catch (error) {
    console.error('❌ Error al crear usuario:', error);
    alert('Error al crear usuario: ' + (error.response?.data?.message || error.message));
  }
};



  async function loadRoles(){
    try {
      const list = await RolesApi.list();
      setRoles(list || []);
    } catch (error) {
      console.error('Failed to fetch roles', error);
      setRoles([]);
    }
  }

  const handleEditUser = (user) => {
    setEditingUser({ ...user });
    setShowEditModal(true);
  };

  const userHasRole = (roleName) => {
    if (!editingUser?.roles) return false;
    return editingUser.roles.some(r => r.name === roleName);
  };

  const handleToggleRole = async (role) => {
    if (!editingUser) return;
    try {
      const existing = userHasRole(role.name);
      if (existing) {
        await UsersApi.removeRole(editingUser.id, role.id);
        setEditingUser(prev => ({
          ...prev,
          roles: (prev.roles || []).filter(r => r.id !== role.id)
        }));
      } else {
        const updated = await UsersApi.addRole(editingUser.id, role.id);

        if (updated && updated.roles) {
          setEditingUser(updated);
        } else {
          setEditingUser(prev => ({
            ...prev,
            roles: [...(prev.roles || []), role]
          }));
        }
      }
    } catch (error) {
      console.error('Failed to toggle role', error);
      alert('Error al cambiar rol: ' + (error.response?.data || error.message));
    }
  };

    const handleSaveUser = async () => {
    if (!editingUser) return;
    try {
      // Enviar todos los campos requeridos por el backend
      const updateData = {
        name: editingUser.name,
        lastName: editingUser.lastName,
        email: editingUser.email,
        documentType: editingUser.documentType || 'CC',
        document: editingUser.document || '0000000000',
        // Incluir otros campos que el DTO pueda requerir
        password: 'dummyPassword', // o mantener la contraseña actual
      };

      await UsersApi.update(editingUser.id, updateData);
      setShowEditModal(false);
      setEditingUser(null);
      loadUsers();
      alert('Usuario actualizado correctamente');
    } catch (error) {
      console.error('Failed to update user', error);
      alert('Error al actualizar usuario: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleUpdateUserStatus = async (userId, newStatus) => {
  const statusLabel = newStatus === 1 ? 'activar' : 'suspender';
  if (!window.confirm(`¿Está seguro de ${statusLabel} este usuario?`)) return;
  try {
    await UsersApi.updateStatus(userId, newStatus);
    loadUsers();
    alert('Estado actualizado correctamente');
  } catch (error) {
    console.error('Failed to update user status', error);
    alert('Error al actualizar estado: ' + (error.response?.data?.message || error.message));
  }
};


  const handleDeleteUser = async (userId) => {
    if (!window.confirm('¿Está seguro de eliminar este usuario? Esta acción no se puede deshacer.')) return;
    try {
      await UsersApi.delete(userId);
      loadUsers();
      alert('Usuario eliminado correctamente');
    } catch (error) {
      console.error('Failed to delete user', error);
      alert('Error al eliminar usuario: ' + (error.response?.data?.message || error.message));
    }
  };

  // Función para manejar el cambio de rol y limpiar campos específicos
  const handleRoleChange = (newRole) => {
    setNewUser(prev => ({
      // Mantener campos comunes
      name: prev.name,
      lastName: prev.lastName,
      email: prev.email,
      password: prev.password,
      documentType: prev.documentType,
      document: prev.document,
      role: newRole,
      // Limpiar TODOS los campos específicos de roles
      code: '',
      gpa: '',
      levelId: '',
      officeLocation: '',
      specialtyArea: '',
      academicRank: '',
      maxGroupsAllowed: '',
    }));
  };

  const filtered = users.filter(user => {
    if (!q) return true;
    const text = `${user.name || ''} ${user.lastName || ''} ${user.email || ''}`.toLowerCase();
    return text.includes(q.toLowerCase());
  });

  // Función para obtener clase del badge según el rol
  const getRoleBadgeClass = (roleName) => {
    switch(roleName?.toUpperCase()) {
      case 'ADMIN': return 'bg-danger';
      case 'PROFESSOR': return 'bg-primary';
      case 'STUDENT': return 'bg-success';
      case 'EDITOR': return 'bg-warning text-dark';
      default: return 'bg-secondary';
    }
  };

  return (
    <div className="container-fluid">
      {/* Estilos para animaciones */}
      <style>{`
        .animate__animated {
          animation-duration: 0.4s;
          animation-fill-mode: both;
        }
        .animate__fadeIn {
          animation-name: fadeIn;
        }
        @keyframes fadeIn {
          from {
            opacity: 0;
            transform: translateY(-10px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
      `}</style>

      {/* Header Mejorado */}
      <div className="d-flex flex-wrap justify-content-between align-items-center mb-4 p-4 bg-light rounded-3 shadow-sm">
        <div>
          <h2 className="mb-1 text-gradient">👨‍💼 Administración de Usuarios</h2>
          <p className="text-muted mb-0">Gestiona roles, estados y accesos del sistema</p>
        </div>
        <div className="d-flex gap-2 align-items-center">
          <button 
            type="button" 
            className="btn btn-success btn-gradient d-flex align-items-center gap-2 shadow"
            onClick={() => setShowCreateProfessorModal(true)}
          >
            <i className="bi bi-person-plus"></i>
            Crear usuario
          </button>
          <div className="input-group search-box" style={{ maxWidth: 300 }}>
            <span className="input-group-text bg-white border-end-0">
              <i className="bi bi-search text-muted"></i>
            </span>
            <input
              type="search"
              className="form-control border-start-0 shadow-sm"
              value={q}
              onChange={event => setQ(event.target.value)}
              placeholder="Buscar por nombre o correo..."
            />
          </div>
        </div>
      </div>

      {/* Loading State */}
      {loading && (
        <div className="alert alert-info d-flex align-items-center gap-2">
          <div className="spinner-border spinner-border-sm" role="status"></div>
          <span>Cargando usuarios...</span>
        </div>
      )}

      {/* Tabla de Usuarios */}
      <div className="card shadow-sm border-0">
        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table table-hover mb-0">
              <thead className="table-light">
                <tr>
                  <th scope="col" className="ps-4">Usuario</th>
                  <th scope="col">Correo</th>
                  <th scope="col">Roles</th>
                  <th scope="col">Estado</th>
                  <th scope="col" className="text-end pe-4">Acciones</th>
                </tr>
              </thead>
              <tbody>
              {filtered.map(user => {
                const isActive = user.status === 1; 

                return (
                  <tr key={user.id} className="user-row">
                    <td className="ps-4">
                      <div className="d-flex align-items-center">
                        <div className="avatar-circle bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-3">
                          {user.name?.charAt(0)}{user.lastName?.charAt(0)}
                        </div>
                        <div>
                          <strong>{`${user.name || ''} ${user.lastName || ''}`}</strong>
                        </div>
                      </div>
                    </td>
                    <td>
                      <span className="text-muted">{user.email}</span>
                    </td>
                    <td>
                      <div className="d-flex flex-wrap gap-1">
                        {(user.roles || []).map(role => (
                          <span 
                            key={role.id || role.name} 
                            className={`badge ${getRoleBadgeClass(role.name)} badge-pill`}
                          >
                            {role.name}
                          </span>
                        ))}
                      </div>
                    </td>
                    <td>
                      {isActive ? (
                        <span className="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25">
                          <i className="bi bi-check-circle me-1"></i>
                          Activo
                        </span>
                      ) : (
                        <span className="badge bg-danger bg-opacity-10 text-danger border border-danger border-opacity-25">
                          <i className="bi bi-pause-circle me-1"></i>
                          Suspendido
                        </span>
                      )}
                    </td>
                    <td className="text-end pe-4">
                      <div className="btn-group" role="group">
                        <button 
                          type="button" 
                          className="btn btn-outline-primary btn-sm d-flex align-items-center gap-1"
                          onClick={() => handleEditUser(user)}
                        >
                          <i className="bi bi-pencil"></i>
                          Editar
                        </button>

                        {isActive ? (
                          <button 
                            type="button" 
                            className="btn btn-outline-warning btn-sm d-flex align-items-center gap-1"
                            onClick={() => handleUpdateUserStatus(user.id, 0)}
                          >
                            <i className="bi bi-pause"></i>
                            Suspender
                          </button>
                        ) : (
                          <button 
                            type="button" 
                            className="btn btn-outline-success btn-sm d-flex align-items-center gap-1"
                            onClick={() => handleUpdateUserStatus(user.id, 1)}
                          >
                            <i className="bi bi-play"></i>
                            Activar
                          </button>
                        )}

                        <button 
                          type="button" 
                          className="btn btn-outline-danger btn-sm d-flex align-items-center gap-1"
                          onClick={() => handleDeleteUser(user.id)}
                        >
                          <i className="bi bi-trash"></i>
                          Eliminar
                        </button>
                      </div>
                    </td>
                  </tr>
                );
              })}

                {!loading && filtered.length === 0 && (
                  <tr>
                    <td colSpan={5} className="text-center text-muted py-5">
                      <i className="bi bi-people display-4 d-block mb-2"></i>
                      No se encontraron usuarios
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Modal para editar usuario */}
      {showEditModal && editingUser && (
        <div className="modal show d-block fade" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }} tabIndex="-1">
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content border-0 shadow-lg">
              <div className="modal-header bg-primary text-white">
                <h5 className="modal-title">
                  <i className="bi bi-person-gear me-2"></i>
                  Editar Usuario
                </h5>
                <button type="button" className="btn-close btn-close-white" onClick={() => setShowEditModal(false)}></button>
              </div>
              <div className="modal-body">
                <div className="mb-4">
                  <label className="form-label fw-bold">Roles del Usuario</label>
                  <div className="d-flex flex-wrap gap-2">
                    {roles.map(role => {
                      const active = userHasRole(role.name);
                      return (
                        <button
                          key={role.id}
                          type="button"
                          onClick={() => handleToggleRole(role)}
                          className={`btn btn-sm ${active ? getRoleBadgeClass(role.name) : 'btn-outline-secondary'} d-flex align-items-center gap-1`}
                          title={active ? 'Quitar rol' : 'Agregar rol'}
                        >
                          {active ? <i className="bi bi-check-lg"></i> : <i className="bi bi-plus"></i>}
                          {role.name}
                        </button>
                      );
                    })}
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label fw-bold">Nombre</label>
                    <input
                      type="text"
                      className="form-control shadow-sm"
                      value={editingUser.name || ''}
                      onChange={(e) => setEditingUser({ ...editingUser, name: e.target.value })}
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label fw-bold">Apellido</label>
                    <input
                      type="text"
                      className="form-control shadow-sm"
                      value={editingUser.lastName || ''}
                      onChange={(e) => setEditingUser({ ...editingUser, lastName: e.target.value })}
                    />
                  </div>
                </div>
                <div className="mb-3">
                  <label className="form-label fw-bold">Email</label>
                  <input
                    type="email"
                    className="form-control shadow-sm"
                    value={editingUser.email || ''}
                    onChange={(e) => setEditingUser({ ...editingUser, email: e.target.value })}
                  />
                </div>
              </div>
              <div className="modal-footer border-top-0">
                <button type="button" className="btn btn-outline-secondary" onClick={() => setShowEditModal(false)}>
                  <i className="bi bi-x-lg me-1"></i>
                  Cancelar
                </button>
                <button type="button" className="btn btn-primary" onClick={handleSaveUser}>
                  <i className="bi bi-check-lg me-1"></i>
                  Guardar cambios
                </button>
              </div>
            </div>
          </div>
        </div>
      )}{/* Modal para registrar usuario */}
{showCreateProfessorModal && (
  <div className="modal show d-block fade" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }} tabIndex="-1">
    <div className="modal-dialog modal-dialog-centered modal-lg">
      <div className="modal-content border-0 shadow-lg">

        {/* Header del Modal */}
        <div className="modal-header bg-success text-white">
          <h5 className="modal-title">
            <i className="bi bi-person-plus me-2"></i>
            Registrar Nuevo Usuario
          </h5>
          <button type="button" className="btn-close btn-close-white" onClick={() => setShowCreateProfessorModal(false)}></button>
        </div>

        {/* Body del Modal */}
        <div className="modal-body">
          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label fw-bold">Nombre *</label>
              <input
                type="text"
                className="form-control shadow-sm"
                value={newUser.name}
                onChange={(e) => setNewUser({ ...newUser, name: e.target.value })}
                required
              />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label fw-bold">Apellido *</label>
              <input
                type="text"
                className="form-control shadow-sm"
                value={newUser.lastName}
                onChange={(e) => setNewUser({ ...newUser, lastName: e.target.value })}
                required
              />
            </div>
          </div>

          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label fw-bold">Tipo de Documento</label>
              <select
                className="form-select shadow-sm"
                value={newUser.documentType || ''}
                onChange={(e) => setNewUser({ ...newUser, documentType: e.target.value })}
              >
                <option value="">Seleccionar...</option>
                <option value="CC">Cédula de Ciudadanía</option>
                <option value="CE">Cédula de Extranjería</option>
                <option value="TI">Tarjeta de Identidad</option>
                <option value="PASSPORT">Pasaporte</option>
              </select>
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label fw-bold">Número de Documento</label>
              <input
                type="text"
                className="form-control shadow-sm"
                value={newUser.document || ''}
                onChange={(e) => setNewUser({ ...newUser, document: e.target.value })}
              />
            </div>
          </div>

          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label fw-bold">Email *</label>
              <input
                type="email"
                className="form-control shadow-sm"
                value={newUser.email}
                onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
                required
              />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label fw-bold">Contraseña *</label>
              <input
                type="password"
                className="form-control shadow-sm"
                value={newUser.password || ''}
                onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
                required
              />
            </div>
          </div>

          <div className="row">
            <div className="col-md-12 mb-4">
              <label className="form-label fw-bold">Rol *</label>
              <select
                className="form-select shadow-sm"
                value={newUser.role}
                onChange={(e) => handleRoleChange(e.target.value)}
              >
                <option value="">Seleccionar rol...</option>
                {roles.map(r => (
                  <option key={r.id} value={r.name}>{r.name}</option>
                ))}
              </select>
              <small className="text-muted">
                {newUser.role && `Los campos específicos para ${newUser.role} se mostrarán abajo`}
              </small>
            </div>
          </div>

          {/* Separador visual */}
          {newUser.role && (
            <div className="mb-3">
              <hr className="my-3" />
              <h6 className="text-primary mb-3">
                <i className="bi bi-info-circle me-2"></i>
                Información Específica de {newUser.role}
              </h6>
            </div>
          )}

          {/* Campos solo visibles si el rol es ADMIN/ADMINISTRATOR */}
          {(newUser.role?.toUpperCase() === 'ADMIN' || newUser.role?.toUpperCase() === 'ADMINISTRATOR') && (
            <div className="animate__animated animate__fadeIn">
              <div className="row">
                <div className="col-md-12 mb-3">
                  <label className="form-label fw-bold">Departamento</label>
                  <input
                    type="text"
                    className="form-control shadow-sm"
                    placeholder="Ej: Tecnologías de Información"
                    value={newUser.department || ''}
                    onChange={(e) => setNewUser({ ...newUser, department: e.target.value })}
                  />
                  <small className="text-muted">Opcional - puede completarlo después</small>
                </div>
              </div>
            </div>
          )}

          {/* Campos solo visibles si el rol es PROFESSOR/PROFESOR */}
          {(newUser.role?.toUpperCase() === 'PROFESSOR' || newUser.role?.toUpperCase() === 'PROFESOR') && (
            <div className="animate__animated animate__fadeIn">
              <div className="row">
                <div className="col-md-6 mb-3">
                  <label className="form-label fw-bold">Ubicación de Oficina</label>
                  <input
                    type="text"
                    className="form-control shadow-sm"
                    placeholder="Ej: Edificio H, Piso 3, Oficina 305"
                    value={newUser.officeLocation || ''}
                    onChange={(e) => setNewUser({ ...newUser, officeLocation: e.target.value })}
                  />
                  <small className="text-muted">Opcional - puede completarlo después</small>
                </div>
                <div className="col-md-6 mb-3">
                  <label className="form-label fw-bold">Área de Especialidad</label>
                  <input
                    type="text"
                    className="form-control shadow-sm"
                    placeholder="Ej: Ciencias de la Computación"
                    value={newUser.specialtyArea || ''}
                    onChange={(e) => setNewUser({ ...newUser, specialtyArea: e.target.value })}
                  />
                  <small className="text-muted">Opcional - puede completarlo después</small>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 mb-3">
                  <label className="form-label fw-bold">Rango Académico</label>
                  <select
                    className="form-select shadow-sm"
                    value={newUser.academicRank || ''}
                    onChange={(e) => setNewUser({ ...newUser, academicRank: e.target.value })}
                  >
                    <option value="">Seleccionar...</option>
                    <option value="Instructor">Instructor</option>
                    <option value="Asistente">Profesor Asistente</option>
                    <option value="Asociado">Profesor Asociado</option>
                    <option value="Titular">Profesor Titular</option>
                  </select>
                  <small className="text-muted">Opcional - puede completarlo después</small>
                </div>
                <div className="col-md-6 mb-3">
                  <label className="form-label fw-bold">Máximo de Grupos Permitidos</label>
                  <input
                    type="number"
                    min="1"
                    max="10"
                    className="form-control shadow-sm"
                    placeholder="Ej: 3"
                    value={newUser.maxGroupsAllowed || ''}
                    onChange={(e) => setNewUser({ ...newUser, maxGroupsAllowed: e.target.value })}
                  />
                  <small className="text-muted">Opcional - puede completarlo después</small>
                </div>
              </div>
            </div>
          )}

          {/* Campos solo visibles si el rol es STUDENT/ESTUDIANTE */}
          {(newUser.role?.toUpperCase() === 'STUDENT' || newUser.role?.toUpperCase() === 'ESTUDIANTE') && (
            <div className="animate__animated animate__fadeIn">
              <div className="row">
                <div className="col-md-6 mb-3">
                  <label className="form-label fw-bold">Código de Estudiante *</label>
                  <input
                    type="text"
                    className="form-control shadow-sm"
                    placeholder="Ej: A00123456"
                    value={newUser.code || ''}
                    onChange={(e) => setNewUser({ ...newUser, code: e.target.value })}
                    required
                  />
                  <small className="text-danger">Campo obligatorio</small>
                </div>
                <div className="col-md-6 mb-3">
                  <label className="form-label fw-bold">Promedio (GPA)</label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    max="5"
                    className="form-control shadow-sm"
                    placeholder="Ej: 4.5"
                    value={newUser.gpa || ''}
                    onChange={(e) => setNewUser({ ...newUser, gpa: e.target.value })}
                  />
                  <small className="text-muted">Opcional - puede completarlo después</small>
                </div>
              </div>
            </div>
          )}

          {/* Mensaje para roles sin campos específicos */}
          {newUser.role && 
           newUser.role.toUpperCase() !== 'STUDENT' && 
           newUser.role.toUpperCase() !== 'ESTUDIANTE' &&
           newUser.role.toUpperCase() !== 'PROFESSOR' &&
           newUser.role.toUpperCase() !== 'PROFESOR' &&
           newUser.role.toUpperCase() !== 'ADMIN' &&
           newUser.role.toUpperCase() !== 'ADMINISTRATOR' && (
            <div className="alert alert-info d-flex align-items-center">
              <i className="bi bi-info-circle me-2"></i>
              <span>El rol <strong>{newUser.role}</strong> no requiere información adicional.</span>
            </div>
          )}



        </div> {/* Cierra modal-body */}

        {/* Footer del Modal */}
        <div className="modal-footer border-top-0">
          <button type="button" className="btn btn-outline-secondary" onClick={() => setShowCreateProfessorModal(false)}>
            <i className="bi bi-x-lg me-1"></i>
            Cancelar
          </button>
          <button 
            type="button" 
            className="btn btn-success" 
            onClick={handleRegisterUser}
            disabled={
              !newUser.name || 
              !newUser.lastName || 
              !newUser.email || 
              !newUser.password || 
              !newUser.role ||
              ((newUser.role.toUpperCase() === 'STUDENT' || newUser.role.toUpperCase() === 'ESTUDIANTE') && !newUser.code)
            }
          >
            <i className="bi bi-check-lg me-1"></i>
            Crear Usuario
          </button>
        </div>

      </div>
    </div>
  </div>
  
      ) /* Cierra modal crear usuario */    }
    </div>
  );
}