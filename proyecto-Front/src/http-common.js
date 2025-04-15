import axios from "axios";

const proyectoBackendServer = import.meta.env.VITE_PROYECTO_BACKEND_SERVER;
const proyectoBackendPort = import.meta.env.VITE_PROYECTO_BACKEND_PORT;

console.log(proyectoBackendServer)
console.log(proyectoBackendPort)

export default axios.create({
    baseURL: `http://${proyectoBackendServer}:${proyectoBackendPort}`,
    headers: {
        'Content-Type': 'application/json'
    }
});