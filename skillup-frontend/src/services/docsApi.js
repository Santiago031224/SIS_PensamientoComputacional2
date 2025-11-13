// DEPRECATED: Este archivo ya no es necesario.
// Todas las APIs ahora usan el cliente axios principal (api.js)
// que apunta a /api/ donde el backend maneja automáticamente
// tanto datos SQL como NoSQL.
//
// Usa 'api.js' en lugar de este archivo para todas las peticiones.

import axios from 'axios';

const docsApi = axios.create();

docsApi.interceptors.request.use((config) => {
  const token = typeof window !== 'undefined' ? sessionStorage.getItem('pc_token') : null;
  if (token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

docsApi.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    if (status === 401 || status === 403) {
      if (typeof window !== 'undefined') {
        sessionStorage.removeItem('pc_token');
        if (window.location.pathname !== '/login') {
          window.location.href = '/login';
        }
      }
    }
    return Promise.reject(error);
  }
);

export default docsApi;
