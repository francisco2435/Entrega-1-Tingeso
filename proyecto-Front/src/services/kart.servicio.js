// src/services/kart.servicio.js
import axios from "axios";

const BASE_URL = "http://localhost:8090/kart";

const obtenerKartsPorEstado = (estado) => {
  return axios.get(`${BASE_URL}/getKartsEstado`, {
    params: { estado },
  });
};

const cambiarEstadoKart = (codigo, newEstado) => {
  return axios.put(`${BASE_URL}/cambiarEstado`, null, {
    params: { codigo, newEstado },
  });
};

export default {
  obtenerKartsPorEstado,
  cambiarEstadoKart,
};
