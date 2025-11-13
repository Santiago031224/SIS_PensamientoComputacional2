import api from './api';

export const TeachingAssignmentsApi = {
  list: async () => {
    try {
      const response = await api.get('/teaching-assignments');
      return response.data;
    } catch (error) {
      console.error('❌ [TeachingAssignments API] Error en la petición:', {
        message: error.message,
        response: error.response?.data,
        status: error.response?.status,
        fullError: error
      });
      throw error;
    }
  },
  
  create: async (payload) => {
    try {
      const response = await api.post('/teaching-assignments', payload);
      return response.data;
    } catch (error) {
      console.error('❌ [TeachingAssignments API] Error creando registro:', error);
      throw error;
    }
  },
};

export default TeachingAssignmentsApi;
