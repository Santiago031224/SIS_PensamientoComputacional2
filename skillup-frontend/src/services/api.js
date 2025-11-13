import axios from 'axios';

const api = axios.create({
  baseURL: '/api'
});

api.interceptors.request.use((config) => {
  const token = typeof window !== 'undefined' ? sessionStorage.getItem('pc_token') : null;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
    // Keep a non-sensitive debug log (avoid printing the token itself)
    try {
      console.debug('[api] Request:', config.method?.toUpperCase(), config.url);
    } catch (e) { /* ignore logging errors */ }
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    console.error('❌ API Error:', {
      status,
      url: error?.config?.url,
      method: error?.config?.method,
      data: error?.response?.data,
      message: error?.message
    });
    
    if (status === 401 || status === 403) {
      console.warn('🚫 API - Unauthorized/Forbidden detected');
      console.warn('⏸️ REDIRECT DISABLED FOR DEBUGGING - Check the error above');
      // TEMPORALMENTE DESHABILITADO PARA DEBUGGING
      // if (typeof window !== 'undefined') {
      //   sessionStorage.removeItem('pc_token');
      //   if (window.location.pathname !== '/login') {
      //     console.log('🔄 Redirecting to /login from:', window.location.pathname);
      //     window.location.href = '/login';
      //   }
      // }
    }
    return Promise.reject(error);
  }
);

export default api;
