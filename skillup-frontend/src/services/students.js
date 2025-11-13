import api from './api';

export const StudentsApi = {
  create: (data) => api.post('/students', data).then(r => r.data),
  list: () => api.get('/students').then(r => r.data),
  getById: (id) => api.get(`/students/${id}`).then(r => r.data),
  getByUserId: (userId) => api.get(`/students/user/${userId}`).then(r => r.data),
  getMyProfile: () => api.get('/students/me').then(r => r.data),
  findByUserId: async (userId) => {
    // Use the new /me endpoint to get the current student's profile
    try {
      return await api.get('/students/me').then(r => r.data);
    } catch (error) {
      console.error('Error fetching student profile:', error);
      return null;
    }
  }
};

export default StudentsApi;
