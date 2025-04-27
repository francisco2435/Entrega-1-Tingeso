import axios from "axios";

const proyectoBackendServer = "4.154.245.139";

console.log(proyectoBackendServer)

export default axios.create({
    baseURL: `http://${proyectoBackendServer}`,
    headers: {
        'Content-Type': 'application/json'
    }
});