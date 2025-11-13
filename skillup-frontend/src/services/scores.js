import api from './api';

export const ScoresApi = {
  list: () => api.get('/scores').then(res => res.data),
  create: (payload) => api.post('/scores', payload).then(res => res.data),
  update: (id, payload) => api.put(`/scores/${id}`, payload).then(res => res.data),
};

export default ScoresApi;
