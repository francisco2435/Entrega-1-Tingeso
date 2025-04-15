import React, { useEffect, useState } from "react";
import { Calendar, dateFnsLocalizer } from "react-big-calendar";
import { Paper, Typography, Button, Box } from "@mui/material";
import "react-big-calendar/lib/css/react-big-calendar.css";
import format from "date-fns/format";
import parse from "date-fns/parse";
import startOfWeek from "date-fns/startOfWeek";
import getDay from "date-fns/getDay";
import addDays from "date-fns/addDays";
import es from "date-fns/locale/es";
import reservaServicio from "../services/reserva.servicio";

// Localizador para fechas en español
const locales = {
  es: es,
};

const localizer = dateFnsLocalizer({
  format,
  parse,
  startOfWeek: () => startOfWeek(new Date(), { weekStartsOn: 1 }),
  getDay,
  locales,
});

const RackSemanal = () => {
  const [eventos, setEventos] = useState([]);
  const [date, setDate] = useState(new Date());

  useEffect(() => {
    reservaServicio
      .obtenerReservas()
      .then((res) => {
        const reservas = res.data;
        const eventosFormateados = reservas.map((reserva) => {
          const fechaInicio = new Date(`${reserva.fechaReserva}T${reserva.horaInicio}`);
          const fechaFin = new Date(`${reserva.fechaReserva}T${reserva.horaFin}`);

          return {
            title: reserva.nombreCliente,
            start: fechaInicio,
            end: fechaFin,
            desc: `Reserva para ${reserva.cantidadPersonas} personas`,
          };
        });

        setEventos(eventosFormateados);
      })
      .catch((error) => {
        console.error("Error al obtener las reservas:", error);
      });
  }, []);

  return (
    <Paper sx={{ padding: 2, margin: 2 }}>
      <Typography variant="h5" gutterBottom>
        Rack Semanal de Reservas
      </Typography>

      {/* Controles para cambiar semana */}
      <Box display="flex" justifyContent="space-between" alignItems="center" marginBottom={2}>
        <Button variant="outlined" onClick={() => setDate(addDays(date, -7))}>
          Semana anterior
        </Button>
        <Typography variant="subtitle1">
          Semana de {format(date, "dd 'de' MMMM yyyy", { locale: es })}
        </Typography>
        <Button variant="outlined" onClick={() => setDate(addDays(date, 7))}>
          Semana siguiente
        </Button>
      </Box>

      <Calendar
        localizer={localizer}
        events={eventos}
        startAccessor="start"
        endAccessor="end"
        views={["week"]}
        defaultView="week"
        date={date}
        onNavigate={setDate}
        style={{ height: 600 }}
        culture="es"
        min={new Date(0, 0, 0, 10, 0)}  // 10:00 AM
        max={new Date(0, 0, 0, 22, 0)}  // 10:00 PM
        components={{ toolbar: () => null }}
      />
    </Paper>
  );
};

export default RackSemanal;
