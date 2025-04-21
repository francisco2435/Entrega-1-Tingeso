import httpClient from "../http-common";

const REPORTE_API_URL = "/reporte";

// Método para hacer un reporte
const hacerReporte = (reporte) => {
  return httpClient.post(`${REPORTE_API_URL}/hacerReporte`, reporte);
};

export default {
  hacerReporte,
};
