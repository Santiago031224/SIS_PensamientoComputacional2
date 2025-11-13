import api from './api';

export const CoursesApi = {
  list: () => api.get('/courses').then(res => res.data),
};

export default CoursesApi;
