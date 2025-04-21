import httpClient from "../http-common";

const USUARIO_API_URL = "/usuario";

// Método para login
const login = (correo, contrasenia) => {
  const params = new URLSearchParams({ correo, contrasenia });
  return httpClient.post(`${USUARIO_API_URL}/login?${params.toString()}`);
};

// Método para registrar nuevo usuario
const registrarUsuario = (nuevoUsuario) => {
  return httpClient.post(`${USUARIO_API_URL}/nuevousuario`, nuevoUsuario);
};

export default {
    login,
    registrarUsuario,
};
