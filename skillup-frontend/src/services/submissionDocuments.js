import api from './api';

export const SubmissionDocuments = {
  list: () => api.get('/submissions').then(r => r.data),
  getById: (id) => api.get(`/submissions/${id}`).then(r => r.data),
  create: (payload) => api.post('/submissions', payload).then(r => r.data),
  update: (id, payload) => api.put(`/submissions/${id}`, payload).then(r => r.data),
  delete: (id) => api.delete(`/submissions/${id}`).then(r => r.data),
};

export default SubmissionDocuments;
