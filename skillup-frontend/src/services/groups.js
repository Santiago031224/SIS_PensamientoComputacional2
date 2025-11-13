import api from './api';

export const GroupsApi = {
  list: () => api.get('/groups').then(res => res.data),
};

export default GroupsApi;
