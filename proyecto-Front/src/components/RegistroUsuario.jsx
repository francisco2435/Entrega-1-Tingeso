import React, { useState } from "react";
import {
  Box,
  TextField,
  Button,
  Typography,
  CircularProgress,
  Alert,
} from "@mui/material";
import { useNavigate } from "react-router-dom";  // Importamos useNavigate
import usuarioServicio from "../services/usuario.servicio";

const RegistroUsuario = () => {
  const navigate = useNavigate(); // Inicializamos el hook useNavigate
  const [usuario, setUsuario] = useState({
    nombre: "",
    rut: "",
    correo: "",
    telefono: "",
    rol: "cliente",
    contrasenia: "",
    fechaNacimiento: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [exito, setExito] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUsuario({ ...usuario, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setExito(null);

    usuarioServicio
      .registrarUsuario(usuario)
      .then((response) => {
        setLoading(false);
        if (!response || response === null) {
          setError("No se pudo registrar el usuario. Respuesta vacía del servidor.");
          return;
        }
        setExito("Usuario registrado exitosamente.");
        setUsuario({
          nombre: "",
          rut: "",
          correo: "",
          telefono: "",
          rol: "cliente",
          contrasenia: "",
          fechaNacimiento: "",
        });
      })
      .catch((error) => {
        setLoading(false);
        const mensajeError = error.response?.data?.mensaje || "Ya existe un usuario con ese correo o RUT.";
        setError(mensajeError);
        console.error("Error en el registro:", error);
      });
  };


  const handleCancel = () => {
    navigate("/home");  
  };

  return (
    <Box sx={{ maxWidth: 600, mx: "auto", mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Registro de Usuario
      </Typography>

      <form onSubmit={handleSubmit}>
        <TextField
          label="Nombre"
          name="nombre"
          value={usuario.nombre}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        <TextField
          label="RUT"
          name="rut"
          value={usuario.rut}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        <TextField
          label="Correo"
          name="correo"
          value={usuario.correo}
          onChange={handleChange}
          type="email"
          fullWidth
          margin="normal"
          required
        />
        <TextField
          label="Teléfono"
          name="telefono"
          value={usuario.telefono}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        <TextField
          label="Contraseña"
          name="contrasenia"
          type="password"
          value={usuario.contrasenia}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        <TextField
          label="Fecha de Nacimiento"
          name="fechaNacimiento"
          type="date"
          value={usuario.fechaNacimiento}
          onChange={handleChange}
          fullWidth
          margin="normal"
          InputLabelProps={{
            shrink: true,
          }}
          required
        />

        {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
        {exito && <Alert severity="success" sx={{ mt: 2 }}>{exito}</Alert>}

        <Box sx={{ mt: 3, textAlign: "center" }}>
          <Button
            variant="contained"
            color="primary"
            type="submit"
            disabled={loading}
            sx={{ mr: 2 }}
          >
            {loading ? <CircularProgress size={24} /> : "Registrar"}
          </Button>
          
          {/* Botón de Cancelar */}
          <Button
            variant="outlined"
            color="secondary"
            onClick={handleCancel}
          >
            Cancelar
          </Button>
        </Box>
      </form>
    </Box>
  );
};

export default RegistroUsuario;
