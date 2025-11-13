import api from './api';

export const PointCodeDocuments = {
  list: () => api.get('/pointcodes').then(r => r.data),
  getById: (code) => api.get(`/pointcodes/${code}`).then(r => r.data),
  create: (payload) => api.post('/pointcodes', payload).then(r => r.data),
  update: (code, payload) => api.put(`/pointcodes/${code}`, payload).then(r => r.data),
  delete: (code) => api.delete(`/pointcodes/${code}`).then(r => r.data),
  deactivate: async (code) => {
    // Desactivar el código usando el endpoint de actualización
    const current = await api.get(`/pointcodes/${code}`).then(r => r.data);
    return api.put(`/pointcodes/${code}`, { ...current, active: false }).then(r => r.data);
  },
};

export default PointCodeDocuments;