import api from './api';

export const ActivityDocuments = {
  list: async () => api.get('/activities').then(r => {
    try { console.debug('[ActivityDocuments] list - raw response length:', Array.isArray(r.data) ? r.data.length : typeof r.data, r.data && r.data.slice ? r.data.slice(0,3) : null); } catch(e){}
    return r.data;
  }),
  getById: async (id) => api.get(`/activities/${id}`).then(r => {
    try { console.debug('[ActivityDocuments] getById - id:', id, 'response:', r.data); } catch(e){}
    return r.data;
  }),
  create: async (payload) => api.post('/activities', payload).then(r => r.data),
  update: async (id, payload) => api.put(`/activities/${id}`, payload).then(r => r.data),
  delete: async (id) => api.delete(`/activities/${id}`).then(r => r.data),
};

export default ActivityDocuments;
