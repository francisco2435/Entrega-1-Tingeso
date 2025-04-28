import React from "react";

const Home = () => {
  return (
    <div style={{ maxWidth: "900px", margin: "auto", padding: "20px" }}>
      <h1>KartingRM: Sistema de Gestión de Reservas y Recursos</h1>
      
      <p>
        KartingRM, un negocio líder en la industria del karting, enfrenta desafíos significativos en la 
        gestión eficiente de sus operaciones debido al aumento en la demanda y a las crecientes 
        expectativas de los clientes. Actualmente, los procesos de reserva, asignación de horarios y 
        seguimiento del estado de los karts se realizan de manera manual, generando retrasos en 
        las confirmaciones, errores en la disponibilidad y una experiencia insatisfactoria para los clientes.
      </p>
      
      <p>
        Para abordar estas dificultades y optimizar la experiencia del cliente, KartingRM busca 
        desarrollar un sistema integral que permita gestionar reservas, asignar horarios y administrar 
        los recursos de manera eficiente, asegurando una operación fluida y una experiencia de cliente superior.
      </p>

      <h2>Karts Disponibles</h2>
      <p>
        El negocio cuenta con un total de <strong>15 karts individuales</strong> modelo <strong>Sodikart RT8</strong>, diseñados para brindar una 
        experiencia emocionante y segura a cada cliente.
      </p>

      <h2>Horario de Atención</h2>
      <ul>
        <li><strong>Lunes a Viernes:</strong> 14:00 a 22:00 horas</li>
        <li><strong>Sábados, Domingos y Feriados:</strong> 10:00 a 22:00 horas</li>
      </ul>

      <h2>Los panchos son los mejores</h2>

      <h3>Por Número de Personas</h3>
      <table border="1" cellPadding="8" style={{ borderCollapse: "collapse", marginBottom: "20px" }}>
        <thead>
          <tr>
            <th>Número de Personas</th>
            <th>Descuento Aplicado</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1-2 personas</td>
            <td>0%</td>
          </tr>
          <tr>
            <td>3-5 personas</td>
            <td>10%</td>
          </tr>
          <tr>
            <td>6-10 personas</td>
            <td>20%</td>
          </tr>
          <tr>
            <td>11-15 personas</td>
            <td>30%</td>
          </tr>
        </tbody>
      </table>

      <h3>Por Frecuencia de Visita</h3>
      <table border="1" cellPadding="8" style={{ borderCollapse: "collapse", marginBottom: "20px" }}>
        <thead>
          <tr>
            <th>Categoría de Cliente</th>
            <th>Número de Visitas al Mes</th>
            <th>Descuento Aplicado</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Muy frecuente</td>
            <td>7 o más veces</td>
            <td>30%</td>
          </tr>
          <tr>
            <td>Frecuente</td>
            <td>5 a 6 veces</td>
            <td>20%</td>
          </tr>
          <tr>
            <td>Regular</td>
            <td>2 a 4 veces</td>
            <td>10%</td>
          </tr>
          <tr>
            <td>No frecuente</td>
            <td>0 a 1 vez</td>
            <td>0%</td>
          </tr>
        </tbody>
      </table>

      <h3>Promoción Especial de Cumpleaños</h3>
      <p>
        Últimamente se tiene la promoción para que personas que <strong>cumplen años</strong> el día que usarán el kartódromo 
        tengan un <strong>50% de descuento especial</strong>. Este descuento funciona de la siguiente manera:
      </p>
      <ul>
        <li>En un grupo de 3 a 5 personas, se aplica el descuento a 1 persona que cumple años.</li>
        <li>En un grupo de 6 a 10 personas, se aplica el descuento a hasta 2 personas que cumplen años.</li>
      </ul>
    </div>
  );
};

export default Home;
