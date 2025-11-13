import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../../services/auth.js';
import { useAuth } from '../../services/AuthContext.jsx';
import '../../styles/login.css';

export default function SignIn(){
  const [email, setEmail] = useState('admin@example.com');
  const [password, setPassword] = useState('password');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();
  const { setToken } = useAuth();

  async function handleSubmit(e){
    e.preventDefault();
    setLoading(true); 
    setError(null);
    
    try {
      const { token } = await login(email, password);
      setToken(token);
      navigate('/');
    } catch (err){
      setError('Credenciales inválidas. Por favor, verifica tu email y contraseña.');
    } finally {
      setLoading(false);
    }
  }

  // Función para obtener el saludo según la hora
  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Buenos días';
    if (hour < 18) return 'Buenas tardes';
    return 'Buenas noches';
  };

  return (
    <div className="login-container">
      {/* Background con patrón y overlay */}
      <div className="login-background">
        <div className="login-overlay"></div>
        <div className="login-shapes">
          <div className="shape shape-1"></div>
          <div className="shape shape-2"></div>
          <div className="shape shape-3"></div>
        </div>
      </div>

      {/* Contenedor principal */}
      <div className="login-content">
        {/* Lado izquierdo - Branding */}
        <div className="login-brand">
          <div className="brand-content">
            <div className="brand-logo">
              <i className="bi bi-cpu"></i>
            </div>
            <div className="brand-text">
              <h1>Pensamiento Computacional</h1>
              <p>Plataforma de aprendizaje interactivo</p>
            </div>
            
            <div className="welcome-message">
              <h2>{getGreeting()} 👋</h2>
              <p>Ingresa a tu cuenta para continuar con tu aprendizaje</p>
            </div>

      
          </div>
        </div>

        {/* Lado derecho - Formulario */}
        <div className="login-form-container">
          <div className="login-form-card">
            <div className="form-header">
              <h2>Iniciar Sesión</h2>
              <p>Ingresa tus credenciales para acceder</p>
            </div>

            <form onSubmit={handleSubmit} className="login-form">
              <div className="form-group">
                <label className="form-label">
                  <i className="bi bi-envelope"></i>
                  Correo Institucional
                </label>
                <div className="input-container">
                  <input 
                    type="email" 
                    className="form-input" 
                    value={email} 
                    onChange={e => setEmail(e.target.value)} 
                    placeholder="tu.correo@institucion.edu"
                    required 
                    autoComplete="email"
                  />
                  <i className="bi bi-person input-icon"></i>
                </div>
              </div>

              <div className="form-group">
                <label className="form-label">
                  <i className="bi bi-lock"></i>
                  Contraseña
                </label>
                <div className="input-container">
                  <input 
                    type={showPassword ? "text" : "password"} 
                    className="form-input" 
                    value={password} 
                    onChange={e => setPassword(e.target.value)} 
                    placeholder="Ingresa tu contraseña"
                    required 
                    autoComplete="current-password"
                  />
                  <i className="bi bi-key input-icon"></i>
                  <button 
                    type="button"
                    className="password-toggle"
                    onClick={() => setShowPassword(!showPassword)}
                  >
                    <i className={`bi ${showPassword ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                  </button>
                </div>
              </div>

              <div className="form-options">
                <label className="checkbox-container">
                  <input type="checkbox" />
                  <span className="checkmark"></span>
                  Recordar sesión
                </label>
                <a href="#" className="forgot-password">
                  ¿Olvidaste tu contraseña?
                </a>
              </div>

              {error && (
                <div className="error-message">
                  <i className="bi bi-exclamation-triangle"></i>
                  {error}
                </div>
              )}

              <button 
                disabled={loading} 
                className={`login-button ${loading ? 'loading' : ''}`}
              >
                {loading ? (
                  <>
                    <div className="button-spinner"></div>
                    Ingresando...
                  </>
                ) : (
                  <>
                    <i className="bi bi-box-arrow-in-right"></i>
                    Entrar a la Plataforma
                  </>
                )}
              </button>

              
            </form>

            <div className="form-footer">
              <p>
                ¿Necesitas ayuda?{' '}
                <a href="https://www.instagram.com/haider_v_z/" className="support-link">
                  Contacta al soporte técnico
                </a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}