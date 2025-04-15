import axios from "axios";

const REPORTE_API_URL = "http://localhost:8090/reporte";

// Método para hacer un reporte
const hacerReporte = (reporte) => {
  return axios.post(`${REPORTE_API_URL}/hacerReporte`, reporte);
};

export default {
  hacerReporte,
};
