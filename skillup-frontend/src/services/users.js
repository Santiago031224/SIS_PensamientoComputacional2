import api from './api';

export const UsersApi = {
  create: (data) => api.post('/users', data).then(r => r.data),
  list: () => api.get('/users').then(r => r.data),
  byEmail: (email) => api.get(`/users/email/${encodeURIComponent(email)}`).then(r => r.data),
  getById: (id) => api.get(`/users/${id}`).then(r => r.data),
  update: (id, data) => api.put(`/users/${id}`, data).then(r => r.data),
  delete: (id) => api.delete(`/users/${id}`).then(r => r.data),
  updateStatus: (id, _status) => api.post(`/users/${id}/toggle-status`).then(() => true),
  addRole: (userId, roleId) => api.post(`/users/${userId}/add-role/${roleId}`).then(r => r.data),
  removeRole: (userId, roleId) => api.post(`/users/${userId}/remove-role/${roleId}`).then(r => r.data),
  changePassword: (id, data) => api.post(`/users/${id}/change-password`, data).then(r => r.data),
};
