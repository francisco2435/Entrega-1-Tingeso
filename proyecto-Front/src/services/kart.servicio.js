// src/services/kart.servicio.js
import httpClient from "../http-common";

const BASE_URL = "/kart";

const obtenerKartsPorEstado = (estado) => {
  return httpClient.get(`${BASE_URL}/getKartsEstado`, {
    params: { estado },
  });
};

const cambiarEstadoKart = (codigo, newEstado) => {
  return httpClient.put(`${BASE_URL}/cambiarEstado`, null, {
    params: { codigo, newEstado },
  });
};

export default {
  obtenerKartsPorEstado,
  cambiarEstadoKart,
};
