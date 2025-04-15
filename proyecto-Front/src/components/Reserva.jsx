import React, { useState } from 'react';
import {
  Box,
  TextField,
  Button,
  Typography,
  Stack,
  IconButton,
  Snackbar,
  Alert,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import reservaServicio from '../services/reserva.servicio';

const Reserva = () => {
  const usuario = JSON.parse(localStorage.getItem('usuario')) || {};

  const [fechaReserva, setFechaReserva] = useState('');
  const [horaInicio, setHoraInicio] = useState('');
  const [tiempoMax, setTiempoMax] = useState('');
  const [numVueltas, setNumVueltas] = useState('');
  const [cantidadPersonas, setCantidadPersonas] = useState('');

  const [rutAmigo, setRutAmigo] = useState('');
  const [nombreAmigo, setNombreAmigo] = useState('');
  const [rutsAmigos, setRutsAmigos] = useState([]);
  const [nombres, setNombres] = useState([]);

  const [mensajeConfirmacion, setMensajeConfirmacion] = useState(false);

  const agregarAmigo = () => {
    if (rutAmigo && nombreAmigo) {
      setRutsAmigos([...rutsAmigos, rutAmigo]);
      setNombres([...nombres, nombreAmigo]);
      setRutAmigo('');
      setNombreAmigo('');
    }
  };

  const eliminarAmigo = (index) => {
    const nuevosRuts = [...rutsAmigos];
    const nuevosNombres = [...nombres];
    nuevosRuts.splice(index, 1);
    nuevosNombres.splice(index, 1);
    setRutsAmigos(nuevosRuts);
    setNombres(nuevosNombres);
  };

  const handleReserva = async () => {
    const reserva = {
      rutCliente: usuario.rut,
      nombreCliente: usuario.nombre,
      fechaReserva,
      horaInicio,
      tiempoMax: parseInt(tiempoMax),
      numVueltas: parseInt(numVueltas),
      cantidadPersonas: parseInt(cantidadPersonas),
      rutsAmigos,
      nombres,
    };

    try {
    await reservaServicio.hacerReserva(reserva);
       setMensajeConfirmacion(true);
       setFechaReserva('');
       setHoraInicio('');
       setTiempoMax('');
       setNumVueltas('');
       setCantidadPersonas('');
       setRutsAmigos([]);
       setNombres([]);
    } catch (error) {
       console.error('Error al realizar la reserva:', error);
    }
  };

  return (
    <Box sx={{ p: 4, maxWidth: 600, margin: 'auto' }}>
      <Typography variant="h5" mb={2}>
        Realizar Reserva
      </Typography>

      <TextField
        fullWidth
        label="Fecha de Reserva"
        type="date"
        value={fechaReserva}
        onChange={(e) => setFechaReserva(e.target.value)}
        sx={{ mb: 2 }}
        InputLabelProps={{ shrink: true }}
      />

      <TextField
        fullWidth
        label="Hora de Inicio"
        type="time"
        value={horaInicio}
        onChange={(e) => setHoraInicio(e.target.value)}
        sx={{ mb: 2 }}
        InputLabelProps={{ shrink: true }}
      />

      <TextField
        fullWidth
        label="Tiempo Máximo (minutos)"
        type="number"
        value={tiempoMax}
        onChange={(e) => setTiempoMax(e.target.value)}
        sx={{ mb: 2 }}
      />

      <TextField
        fullWidth
        label="Número de Vueltas"
        type="number"
        value={numVueltas}
        onChange={(e) => setNumVueltas(e.target.value)}
        sx={{ mb: 2 }}
      />

      <TextField
        fullWidth
        label="Cantidad de Personas"
        type="number"
        value={cantidadPersonas}
        onChange={(e) => setCantidadPersonas(e.target.value)}
        sx={{ mb: 2 }}
      />

      <Typography variant="h6" mt={3}>
        Añadir Amigos
      </Typography>

      <Stack direction="row" spacing={2} sx={{ mt: 1, mb: 2 }}>
        <TextField
          label="RUT Amigo"
          value={rutAmigo}
          onChange={(e) => setRutAmigo(e.target.value)}
        />
        <TextField
          label="Nombre Amigo"
          value={nombreAmigo}
          onChange={(e) => setNombreAmigo(e.target.value)}
        />
        <Button variant="contained" onClick={agregarAmigo}>
          Añadir
        </Button>
      </Stack>

      {rutsAmigos.map((rut, index) => (
        <Box
          key={index}
          display="flex"
          justifyContent="space-between"
          alignItems="center"
          sx={{
            border: '1px solid #ccc',
            borderRadius: 2,
            p: 1,
            mb: 1,
          }}
        >
          <Typography>
            {rut} - {nombres[index]}
          </Typography>
          <IconButton onClick={() => eliminarAmigo(index)} color="error">
            <DeleteIcon />
          </IconButton>
        </Box>
      ))}

      <Button
        variant="contained"
        color="primary"
        fullWidth
        sx={{ mt: 3 }}
        onClick={handleReserva}
      >
        Realizar Reserva
      </Button>

      <Snackbar
        open={mensajeConfirmacion}
        autoHideDuration={6000}
        onClose={() => setMensajeConfirmacion(false)}
      >
        <Alert
          onClose={() => setMensajeConfirmacion(false)}
          severity="success"
          sx={{ width: '100%' }}
        >
          El comprobante de su reserva ha sido enviado a su correo electrónico: {usuario.correo}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default Reserva;
