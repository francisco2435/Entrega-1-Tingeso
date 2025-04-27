// src/components/Karts.js
import React, { useEffect, useState } from "react";
import { Box, Typography, Card, CardContent, Button, Grid, CircularProgress, Stack } from "@mui/material";
import { useNavigate } from "react-router-dom"; // importamos useNavigate
import kartServicio from "../services/kart.servicio";

const Karts = () => {
  const [disponibles, setDisponibles] = useState([]);
  const [mantenimiento, setMantenimiento] = useState([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate(); // hook para navegar

  const obtenerKarts = async () => {
    setLoading(true);
    try {
      const [resDisponibles, resMantenimiento] = await Promise.all([
        kartServicio.obtenerKartsPorEstado("disponible"),
        kartServicio.obtenerKartsPorEstado("mantenimiento"),
      ]);
      setDisponibles(resDisponibles.data);
      setMantenimiento(resMantenimiento.data);
    } catch (error) {
      console.error("Error al obtener karts:", error);
    }
    setLoading(false);
  };

  useEffect(() => {
    obtenerKarts();
  }, []);

  const cambiarEstado = async (codigo, estadoActual) => {
    const nuevoEstado = estadoActual === "disponible" ? "mantenimiento" : "disponible";
    try {
      await kartServicio.cambiarEstadoKart(codigo, nuevoEstado);
      obtenerKarts(); // Refrescar lista
    } catch (error) {
      console.error("Error al cambiar estado del kart:", error);
    }
  };

  const renderKarts = (karts) =>
    karts.map((kart) => (
      <Card key={kart.codigo} sx={{ mb: 2, position: "relative" }}>
        <CardContent>
          <Typography variant="h6">Código: {kart.codigo}</Typography>
          <Typography>Modelo: {kart.modelo}</Typography>
          <Typography>Estado: {kart.estado}</Typography>
          <Box sx={{ mt: 1 }}>
            <Button
              variant="outlined"
              size="small"
              onClick={() => cambiarEstado(kart.codigo, kart.estado)}
            >
              Cambiar a {kart.estado === "disponible" ? "mantenimiento" : "disponible"}
            </Button>
          </Box>
        </CardContent>
      </Card>
    ));

  return (
    <Box sx={{ p: 4 }}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 3 }}>
        <Typography variant="h4">
          Gestión de Karts
        </Typography>
        <Button
          variant="contained"
          color="primary"
          onClick={() => navigate("/nuevo-Kart")}
        >
          Crear Nuevo Kart
        </Button>
      </Stack>

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <Box sx={{ display: "flex", justifyContent: "center" }}>
          <Grid container spacing={4} sx={{ maxWidth: "1000px" }}>
            <Grid item xs={12} md={6}>
              <Typography variant="h5" gutterBottom>
                Karts Disponibles
              </Typography>
              {renderKarts(disponibles)}
            </Grid>
            <Grid item xs={12} md={6}>
              <Typography variant="h5" gutterBottom>
                Karts en Mantenimiento
              </Typography>
              {renderKarts(mantenimiento)}
            </Grid>
          </Grid>
        </Box>
      )}
    </Box>
  );
};

export default Karts;
