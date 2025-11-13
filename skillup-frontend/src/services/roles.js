// Helper utilities to normalize and check user roles across the frontend
// Accepts roles as either strings or objects like { name: 'ROLE_ADMIN' }

function normalizeRoleName(raw) {
  if (!raw) return null;
  let name = typeof raw === 'string' ? raw : (raw.name || '');
  name = name.toString().toUpperCase().trim();
  // Remove common prefix
  if (name.startsWith('ROLE_')) name = name.slice(5);
  // Map Spanish/variants to canonical roles
  if (['ADMINISTRADOR', 'ADMIN', 'ADMINISTRADOR_ROLE'].includes(name)) return 'ADMIN';
  if (['PROFESOR', 'PROFESSOR', 'TEACHER', 'DOCENTE'].includes(name)) return 'PROFESSOR';
  if (['ESTUDIANTE', 'STUDENT', 'ALUMNO'].includes(name)) return 'STUDENT';
  if (['COORDINADOR', 'COORDINATOR'].includes(name)) return 'COORDINATOR';
  if (['INVITADO', 'GUEST'].includes(name)) return 'GUEST';
  // Default: return uppercased name without prefix
  return name;
}

function normalizeRolesArray(roles) {
  if (!Array.isArray(roles)) return [];
  return roles.map(r => normalizeRoleName(r)).filter(Boolean);
}

function hasRole(roles, expected) {
  const normalized = normalizeRolesArray(roles);
  return normalized.includes(expected);
}

function isAdmin(roles) {
  return hasRole(roles, 'ADMIN');
}

function isProfessor(roles) {
  return hasRole(roles, 'PROFESSOR');
}

function isStudent(roles) {
  return hasRole(roles, 'STUDENT');
}

function isAdminOrProfessor(roles) {
  const normalized = normalizeRolesArray(roles);
  return normalized.includes('ADMIN') || normalized.includes('PROFESSOR');
}

export { normalizeRoleName, normalizeRolesArray, hasRole, isAdmin, isProfessor, isStudent, isAdminOrProfessor };
import api from './api';

export const RolesApi = {
  list: () => api.get('/roles').then(res => res.data),
};

export default RolesApi;
