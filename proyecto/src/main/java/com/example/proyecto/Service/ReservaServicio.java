package com.example.proyecto.Service;

import com.example.proyecto.Entity.Reserva;
import com.example.proyecto.Entity.Tarifa;
import com.example.proyecto.Entity.Usuario;
import com.example.proyecto.Repository.KartRepositorio;
import com.example.proyecto.Repository.ReservaRepositorio;
import com.example.proyecto.Repository.TarifaRepositorio;
import com.example.proyecto.Repository.UsuarioRepositorio;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaServicio {
    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private KartRepositorio kartRepositorio;

    @Autowired
    private TarifaRepositorio tarifaRepositorio;

    @Autowired
    private JavaMailSender mailSender;


    // hacer Reserva
    public Reserva hacerReserva(String rutCliente, String nombreCliente, LocalDate fecha, LocalTime horaInicio, int tiempoMax,
                                int numVueltas, int cantidadPersonas, List<String> rutsAmigos, List<String> nombres) {
    // Variables
        int numKartsDisponibles = obtenerCantidadKartsDisponibles(fecha, horaInicio, horaInicio);
        Tarifa tarifa = obtenerTarifa(fecha,tiempoMax, numVueltas); // # Obtener tarifa correspondiente #
        LocalTime horaFin = horaInicio.plusMinutes(tarifa.duracionReserva); //hora inicio mas la duracion total de la reserva
        double descuento;
        int numDescuentosCumplAplicados = 0;
        List<String> nombreDescuentoTamanoGrupo = new ArrayList<>();
        List<Double> valorDescuentoTamanoGrupo = new ArrayList<>();
        List<String> nombreDescuentoEspeciales = new ArrayList<>(); //por ser cliente frecuente o promociones especiales
        List<Double> valorDescuentoEspeciales = new ArrayList<>();
        double montoTotal = 0;
        double valorIva = 0.19; //un 19%, según lo fija el artículo 14 de la Ley sobre Impuesto a las Ventas y Servicios
        double montoTotalConIva = 0;


        //Añadir al inicio al cliente quien está haciendo la reserva
        rutsAmigos.add(0, rutCliente);
        nombres.add(0, nombreCliente);

    // # Comprobar valores #

    // Comprobar que la cantidad de ruts ingresados sea igual al de los nombres ingresados
        if(rutsAmigos.size() != nombres.size()){
            System.out.println("La cantidad de ruts ingresados no es igual al de los nombres ingresados");
            return null;
        }

    // Comprobar la disponibilidad de karts segun el numero de integrantes.
        if(numKartsDisponibles < cantidadPersonas){

            System.out.println("No hay suficientes karts para la cantidad de personas ingresadas " + numKartsDisponibles);
            return null;
        }
    // Comprobar (número del grupo) y (ids ingresados más el usuario) sean iguales
        if(rutsAmigos.size() != cantidadPersonas){
            System.out.println("El numero de ruts ingresados no coinciden con el numero de personas ingresadas");
            return null;
        }
    // Comprobar que el total de integrantes sea menor o igual a 15
        if(cantidadPersonas > 15){
            System.out.println("El numero de personas ingresadas es mayor que 15");
            return null;
        }


    // # Comprobar horarios #


    // Comprobar que las fechas seleccionadas estén dentro del horario de trabajo
        if(!comprobarHorarioTrabajo(fecha, horaInicio, horaFin)){
            System.out.println("la reserva se está realizando fuera del horario de trabajo");
            return null;
        }

    // Comprobar tope de horario
        if(!comprobarTopeHorario(fecha, horaInicio, horaFin, cantidadPersonas+1)){
            System.out.println("la reserva tiene tope de horario con otra reserva ya realizada");
            return null;
        }

    // # Aplicar descuentos y calculo del monto total#


        // Aplicar descuentos a cada integrante del grupo
        for (String rut : rutsAmigos) {

            // Obtener descuento
            descuento = calcularDescuento(rut, cantidadPersonas, fecha, numDescuentosCumplAplicados);
            double parteEntera = Math.floor(descuento); //parte entera para identificar el tipo de descuento que se aplicó
            descuento = descuento - parteEntera; // descuento real a aplicar
            descuento = Math.round(descuento * 100.0) / 100.0;

            Usuario usuarioEncontrado = usuarioRepositorio.findByRut(rut);
            if (usuarioEncontrado == null) {  // Verifica que el usuario esté registrado
                //descuento por tamaño de grupo
                if(parteEntera == 1.0){
                    nombreDescuentoTamanoGrupo.add("Por número de personas");
                    valorDescuentoTamanoGrupo.add(descuento);
                    nombreDescuentoEspeciales.add("No Aplica");
                    valorDescuentoEspeciales.add(0.0);
                    montoTotal += Math.round((tarifa.precio - tarifa.precio * descuento) * 100.0) / 100.0;

                } else{ //Si no hay descuento identificado, sumar la tarifa completa
                    nombreDescuentoTamanoGrupo.add("No Aplica");
                    valorDescuentoTamanoGrupo.add(0.0);
                    nombreDescuentoEspeciales.add("No Aplica");
                    valorDescuentoEspeciales.add(0.0);
                    montoTotal += Math.round((tarifa.precio) * 100.0) / 100.0;
                }
            } else{
                //descuento por tamaño de grupo
                if(parteEntera == 1.0){
                    nombreDescuentoTamanoGrupo.add("Por número de personas");
                    valorDescuentoTamanoGrupo.add(descuento);
                    nombreDescuentoEspeciales.add("No Aplica");
                    valorDescuentoEspeciales.add(0.0);
                    montoTotal += Math.round((tarifa.precio - tarifa.precio * descuento) * 100.0) / 100.0;

                }else if(parteEntera == 2.0){
                    nombreDescuentoTamanoGrupo.add("No Aplica");
                    valorDescuentoTamanoGrupo.add(0.0);
                    nombreDescuentoEspeciales.add("Cliente Frecuente");
                    valorDescuentoEspeciales.add(descuento);
                    montoTotal += Math.round((tarifa.precio - tarifa.precio * descuento) * 100.0) / 100.0;

                }else if(parteEntera == 3.0){
                    nombreDescuentoTamanoGrupo.add("No Aplica");
                    valorDescuentoTamanoGrupo.add(0.0);
                    nombreDescuentoEspeciales.add("Descuento de cumpleaños");
                    valorDescuentoEspeciales.add(descuento);
                    numDescuentosCumplAplicados ++;
                    montoTotal += Math.round((tarifa.precio - tarifa.precio * descuento) * 100.0) / 100.0;

                } else{ //Si no hay descuento identificado, sumar la tarifa completa
                    nombreDescuentoTamanoGrupo.add("No Aplica");
                    valorDescuentoTamanoGrupo.add(0.0);
                    nombreDescuentoEspeciales.add("No Aplica");
                    valorDescuentoEspeciales.add(0.0);
                    montoTotal += Math.round((tarifa.precio) * 100.0) / 100.0;
                }
            }
        }

        rutsAmigos.remove(0);
        nombres.remove(0);

        String tipoTarifa = tarifa.tipo;


        // Al final, redondear el monto total con IVA
        montoTotalConIva = Math.round((montoTotal + montoTotal * valorIva) * 100.0) / 100.0;

        Reserva reserva = new Reserva(rutCliente, nombreCliente, horaInicio, horaFin,
        tarifa.duracionReserva, rutsAmigos, fecha, horaInicio, numVueltas,
        tiempoMax, cantidadPersonas, nombres, nombreDescuentoTamanoGrupo,
                valorDescuentoTamanoGrupo, nombreDescuentoEspeciales,
                valorDescuentoEspeciales, montoTotal, valorIva, montoTotalConIva,tipoTarifa);

        Usuario usuario = usuarioRepositorio.findByRut(rutCliente);

        if(usuario == null){
            System.out.println("rut ingresado no está registrado");
            return null;
        }

        enviarComprobanteReserva(reserva, usuario);

        return reservaRepositorio.save(reserva);
    }


    // Obtener descuentos
    // Devolverá el descuento que se debería aplicar en el formato [indice el tipo de descuento. valor del descuento menos el indice]
    public double calcularDescuento(String rutIntegrante, int cantidadIntegrantes, LocalDate fecha, int descuentosCumpleanosAplicados) {
        double maxDescuento = 0.0;
        // Lista para almacenar descuentos y luego obtener el máximo
        List<Double> descuentos = new ArrayList<>();

        // Descuentos por número de personas
        if (cantidadIntegrantes >= 3 && cantidadIntegrantes <= 5) {
            descuentos.add(1.1);
        } else if (cantidadIntegrantes >= 6 && cantidadIntegrantes <= 10) {
            descuentos.add(1.2);
        } else if (cantidadIntegrantes > 10) {
            descuentos.add(1.3);
        }

        // Obtener fecha de nacimiento del usuario
        Usuario usuario = usuarioRepositorio.findByRut(rutIntegrante);
        if (usuario == null || usuario.fechaNacimiento == null) {
            // No hay usuario registrado o sin fecha de nacimiento solo prodrá acceder al descuento de numero de personas del grupo
            return descuentos.stream().max(Double::compare).orElse(0.0);
        }
        LocalDate fechaNacimientoRut = usuario.fechaNacimiento;

        // Descuento por frecuencia del cliente
        int frecuencia = calcularFrecuencia(fecha, rutIntegrante);
        if (frecuencia >= 2 && frecuencia <= 4) {
            descuentos.add(2.1);
        } else if (frecuencia >= 5 && frecuencia <= 6) {
            descuentos.add(2.2);
        } else if (frecuencia > 6) {
            descuentos.add(2.3);
        }

        // Descuento de cumpleaños
        boolean esCumpleanos = fechaNacimientoRut.getMonth() == fecha.getMonth() &&
                fechaNacimientoRut.getDayOfMonth() == fecha.getDayOfMonth();

        if (esCumpleanos) {
            if ((cantidadIntegrantes >= 3 && cantidadIntegrantes <= 5 && descuentosCumpleanosAplicados == 0) ||
                    (cantidadIntegrantes >= 6 && cantidadIntegrantes <= 10 && descuentosCumpleanosAplicados < 3)) {
                descuentos.add(3.5);
            }
        }

        // Obtener el descuento máximo
        return descuentos.stream().max(Double::compare).orElse(0.0);
    }

    public int calcularFrecuencia(LocalDate fecha, String rutIntegrante){
        //Obtener las reservas realizadas en el mes
        YearMonth mesIngresado = YearMonth.from(fecha);
        LocalDate inicioMes = mesIngresado.atDay(1);
        LocalDate finMes = mesIngresado.atEndOfMonth();
        int frecuenciaReservasRealizadas = 0;    //cuantas veces aparece su rut en las reservas del mes
        int frecuenciaEnListaRuts = 0;           //Cuantas veces aparece el rut en las listas de ruts

        List<Reserva> reservasDelMes = reservaRepositorio.findByFechaReservaBetween(inicioMes, finMes);

        for (Reserva reserva : reservasDelMes) {
            if (reserva.rutsAmigos != null) { // Evitar agregar valores null
                if(reserva.rutsAmigos.contains(rutIntegrante)){ //revisar si el rut se encuentra en lista de ruts
                    frecuenciaReservasRealizadas++;
                }
            }
        }

        for (Reserva reserva : reservasDelMes) {
            if (reserva.rutCliente.equals(rutIntegrante)) { //revisar si el rut ha hecho alguna otra reserva
                frecuenciaReservasRealizadas++;
            }
        }
        return frecuenciaReservasRealizadas + frecuenciaEnListaRuts;
    }

    // Comprobar tope de horario
    public boolean comprobarTopeHorario(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int numIntegrantes){
        List<Reserva> reservas = reservaRepositorio.findByFechaReserva(fecha);;

        // Recorrer cada reserva y verificar si hay cruce de horarios
        for (Reserva reserva : reservas) {
            LocalTime inicioReserva = reserva.horaInicio;
            LocalTime finReserva = reserva.horaFin;

            // Comprobar si los horarios ingresados se solapan con la reserva
            if (!(horaFin.isBefore(inicioReserva) || horaInicio.isAfter(finReserva))) {
                System.out.println(numIntegrantes + "a" + obtenerCantidadKartsDisponibles(fecha, horaInicio, horaFin));
                if(numIntegrantes <= obtenerCantidadKartsDisponibles(fecha, horaInicio, horaFin)){
                    return true;//se cruzan los horarios pero hay disponibilidad de karts
                } else{
                    return false;
                }
            }
        }
        return true; // No hay tope de horario
    }

    // Comprobar que las fechas seleccionadas estén dentro del horario de trabajo
    // ( Lunes a Viernes: 14:00 a 22:00 horas o Sábados, Domingos y Feriados: 10:00 a 22:00 horas. )
    public boolean comprobarHorarioTrabajo(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        int tipoDeDia = diasEspeciales(fecha); // 0 = día normal, 1 = fin de semana, 2 = feriado
        LocalTime apertura;
        LocalTime cierre = LocalTime.of(22, 0);

        if (tipoDeDia == 0) { // Día normal (Lunes a Viernes)
            apertura = LocalTime.of(14, 0);
        } else { // Fin de semana o feriado (Sábado, Domingo o feriado)
            apertura = LocalTime.of(10, 0);
        }

        // Verificar si el horario está dentro del rango permitido
        return !horaInicio.isBefore(apertura) && !horaFin.isAfter(cierre);
    }

    // obtener la cantidad de karts disponibles dentro de ese horario
    public int obtenerCantidadKartsDisponibles(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        // Obtener la cantidad total de karts disponibles
        int kartsTotal = kartRepositorio.findByEstado("disponible").size();
        int kartsOcupados = 0; // Contador de karts ocupados en el horario dado

        // Obtener las reservas del día
        List<Reserva> reservas = reservaRepositorio.findByFechaReserva(fecha);;

        // Recorrer cada reserva y verificar si hay cruce de horarios
        for (Reserva reserva : reservas) {
            LocalTime inicioReserva = reserva.horaInicio;
            LocalTime finReserva = reserva.horaFin;

            // Comprobar si los horarios ingresados se solapan con la reserva
            if (!(horaFin.isBefore(inicioReserva) || horaInicio.isAfter(finReserva))) {
                kartsOcupados += reserva.cantidadPersonas;
            }
        }

        if(kartsTotal - kartsOcupados < 0){ //Esto nunca debería ocurrir, pero se está comprobando igual
            System.out.println("kartsOcupados es mayor que karts total");
            return 0;
        }

        // retonar la cantidad de karts disponibles en ese horario
        return kartsTotal - kartsOcupados;
    }

    // Obtener todas las reservas RF 7 Rack semanal
    public List<Reserva> ObtenerReservas(){
        return reservaRepositorio.findAll();
    }

    // Obtener tarifa correspondiente
    public Tarifa obtenerTarifa(LocalDate fecha, int tiempoMax, int numVueltas) {
        int tipoDeDia = diasEspeciales(fecha);

        if (numVueltas == 0 && tiempoMax == 0) {
            System.out.println("Debe especificar numVueltas o tiempoMax.");
            return null;
        }
        if(numVueltas != 0){
            if (tipoDeDia == 0) {
                return tarifaRepositorio.findByTipoAndNumeroVueltas("normal", numVueltas);
            } else if (tipoDeDia == 1) {
                return tarifaRepositorio.findByTipoAndNumeroVueltas("fin de semana", numVueltas);
            } else {
                return tarifaRepositorio.findByTipoAndNumeroVueltas("dia especial", numVueltas);
            }
        } else { //Si numVueltas==0, entonces tiempoMax debe ser distinto de cero
            if (tipoDeDia == 0) {
                return tarifaRepositorio.findByTipoAndTiempoMax("normal", tiempoMax);
            } else if (tipoDeDia == 1) {
                return tarifaRepositorio.findByTipoAndTiempoMax("fin de semana", tiempoMax);
            } else {
                return tarifaRepositorio.findByTipoAndTiempoMax("dia especial", tiempoMax);
            }
        }
    }


    // Hacer lista de dias feriados y comprobar si la reserva será en estos días (2), fin de semana (1) o dia normal (0)
    public int diasEspeciales(LocalDate fecha){
        //Comprobar si es fin de semana
        DayOfWeek dia = fecha.getDayOfWeek(); //Comprobar si es fin de semana
        if(dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY){
            return 1;
        }
        //Comprobar si la reserva es día feriado
        List<LocalDate> feriados = obtenerFeriadosChile(fecha.getYear());
        if(feriados.contains(fecha)){
            return 2;
        }
        //Si no es ni fin de semana o feriado, retorno 0 para el día normal
        return 0;
    }

    //Metodo para obtener los feriados de año
    public List<LocalDate> obtenerFeriadosChile(int year) {
        List<LocalDate> feriados = new ArrayList<>();

        // Feriados fijos
        feriados.add(LocalDate.of(year, 1, 1));   // Año Nuevo
        feriados.add(LocalDate.of(year, 4, 18));  // Viernes Santo
        feriados.add(LocalDate.of(year, 4, 19));  // Sábado Santo
        feriados.add(LocalDate.of(year, 5, 1));   // Día del Trabajador
        feriados.add(LocalDate.of(year, 5, 21));  // Día de las Glorias Navales
        feriados.add(LocalDate.of(year, 6, 29));  // San Pedro y San Pablo
        feriados.add(LocalDate.of(year, 7, 16));  // Día de la Virgen del Carmen
        feriados.add(LocalDate.of(year, 8, 15));  // Asunción de la Virgen
        feriados.add(LocalDate.of(year, 9, 18));  // Independencia Nacional
        feriados.add(LocalDate.of(year, 9, 19));  // Día de las Glorias del Ejército
        feriados.add(LocalDate.of(year, 10, 12)); // Encuentro de Dos Mundos
        feriados.add(LocalDate.of(year, 10, 31)); // Día de las Iglesias Evangélicas
        feriados.add(LocalDate.of(year, 11, 1));  // Todos los Santos
        feriados.add(LocalDate.of(year, 12, 8));  // Inmaculada Concepción
        feriados.add(LocalDate.of(year, 12, 25)); // Navidad

        return feriados;
    }

    // Crear comprobante (esperar respuesta del profe, si se debe hacer un pdf y mandar comprobante se hará este metodo, si no, solo se muestra por pantalla)
    //public boolean crearComprobante(Long id, LocalDate fechaReserva, LocalTime horaReserva, int numVueltas, int tiempoMax, int cantidadPersonas,
    //                                String nombreReservante, List<String> nombres, String tarifatipo, double tarifaValor, List<String> nombreDescuentoTamanoGrupo,
    //                                List<Double> valorDescuentoTamanoGrupo, List<String> nombreDescuentoEspeciales, List<Double> valorDescuentoEspeciales,
    //                                double montoTotal, double valorIva, double montoTotalConIva){

    //}

    public PDDocument generarComprobanteReserva(Reserva reserva) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDType1Font font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        // Establecer la fuente antes de mostrar el texto
        contentStream.setFont(font, 12);

        // Título
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("Comprobante de Reserva");
        contentStream.endText();

        // Información de la reserva
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 720);
        contentStream.showText("ID: " + reserva.id);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Nombre reservante: " + reserva.nombreCliente);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Fecha de reserva: " + reserva.fechaReserva.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Hora de reserva: " + reserva.horaReserva);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Número de vueltas: " + reserva.numVueltas);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Tiempo máximo: " + reserva.tiempoMax + " minutos");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Cantidad de personas: " + reserva.cantidadPersonas);

        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Tipo de Tarifa: " + reserva.TipoTarifa);

        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Nombres: " + String.join(", ", reserva.nombres));

        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Descuento por tamaño de grupo: ");
        for (int i = 0; i < reserva.nombreDescuentoTamanoGrupo.size(); i++) {
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText(reserva.nombreDescuentoTamanoGrupo.get(i) + ": " + reserva.valorDescuentoTamanoGrupo.get(i));
        }

        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Descuento especial: ");
        for (int i = 0; i < reserva.nombreDescuentoEspeciales.size(); i++) {
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText(reserva.nombreDescuentoEspeciales.get(i) + ": " + reserva.valorDescuentoEspeciales.get(i));
        }

        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Monto total: " + reserva.montoTotal);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("IVA (19%): " + reserva.valorIva);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Monto total con IVA: " + reserva.montoTotalConIva);

        contentStream.endText();
        contentStream.close();

        return document;
    }

    public void enviarPdfPorCorreo(String correo, PDDocument pdfDocument) throws MessagingException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdfDocument.save(outputStream);
        byte[] pdfBytes = outputStream.toByteArray();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("franciscoriquelmenunez@gmail.com");
        helper.setTo(correo);
        helper.setSubject("Comprobante de Reserva");
        helper.setText("Adjunto encontrará el comprobante de su reserva.");

        // Adjuntar el PDF
        helper.addAttachment("comprobante_reserva.pdf", new ByteArrayDataSource(pdfBytes, "application/pdf"));

        mailSender.send(mimeMessage);
    }

    public void enviarComprobanteReserva(Reserva reserva, Usuario usuario) {
        try {
            // Generar el comprobante PDF
            PDDocument comprobante = generarComprobanteReserva(reserva);

            // Enviar el PDF por correo electrónico
            enviarPdfPorCorreo(usuario.correo, comprobante);

        } catch (MessagingException e) {
            // Manejar excepciones al enviar el correo
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            // Manejar excepciones al generar o escribir el PDF
            System.err.println("Error al generar el archivo PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
