// src/components/CrearKart.js
import React, { useState } from "react";
import { TextField, Button, Container, Typography, Box, Stack } from "@mui/material";
import { useNavigate } from "react-router-dom"; // importamos useNavigate
import kartServicio from "../services/kart.servicio";

const CrearKart = () => {
  const navigate = useNavigate(); // hook para navegar

  const [kartData, setKartData] = useState({
    codigo: "",
    modelo: "",
    estado: "disponible",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setKartData({
      ...kartData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await kartServicio.crearKart(kartData);
      alert("¡Kart creado exitosamente!");
      setKartData({ codigo: "", modelo: "", estado: "disponible" });
      navigate("/karts"); // después de crear, también podrías redirigir automáticamente
    } catch (error) {
      console.error("Error al crear el kart:", error);
      alert("Hubo un error al crear el kart.");
    }
  };

  const handleCancel = () => {
    navigate("/karts"); // simplemente navegamos a /karts
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 5 }}>
        <Typography variant="h4" align="center" gutterBottom>
          Crear Nuevo Kart
        </Typography>
        <form onSubmit={handleSubmit}>
          <TextField
            label="Código"
            name="codigo"
            value={kartData.codigo}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Modelo"
            name="modelo"
            value={kartData.modelo}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <Stack direction="row" spacing={2} sx={{ mt: 2 }}>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              fullWidth
            >
              Crear Kart
            </Button>
            <Button
              variant="outlined"
              color="secondary"
              fullWidth
              onClick={handleCancel}
            >
              Cancelar
            </Button>
          </Stack>
        </form>
      </Box>
    </Container>
  );
};

export default CrearKart;
