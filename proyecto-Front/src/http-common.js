import axios from "axios";

const proyectoBackendServer = "localhost:8090";

console.log(proyectoBackendServer)

export default axios.create({
    baseURL: `http://${proyectoBackendServer}`,
    headers: {
        'Content-Type': 'application/json'
    }
});