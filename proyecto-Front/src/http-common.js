import axios from "axios";

const proyectoBackendServer = "localhost:80";

console.log(proyectoBackendServer)

export default axios.create({
    baseURL: `http://${proyectoBackendServer}`,
    headers: {
        'Content-Type': 'application/json'
    }
});