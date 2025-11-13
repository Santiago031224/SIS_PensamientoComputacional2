// services/exerciseDocuments.js - VERSIÓN DEBUG
import api from './api';

export const ExerciseDocuments = {
  list: () => api.get('/exercises').then(r => r.data),
  getById: (id) => api.get(`/exercises/${id}`).then(r => r.data),
  create: async (payload) => {
    try {
      const response = await api.post('/exercises', payload);
      return response.data;
    } catch (error) {
      console.error('❌ Error completo:', error);
      console.error('📊 Datos de la respuesta:', error.response?.data);
      throw error;
    }
  },
  update: (id, payload) => api.put(`/exercises/${id}`, payload).then(r => r.data),
  delete: (id) => api.delete(`/exercises/${id}`).then(r => r.data),
};