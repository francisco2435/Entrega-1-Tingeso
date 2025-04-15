import axios from 'axios';

const TARIFA_API_URL = 'http://localhost:8090/tarifa'; 

const obtenerTarifas = () => {
    return axios.get(`${TARIFA_API_URL}/obtenerTarifas`);
};

const crearTarifa = (nuevaTarifa) => {
    return axios.post(`${TARIFA_API_URL}/nuevaTarifa`, nuevaTarifa); 
};

const modificarTarifa = (tarifa) => {
    return axios.put(`${TARIFA_API_URL}/modificarTarifa`, tarifa); 
};

const obtenerTarifa = (id) => {
    return axios.get(`${TARIFA_API_URL}/obtenerTarifa?id=${id}`);
};

export default {
    obtenerTarifas,
    crearTarifa,
    modificarTarifa,
    obtenerTarifa
};
