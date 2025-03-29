package com.example.proyecto.Service;

import com.example.proyecto.Repository.KartRepositorio;
import com.example.proyecto.Repository.ReservaRepositorio;
import com.example.proyecto.Repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaServicio {
    @Autowired
    ReservaRepositorio reservaRepositorio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    KartRepositorio kartRepositorio;


    // hacer Reserva
    // public Reserva hacerReserva(Usuario cliente, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int tiempoTotal, int numVueltas, int cantidadPersonas, List<Long> idsAmigos){

    // Obtener una lista de usuarios a partir del número del grupo y los ids ingresados

    // # Comprobar números #

    // Comprobar la disponibilidad de karts segun el numero de integrantes.
    // Comprobar (número del grupo) y (ids ingresados más el usuario) sean iguales


    // # Comprobar horarios #

    // Comprobar que las fechas seleccionadas estén dentro del horario de trabajo
    // Comprobar tope de horario
    // Calcular el tiempo maximo


    // # Obtener tarifas y descuentos #

    // Obtener tarifa que se debería aplicar según si es un día normal, fin de semana o feriado, tomar en cuenta la cantidad de personas o el numero de vueltas
    // Obtener descuentos
    // Aplicar descuentos a cada integrante del grupo
    // Calcular la cantidad de veces que el usuario ha visitado el karting en el mes y determinar si se le aplica el descuento correspondiente


    // # Crear comprobante #











    // }


    // Obtener descuentos
    // Devolverá el descuento que se debería aplicar.
    // public String Descuentos(Long idsIntegrante){
    // }

    // Comprobar tope de horario

    // Comprobar que las fechas seleccionadas estén dentro del horario de trabajo
    // ( Lunes a Viernes: 14:00 a 22:00 horas o Sábados, Domingos y Feriados: 10:00 a 22:00 horas. )

    // Comprobar disponibilidad de karts

    // Obtener todas las reservas RF 7

    // Obtener la tarifa correspondiente (normal, dia especial o fin de semana)

    // Hacer lista de dias feriados y comprobar si la reserva será en estos días, fin de semana

    // Calcular la cantidad de veces que el usuario ha visitado el karting en el mes

    // Crear comprobante
}
