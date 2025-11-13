import api from './api';

/**
 * Exercise API Service
 * Handles all exercise-related API calls
 */
export const ExercisesApi = {
  /**
   * Get all exercises
   * @returns {Promise<Array>}
   */
  list: () => api.get('/exercises').then(res => res.data),

  /**
   * Get exercise by ID
   * @param {number} id - Exercise ID
   * @returns {Promise<Object>}
   */
  getById: (id) => api.get(`/exercises/${id}`).then(res => res.data),

  /**
   * Create a new exercise
   * @param {Object} data - Exercise data
   * @param {string} data.description - Exercise description
   * @param {string} data.difficulty - Difficulty level (EASY, MEDIUM, HARD)
   * @returns {Promise<Object>}
   */
  create: (data) => api.post('/exercises', data).then(res => res.data),

  /**
   * Update an exercise
   * @param {number} id - Exercise ID
   * @param {Object} data - Updated exercise data
   * @returns {Promise<Object>}
   */
  update: (id, data) => api.put(`/exercises/${id}`, data).then(res => res.data),

  /**
   * Delete an exercise
   * @param {number} id - Exercise ID
   * @returns {Promise<void>}
   */
  delete: (id) => api.delete(`/exercises/${id}`).then(res => res.data),
};

export default ExercisesApi;
