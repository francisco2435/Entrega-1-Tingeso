import httpClient from "../http-common";

const RESERVA_API_URL = '/reserva';

// Método para hacer una reserva
const hacerReserva = (reserva) => {
  return httpClient.post(`${RESERVA_API_URL}/hacerReserva`, reserva);
};

// Método para obtener todas las reservas
const obtenerReservas = () => {
  return httpClient.get(`${RESERVA_API_URL}/obtenerReservas`);
};

export default {
  hacerReserva,
  obtenerReservas,
};
