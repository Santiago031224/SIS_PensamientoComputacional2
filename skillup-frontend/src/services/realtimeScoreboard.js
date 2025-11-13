// Servicio simple para suscripción al scoreboard en tiempo real mediante WebSocket.
// Usa la variable de entorno VITE_WS_SCOREBOARD; si no existe, asume ws://localhost:8080/ws/scoreboard
// Protocolo esperado: mensajes JSON { type: 'EXERCISE_SOLVED', studentName, exerciseId, activityTitle, timestamp }

const WS_URL = import.meta.env.VITE_WS_SCOREBOARD || 'ws://localhost:8080/ws/scoreboard';

class ScoreboardRealtime {
  constructor() {
    this.ws = null;
    this.listeners = new Set();
    this.reconnectDelay = 3000;
    this.connect();
  }

  connect() {
    try {
      this.ws = new WebSocket(WS_URL);
      this.ws.onopen = () => {
        // console.debug('WS scoreboard conectado');
      };
      this.ws.onmessage = (evt) => {
        try {
          const data = JSON.parse(evt.data);
          this.listeners.forEach(fn => fn(data));
        } catch (err) {
          console.error('Mensaje WS inválido', err);
        }
      };
      this.ws.onclose = () => {
        setTimeout(() => this.connect(), this.reconnectDelay);
      };
      this.ws.onerror = (e) => {
        console.error('Error WebSocket scoreboard', e);
        try { this.ws.close(); } catch(_) {}
      };
    } catch (err) {
      console.error('No se pudo conectar al WebSocket', err);
      setTimeout(() => this.connect(), this.reconnectDelay);
    }
  }

  subscribe(fn) {
    this.listeners.add(fn);
    return () => this.listeners.delete(fn);
  }

  // Enviar eventos manuales (para pruebas locales sin backend)
  emitTestSolved(payload) {
    const mock = { type: 'EXERCISE_SOLVED', timestamp: new Date().toISOString(), ...payload };
    this.listeners.forEach(fn => fn(mock));
  }
}

export const scoreboardRealtime = new ScoreboardRealtime();
export default scoreboardRealtime;
