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

const GestionTarifas = () => {
  const [tarifas, setTarifas] = useState([]);
  const [loading, setLoading] = useState(true);

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

  return (
    <Box sx={{ padding: 4 }}>
        <Typography variant="h5" mb={2}>
            Tarifas diponibles
        </Typography>
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead sx={{ backgroundColor: "#1976d2" }}>
              <TableRow>
                <TableCell sx={{ color: "#fff" }}>Numero de vueltas</TableCell>
                <TableCell sx={{ color: "#fff" }}>Tiempo Máximo</TableCell>
                <TableCell sx={{ color: "#fff" }}>Precio</TableCell>
                <TableCell sx={{ color: "#fff" }}>Duracion de reserva</TableCell>
                <TableCell sx={{ color: "#fff" }}>Tipo</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {tarifas.map((tarifa) => (
                <TableRow key={tarifa.id}>
                  <TableCell>{tarifa.numeroVueltas}</TableCell>
                  <TableCell>{tarifa.tiempoMax}</TableCell>
                  <TableCell>{tarifa.precio}</TableCell>
                  <TableCell>{tarifa.duracionReserva}</TableCell>
                  <TableCell>{tarifa.tipo}</TableCell>
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
