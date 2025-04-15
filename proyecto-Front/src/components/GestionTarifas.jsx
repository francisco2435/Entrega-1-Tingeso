import React, { useEffect, useState } from "react";
import tarifaServicio from "../services/tarifa.servicio";
import {
  Box,
  Typography,
  TableContainer,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Paper,
  CircularProgress,
  Button
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const GestionTarifas = () => {
  const [tarifas, setTarifas] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    tarifaServicio
      .obtenerTarifas()
      .then((response) => {
        setTarifas(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error al obtener tarifas:", error);
        setLoading(false);
      });
  }, []);

  const CrearTarifa = () => {
    navigate("/crear-tarifa");
  };

  const ModificarTarifa = (id) => {
    navigate(`/modificar-tarifa/${id}`);
  };

  return (
    <Box sx={{ padding: 4 }}>
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
        <Typography variant="h4">Gestión de Tarifas</Typography>
        <Button variant="contained" color="primary" onClick={CrearTarifa}>
          Crear nueva tarifa
        </Button>
      </Box>

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead sx={{ backgroundColor: "#1976d2" }}>
              <TableRow>
                <TableCell sx={{ color: "#fff" }}>ID</TableCell>
                <TableCell sx={{ color: "#fff" }}>Numero de vueltas</TableCell>
                <TableCell sx={{ color: "#fff" }}>Tiempo Máximo</TableCell>
                <TableCell sx={{ color: "#fff" }}>Precio</TableCell>
                <TableCell sx={{ color: "#fff" }}>Duracion de reserva</TableCell>
                <TableCell sx={{ color: "#fff" }}>Tipo</TableCell>
                <TableCell sx={{ color: "#fff" }}>Acciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {tarifas.map((tarifa) => (
                <TableRow key={tarifa.id}>
                  <TableCell>{tarifa.id}</TableCell>
                  <TableCell>{tarifa.numeroVueltas}</TableCell>
                  <TableCell>{tarifa.tiempoMax}</TableCell>
                  <TableCell>{tarifa.precio}</TableCell>
                  <TableCell>{tarifa.duracionReserva}</TableCell>
                  <TableCell>{tarifa.tipo}</TableCell>
                  <TableCell>
                    <Button
                      variant="outlined"
                      color="primary"
                      size="small"
                      onClick={() => ModificarTarifa(tarifa.id)}
                    >
                      Modificar
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </Box>
  );
};

export default GestionTarifas;
