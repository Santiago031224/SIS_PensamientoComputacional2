import api from './api';

/**
 * Professor Analytics API Service
 * Provides integrated data from both SQL (Oracle) and NoSQL (MongoDB) databases
 */
export const ProfessorAnalyticsApi = {
  /**
   * Get analytics for the currently authenticated professor
   * @returns {Promise<ProfessorAnalyticsDTO>}
   */
  getCurrent: async () => {
    const response = await api.get('/professor-analytics');
    return response.data;
  },

  /**
   * Get analytics for a specific professor (Admin/Coordinator only)
   * @param {number} professorId - The ID of the professor
   * @returns {Promise<ProfessorAnalyticsDTO>}
   */
  getByProfessorId: async (professorId) => {
    const response = await api.get(`/professor-analytics/${professorId}`);
    return response.data;
  },
};

/**
 * @typedef {Object} ProfessorAnalyticsDTO
 * @property {number} professorId
 * @property {string} professorName
 * @property {string} professorEmail
 * @property {ActivitySummaryDTO[]} activities
 * @property {GroupSummaryDTO[]} groups
 * @property {ProfessorStatistics} statistics
 */

/**
 * @typedef {Object} ActivitySummaryDTO
 * @property {string} id - MongoDB document ID
 * @property {string} title
 * @property {string} description
 * @property {string} startDate - ISO date string
 * @property {string} endDate - ISO date string
 * @property {string} status - ACTIVE, COMPLETED, etc.
 * @property {number} exerciseCount
 * @property {number} submissionCount
 */

/**
 * @typedef {Object} GroupSummaryDTO
 * @property {number} id - Oracle group ID
 * @property {string} name
 * @property {number} studentCount
 * @property {string} courseName
 */

/**
 * @typedef {Object} ProfessorStatistics
 * @property {number} totalTeachingAssignments
 * @property {number} totalActivities
 * @property {number} totalStudents
 * @property {number} totalGroups
 * @property {number} activeActivities
 * @property {number} completedActivities
 */

export default ProfessorAnalyticsApi;
