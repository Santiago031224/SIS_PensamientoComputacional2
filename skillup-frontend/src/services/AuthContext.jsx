import React, { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import api from './api.js';
import { UsersApi } from './users.js';

const AuthCtx = createContext(null);

function decodeJwt(token){
  try{
    const parts = token.split('.');
    if (parts.length !== 3) return null;
    const payload = parts[1];
    const normalized = payload.replace(/-/g, '+').replace(/_/g, '/');
    const json = atob(normalized);

    return JSON.parse(decodeURIComponent(escape(json)));
  }catch(e){ return null; }
}

export function AuthProvider({ children }){
  const [token, setToken] = useState(() => {
    if (typeof window === 'undefined') return null;
    return sessionStorage.getItem('pc_token');
  });
  const [user, setUser] = useState(null);

  const updateUser = useCallback((userData) => {
    setUser(prevUser => ({
      ...prevUser,
      ...userData
    }));
  }, []);

  const logout = useCallback(() => {
    setToken(null);
    setUser(null);
    if (typeof window !== 'undefined') {
      sessionStorage.removeItem('pc_token');
    }
  }, []);

  useEffect(() => {
    if (typeof window === 'undefined') return;
    if (token) sessionStorage.setItem('pc_token', token);
    else sessionStorage.removeItem('pc_token');
  }, [token]);

  useEffect(() => {
    if (!token) return;
    const payload = decodeJwt(token);
    if (!payload) {
      logout();
      return;
    }
    if (payload.exp && payload.exp * 1000 <= Date.now()) {
      logout();
    }
  }, [token, logout]);

  // load user when token changes
  useEffect(() => {
    let mounted = true;
    async function loadUser(){
      if (!token) { setUser(null); return; }
      const payload = decodeJwt(token);
      const email = payload?.sub || payload?.email;
      try{
        if (email){
          const u = await UsersApi.byEmail(email);
          if (mounted) setUser(u);
        }
      }catch(err){
        console.error('❌ AuthContext - Error loading user:', err);
        const status = err?.response?.status;
        if (status === 401 || status === 403) {
          // TEMPORALMENTE DESHABILITADO PARA DEBUGGING
          // logout();
          return;
        }

        try{
          const list = await api.get('/users');
          if (mounted) setUser(list.data[0] || null);
        }catch(e){
          console.error('❌ AuthContext - Fallback also failed:', e);
          if (mounted) setUser(null);
        }
      }
    }
    loadUser();
    return () => { mounted = false; };
  }, [token, logout]);

  const value = useMemo(
    () => ({ token, setToken, user, setUser, api, logout, updateUser }),
    [token, user, logout, updateUser]
  );
  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>;
}

export function useAuth(){
  const ctx = useContext(AuthCtx);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}