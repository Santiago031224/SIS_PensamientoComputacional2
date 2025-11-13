import api from './api';

export const ProfessorsApi = {
  list: () => api.get('/professors').then(res => res.data),
  getById: (id) => api.get(`/professors/${id}`).then(res => res.data),
  create: (data) => api.post('/professors', data).then(res => res.data),
  update: (id, data) => api.put(`/professors/${id}`, data).then(res => res.data),
  delete: (id) => api.delete(`/professors/${id}`).then(res => res.data),
};

export default ProfessorsApi;
