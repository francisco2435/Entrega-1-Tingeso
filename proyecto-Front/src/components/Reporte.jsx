import React, { useState, useEffect } from "react";
import { Box, Typography, CircularProgress, TextField, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from "@mui/material";
import reporteServicio from "../services/reporte.servicio";

const Reporte = () => {
  const [tipo, setTipo] = useState("Número de personas");  // Valor por defecto
  const [fechaInicio, setFechaInicio] = useState("");
  const [fechaFin, setFechaFin] = useState("");
  const [reporte, setReporte] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!fechaInicio || !fechaFin) {
      setError("Por favor ingresa ambas fechas.");
      return;
    }

    setLoading(true);
    setError(null);  // Limpiar cualquier error previo

    try {
      const response = await reporteServicio.hacerReporte({ tipo, fechaInicio, fechaFin });
      setReporte(response.data);
    } catch (error) {
      setError("Error al obtener el reporte.");
    } finally {
      setLoading(false);
    }
  };

  const renderTable = () => {
    if (!reporte) return null;

    // Datos del reporte
    const columnasMeses = reporte.ColumnasMeses || [];
    const filasTipo = reporte.Filastipo || [];

    // Dependiendo del tipo, obtenemos las columnas de valores
    let rows = [];
    if (reporte.tipo === "Número de personas") {
      rows = [
        { label: "1-2 personas", values: reporte.personas1a2 },
        { label: "3-5 personas", values: reporte.personas3a5 },
        { label: "6-10 personas", values: reporte.personas6a10 },
        { label: "11-15 personas", values: reporte.personas11a15 },
        { label: "Total", values: reporte.totalesFilas },
      ];
    } else {
      rows = [
        { label: "Vueltas 10-10", values: reporte.vueltas1010 },
        { label: "Vueltas 15-15", values: reporte.vueltas1515 },
        { label: "Vueltas 20-20", values: reporte.vueltas2020 },
      ];
    }

    return (
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Tipo</TableCell>
              {columnasMeses.map((mes, index) => (
                <TableCell key={index}>{mes}</TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((row, rowIndex) => (
              <TableRow key={rowIndex}>
                <TableCell>{row.label}</TableCell>
                {row.values.map((value, colIndex) => (
                  <TableCell key={colIndex}>{value !== null ? value : "N/A"}</TableCell>
                ))}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    );
  };

  return (
    <Box sx={{ padding: 2 }}>
      <Typography variant="h4" gutterBottom>
        Generar Reporte
      </Typography>

      {/* Formulario para ingresar los parámetros */}
      <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
        <TextField
          select
          label="Tipo de Reporte"
          value={tipo}
          onChange={(e) => setTipo(e.target.value)}
          fullWidth
          required
          SelectProps={{
            native: true,
          }}
          sx={{ marginBottom: "20px" }}
        >
          <option value="Número de personas">Número de personas</option>
          <option value="Por cantidad de vueltas o tiempo máximo">Por cantidad de vueltas o tiempo máximo</option>
        </TextField>

        <TextField
          type="date"
          label="Fecha de Inicio"
          value={fechaInicio}
          onChange={(e) => setFechaInicio(e.target.value)}
          fullWidth
          required
          sx={{ marginBottom: "20px" }}
          InputLabelProps={{
            shrink: true,
          }}
        />

        <TextField
          type="date"
          label="Fecha de Fin"
          value={fechaFin}
          onChange={(e) => setFechaFin(e.target.value)}
          fullWidth
          required
          sx={{ marginBottom: "20px" }}
          InputLabelProps={{
            shrink: true,
          }}
        />

        <Button type="submit" variant="contained" color="primary" fullWidth>
          Generar Reporte
        </Button>
      </form>

      {loading && <CircularProgress />}
      {error && <Typography color="error">{error}</Typography>}

      {reporte && (
        <>
          <Typography variant="body1">Fecha de Creación: {reporte.fechaCreacion}</Typography>
          <Typography variant="body1">Fecha de Inicio: {reporte.fechaInicio}</Typography>
          <Typography variant="body1">Fecha de Fin: {reporte.fechaFin}</Typography>

          {renderTable()}
        </>
      )}
    </Box>
  );
};

export default Reporte;
