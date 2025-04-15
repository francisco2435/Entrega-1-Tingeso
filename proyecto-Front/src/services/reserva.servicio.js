import axios from 'axios';

const RESERVA_API_URL = 'http://localhost:8090/reserva';

// Método para hacer una reserva
const hacerReserva = (reserva) => {
  return axios.post(`${RESERVA_API_URL}/hacerReserva`, reserva);
};

// Método para obtener todas las reservas
const obtenerReservas = () => {
  return axios.get(`${RESERVA_API_URL}/obtenerReservas`);
};

export default {
  hacerReserva,
  obtenerReservas,
};
