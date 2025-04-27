import React, { useState } from "react";
import { Box, TextField, Button, Typography, CircularProgress, Alert } from "@mui/material";
import usuarioServicio from "../services/usuario.servicio";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [correo, setCorreo] = useState("");
  const [contrasenia, setContrasenia] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    usuarioServicio
      .login(correo, contrasenia)
      .then((response) => {
        setLoading(false);
        if (response.data) {
          const rolUsuario = response.data.rol;
          localStorage.setItem("usuario", JSON.stringify(response.data));
          if (rolUsuario === "administrador") {
            navigate("/tarifas");
          } else if (rolUsuario === "cliente") {
            navigate("/home");
          } else {
            setError("Rol de usuario no reconocido.");
          }
        }
      })
      .catch((error) => {
        setLoading(false);
        setError("Credenciales incorrectas.");
      });
  };

  const handleRegistro = () => {
    navigate("/registro-usuario");
  };

  return (
    <Box sx={{ maxWidth: 600, mx: "auto", mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Iniciar Sesión
      </Typography>

      <form onSubmit={handleSubmit}>
        <TextField
          label="Correo"
          value={correo}
          onChange={(e) => setCorreo(e.target.value)}
          type="email"
          fullWidth
          margin="normal"
          required
        />
        <TextField
          label="Contraseña"
          value={contrasenia}
          onChange={(e) => setContrasenia(e.target.value)}
          type="password"
          fullWidth
          margin="normal"
          required
        />

        {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}

        <Box sx={{ mt: 3, textAlign: "center" }}>
          <Button variant="contained" color="primary" type="submit" disabled={loading} sx={{ mr: 2 }}>
            {loading ? <CircularProgress size={24} /> : "Iniciar Sesión"}
          </Button>
          <Button variant="outlined" color="secondary" onClick={handleRegistro}>
            Registro
          </Button>
        </Box>
      </form>
    </Box>
  );
};

export default Login;
