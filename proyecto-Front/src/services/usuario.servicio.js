import axios from "axios";

const USUARIO_API_URL = "http://localhost:8090/usuario";

// Método para login
const login = (correo, contrasenia) => {
  const params = new URLSearchParams({ correo, contrasenia });
  return axios.post(`${USUARIO_API_URL}/login?${params.toString()}`);
};

// Método para registrar nuevo usuario
const registrarUsuario = (nuevoUsuario) => {
  return axios.post(`${USUARIO_API_URL}/nuevousuario`, nuevoUsuario);
};

export default {
    login,
    registrarUsuario,
};
