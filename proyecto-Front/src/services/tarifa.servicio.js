import httpClient from "../http-common";

const TARIFA_API_URL = '/tarifa'; 

const obtenerTarifas = () => {
    return httpClient.get(`${TARIFA_API_URL}/obtenerTarifas`);
};

const crearTarifa = (nuevaTarifa) => {
    return httpClient.post(`${TARIFA_API_URL}/nuevaTarifa`, nuevaTarifa); 
};

const modificarTarifa = (tarifa) => {
    return httpClient.put(`${TARIFA_API_URL}/modificarTarifa`, tarifa); 
};

const obtenerTarifa = (id) => {
    return httpClient.get(`${TARIFA_API_URL}/obtenerTarifa?id=${id}`);
};

export default {
    obtenerTarifas,
    crearTarifa,
    modificarTarifa,
    obtenerTarifa
};
