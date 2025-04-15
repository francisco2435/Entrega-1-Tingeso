import React, { useState } from "react";
import { Box, Typography, TextField, Button, CircularProgress, Alert, MenuItem, Select, InputLabel, FormControl } from "@mui/material";
import tarifaServicio from "../services/tarifa.servicio";
import { useNavigate } from "react-router-dom";

const CrearTarifa = () => {
  const [nuevaTarifa, setNuevaTarifa] = useState({
    numeroVueltas: "",
    tiempoMax: "",
    precio: "",
    duracionReserva: "",
    tipo: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNuevaTarifa({
      ...nuevaTarifa,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
  e.preventDefault();
  setLoading(true);
  setError(null); // Limpiar errores previos

  // Llamamos al servicio para crear la tarifa
  tarifaServicio
    .crearTarifa(nuevaTarifa)
    .then((response) => {
      setLoading(false);

      // Verificamos si la respuesta tiene algún dato
      if (response && response.data) {
        // Mostrar algún tipo de mensaje o utilizar los datos de la respuesta
        console.log("Tarifa creada con éxito:", response.data);

        // Redirigir a la vista de tarifas después de crear
        navigate("/tarifas");
      } else {
        setError("La respuesta del servidor no es la esperada.");
      }
    })
    .catch((error) => {
      setLoading(false);
      setError("Hubo un error al crear la tarifa. Inténtalo nuevamente.");
      console.error("Error al crear la tarifa:", error);  // Muestra el error para depuración
    });
};

  const handleCancel = () => {
    navigate("/tarifas"); // Redirigir a la vista de tarifas si se cancela
  };

  return (
    <Box sx={{ padding: 4 }}>
      <Typography variant="h4" gutterBottom>
        Crear Nueva Tarifa
      </Typography>

      <form onSubmit={handleSubmit}>
        <TextField
          label="Número de Vueltas"
          name="numeroVueltas"
          value={nuevaTarifa.numeroVueltas}
          onChange={handleChange}
          fullWidth
          margin="normal"
          type="number"
          required
        />
        <TextField
          label="Tiempo Máximo"
          name="tiempoMax"
          value={nuevaTarifa.tiempoMax}
          onChange={handleChange}
          fullWidth
          margin="normal"
          type="number"
          required
        />
        <TextField
          label="Precio"
          name="precio"
          value={nuevaTarifa.precio}
          onChange={handleChange}
          fullWidth
          margin="normal"
          type="number"
          required
        />
        <TextField
          label="Duración de Reserva"
          name="duracionReserva"
          value={nuevaTarifa.duracionReserva}
          onChange={handleChange}
          fullWidth
          margin="normal"
          type="number"
          required
        />
        
        <FormControl fullWidth margin="normal">
          <InputLabel>Tipo</InputLabel>
          <Select
            name="tipo"
            value={nuevaTarifa.tipo}
            onChange={handleChange}
            required
          >
            <MenuItem value="normal">Normal</MenuItem>
            <MenuItem value="fin de semana">Fin de semana</MenuItem>
            <MenuItem value="dia especial">Día especial</MenuItem>
          </Select>
        </FormControl>

        {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}

        <Box sx={{ display: "flex", justifyContent: "space-between", mt: 3 }}>
          <Button
            variant="contained"
            color="primary"
            type="submit"
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} /> : "Crear Tarifa"}
          </Button>
          <Button
            variant="outlined"
            color="secondary"
            onClick={handleCancel}
            disabled={loading}
          >
            Cancelar
          </Button>
        </Box>
      </form>
    </Box>
  );
};

export default CrearTarifa;
