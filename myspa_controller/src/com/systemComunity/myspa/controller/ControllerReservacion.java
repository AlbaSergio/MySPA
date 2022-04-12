/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.systemComunity.myspa.controller;

import com.scd.myspa.model.Cliente;
import com.scd.myspa.model.Horario;
import com.scd.myspa.model.Persona;
import com.scd.myspa.model.Reservacion;
import com.scd.myspa.model.Sala;
import com.systemComunity.myspa.db.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DanielAbrahamSanchez
 */
public class ControllerReservacion {

    public void insert(Reservacion r) throws Exception {
        String sql = "INSERT INTO reservacion(estatus, idCliente, idSala, fecha, idHorario) "
                + "VALUES (" + r.getEstatus() + ", " + r.getCliente().getId() + ", " + r.getSala().getId()
                + ", STR_TO_DATE('" + r.getFecha() + "', '%Y-%m-%d'), " + r.getHorario().getId() + ")";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion de la base de datos:
        Connection conn = connMySQL.open();

        Statement stmt = conn.createStatement();

        stmt.execute(sql);
        stmt.close();
        connMySQL.close();
    }

    public List<Horario> getAllHorarios(String fecha, int idSala) throws SQLException, Exception {
        String sql = "SELECT H1.* FROM horario H1\n"
                + "	LEFT JOIN\n"
                + "(SELECT H.*\n"
                + "	FROM horario H\n"
                + "	INNER JOIN reservacion R\n"
                + "	ON H.idHorario = R.idHorario\n"
                + "	WHERE R.idSala = " + idSala + " AND R.fecha = STR_TO_DATE('" + fecha + "', '%Y-%m-%d')) AS SQ2\n"
                + "ON H1.idHorario = SQ2.idHorario\n"
                + "WHERE SQ2.idHorario IS NULL;";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion de la base de datos:
        Connection conn = connMySQL.open();

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);

        List<Horario> horarios = new ArrayList<>();

        while (rs.next()) {
            Horario h = new Horario();
            h.setId(rs.getInt("idHorario"));
            h.setHoraInicio(rs.getString("horaInicio"));
            h.setHoraFin(rs.getString("horaFin"));
            horarios.add(h);
        }

        // Cerramos los objetos de BD:
        rs.close();
        stmt.close();
        connMySQL.close();

