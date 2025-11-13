import api from './api';

export const PeriodsApi = {
  list: () => api.get('/periods').then(res => res.data),
  getById: (id) => api.get(`/periods/${id}`).then(res => res.data),
  create: (data) => api.post('/periods', data).then(res => res.data),
  update: (id, data) => api.put(`/periods/${id}`, data).then(res => res.data),
  delete: (id) => api.delete(`/periods/${id}`).then(res => res.data),
};

export default PeriodsApi;