        // Devolvemos la lista de horarios:
        return horarios;
    }

    public List<Reservacion> getAll(String filtro) throws Exception {
        // Definimos la consulta SQL:
        String sql = "SELECT * FROM v_reservacion WHERE estatus = 1";

        // Aquí guardaremos objetos de tipo Resrvacion. Una lista es un contenedor dinámico de objetos
        List<Reservacion> reservaciones = new ArrayList<Reservacion>();

        // Una variable temporal para crear nuevas instancias de Reservación:
        Reservacion r = null;

        // Con este objeto vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        // Abrimos la conexión con MySQL:
        Connection conn = connMySQL.open();

        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Ejecutammos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();

        // Recorremos el ResultSet:
        while (rs.next()) {
            r = fill(rs);
            reservaciones.add(r);
        }
        // Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        // Devolvemos la lista de Reservaciones:
        return reservaciones;
    }
    public List<Reservacion> getAllCancelada(String filtro) throws Exception {
        // Definimos la consulta SQL:
        String sql = "SELECT * FROM v_reservacion WHERE estatus = 3";

        // Aquí guardaremos objetos de tipo Resrvacion. Una lista es un contenedor dinámico de objetos
        List<Reservacion> reservaciones = new ArrayList<Reservacion>();

        // Una variable temporal para crear nuevas instancias de Reservación:
        Reservacion r = null;

        // Con este objeto vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        // Abrimos la conexión con MySQL:
        Connection conn = connMySQL.open();

        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Ejecutammos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();

        // Recorremos el ResultSet:
        while (rs.next()) {
            r = fill(rs);
            reservaciones.add(r);
        }
        // Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        // Devolvemos la lista de Reservaciones:
        return reservaciones;
    }
    public List<Reservacion> getAllAtendidas(String filtro) throws Exception {
        // Definimos la consulta SQL:
        String sql = "SELECT * FROM v_reservacion WHERE estatus = 2";

        // Aquí guardaremos objetos de tipo Resrvacion. Una lista es un contenedor dinámico de objetos
        List<Reservacion> reservaciones = new ArrayList<Reservacion>();

        // Una variable temporal para crear nuevas instancias de Reservación:
        Reservacion r = null;

        // Con este objeto vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        // Abrimos la conexión con MySQL:
        Connection conn = connMySQL.open();

        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Ejecutammos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();

        // Recorremos el ResultSet:
        while (rs.next()) {
            r = fill(rs);
            reservaciones.add(r);
        }
        // Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        // Devolvemos la lista de Reservaciones:
        return reservaciones;
    }

    /**
     * Crear un objeto de tipo Reservacion y llena sus propiedades utilizando
     * los datos proporcionados por un ResultSet.
     *
     * @param rs
     * @return
     */
    private Reservacion fill(ResultSet rs) throws SQLException {
        // Una variable temporal para crear nuevos objetos de tipo Reservacion
        Reservacion r = new Reservacion();

        // Una variable temporal para crear nuevos objetos de tipo Persona
        Persona p = new Persona();

        // Una variable temporal para crear nuevos objetos de tipo Cliente:
        Cliente c = new Cliente();

        // Una variable temporal para crear nuevos objetos de tipo Sala:
        Sala s = new Sala();

        // Una variable temporal para crear nuevos objetos de tipo Sala:
        Horario h = new Horario();

        // Llenamos los datos de Persona:
        p.setId(rs.getInt("idPersona"));
        p.setNombre(rs.getString("nombre"));
        p.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p.setApellidoMaterno(rs.getString("apellidoMaterno"));
        p.setGenero(rs.getString("genero"));
        p.setDomicilio(rs.getString("domicilio"));
        p.setTelefono(rs.getString("telefono"));
        p.setRfc(rs.getString("rfc"));

        // Llenamos los datos de Cliente
        c.setId(rs.getInt("idCliente"));
        c.setCorreo(rs.getString("correo"));
        c.setNumeroUnico(rs.getString("numeroUnico"));

        // Llenamos los datos de Horario
        h.setId(rs.getInt("idHorario"));
        h.setHoraInicio(rs.getString("horaInicio"));
        h.setHoraFin(rs.getString("horaFin"));

        // Llenamos los datos de Sala
        s.setId(rs.getInt("idSala"));
        s.setNombre(rs.getString("nombreSala"));
        s.setDescripcion(rs.getString("descripcion"));
        s.setFoto(rs.getString("fotoSala"));
        s.setRutaFoto(rs.getString("rutaFotoSala"));
        s.setEstatus(rs.getInt("estatusSala"));

        // Llenamos los datos de Reservacion
        r.setId(rs.getInt("idReservacion"));
        r.setFecha(rs.getString("fecha"));
        r.setEstatus(rs.getInt("estatus"));

        // Establecemos su persona al Cliente:
        c.setPersona(p);

        // Establecemos sus datos corresondientes de Cliente, Sala y Horario a la Reservacion:
        r.setCliente(c);
        r.setSala(s);
        r.setHorario(h);

        return r;
    }

    public void delete(int idReservacion) throws Exception {
        //Definimos la consulta SQL que realiza la inserción del registro:
        String sql = "UPDATE reservacion SET estatus = 3 WHERE idReservacion = ?";

        // Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        // Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();

        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Obtenemos el id del producto para hacer su eliminación lógica cambiando el estatus a 0:
        pstmt.setInt(1, idReservacion);

        // Ejecutamos la consulta SQL:
        pstmt.executeUpdate();

        // Cerramos todos los objetos de conexión con la Base de Datos:
        pstmt.close();
        connMySQL.close();
    }
}
