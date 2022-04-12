/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.systemComunity.myspa.controller;

import com.scd.myspa.model.Cliente;
import com.scd.myspa.model.Empleado;
import com.scd.myspa.model.Horario;
import com.scd.myspa.model.Persona;
import com.scd.myspa.model.Producto;
import com.scd.myspa.model.Reservacion;
import com.scd.myspa.model.Sala;
import com.scd.myspa.model.Servicio;
import com.scd.myspa.model.ServicioTratamiento;
import com.scd.myspa.model.Tratamiento;
import com.systemComunity.myspa.db.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eveli
 */
public class ControllerServicio {
    
    public int insert(Servicio s) throws Exception {
        // Preparamos las consultas:
        String sqlServicio = "INSERT INTO servicio(fecha, idReservacion, idEmpleado) "
                + "VALUES(?, ?, ?)";
        
        String sqlServicioTratamiento = "INSERT INTO servicio_tratamiento(idTratamiento, idServicio) "
                + "VALUES(?, ?)";
        
        String sqlServicioTratamientoProducto = "INSERT INTO servicio_tratamiento_producto(idServicioTratamiento, idProducto, precioUso) "
                + "VALUES(?, ?, ?)";
        
        String sqlReservacion = "UPDATE reservacion SET estatus = 2 WHERE idReservacion = ?";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion de la base de datos:
        Connection conn = connMySQL.open();
        
        // Preparamos los satatements que vamos a ocupar:
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        
        // Ejecutaremos el ResultSet:
        ResultSet rs = null;
        
        // Preparamos objetos para colocar las listas que se incluyen en el servicio y un contador
        int cont = 0;
        List<ServicioTratamiento> serviciosTratamientos = null;
        List<Producto> productos = null;
        
        // Deshabilitamos la persistencia automatica de los datos para comenzar la transaccion
        conn.setAutoCommit(false);
        
        try {
            // Generamos un PreparedStatement que ejecutara la consulta************************************
            // y le indicamos que nos devuelva los ID's que se generen (1).
            pstmt1 = conn.prepareStatement(sqlServicio, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, s.getFecha());
            pstmt1.setInt(2, s.getReservacion().getId());
            pstmt1.setInt(3, s.getEmpleado().getId());
                                    
            // Ejecutamos la consulta:
            pstmt1.executeUpdate();
            
            // Recuperamos el ID que se genero del servicio:
            rs = pstmt1.getGeneratedKeys();
            rs.next();
            s.setId(rs.getInt(1));
            
            // Cerramos el ResultSet:
            rs.close();
            
            // Preparamos la segunda consulta**************************************************************
            pstmt2 = conn.prepareStatement(sqlServicioTratamiento, PreparedStatement.RETURN_GENERATED_KEYS);
            
            // Preparamos un lote de ejecuciones SQL:
            for (int i = 0; i < s.getServicioTratamiento().size(); i++) {
                // Llenamos los datos del PreparedStatement:
                pstmt2.setInt(1, s.getServicioTratamiento().get(i).getTratamiento().getId());
                pstmt2.setInt(2, s.getId());
                
                // Agregamos la consulta al lote:
                pstmt2.addBatch();
            }
            
            // Ejecutamos todo el lote de instrucciones:
            pstmt2.executeBatch();
            
            // Recuperamos los ID's generados:
            rs = pstmt2.getGeneratedKeys();
            
            // Vamos asignando cada uno de los ID's de servicioTratamiento generados
            while (rs.next())
                s.getServicioTratamiento().get(cont++).setId(rs.getInt(1));
            
            // Cerramos el ResultSet:
            rs.close();
            
            // Preparamos la tercer consulta**************************************************************
            pstmt3 = conn.prepareStatement(sqlServicioTratamientoProducto);
            serviciosTratamientos = s.getServicioTratamiento();
            
            // Recorremos la lista que contiene los ServicioTratamiento:
            for (int i = 0; i < s.getServicioTratamiento().size(); i++) {
                // Recuperamos el ServicioTratamiento de la posicion i y le pedimos sus productos:
                productos = serviciosTratamientos.get(i).getProducto();
                
                // Recorremos la lista de productos:
                for (int j = 0; j < productos.size(); j++) {
                    // Llenamos los parametros del PreparedStatement:
                    pstmt3.setInt(1, serviciosTratamientos.get(i).getId());
                    pstmt3.setInt(2, productos.get(j).getId());
                    pstmt3.setDouble(3, productos.get(j).getPrecioUso());
                    
                    // Agregamos la consulta al lote:
                    pstmt3.addBatch();
                }
            }
            // Ejecutamos el lote de instrucciones:
            pstmt3.executeBatch();
            
            // Preparamos la cuarta consulta**************************************************************
            pstmt4 = conn.prepareStatement(sqlReservacion);
            pstmt4.setInt(1, s.getReservacion().getId());
            pstmt4.executeUpdate();
            
            // Persistimos los cambios realizados en la BD:
            conn.commit();
        
        } catch (Exception e){
            // Si algo falla, imprimimos la excepcion:
            e.printStackTrace();
            
            // Hacemos un RollBack:
            conn.rollback();
            
            // Cerramos la conexion:
            connMySQL.close();
            
            // Lanzamos la excepcion:
            throw e;
        }
        
        conn.setAutoCommit(true);
        
        pstmt1.close();
        pstmt2.close();
        pstmt3.close();
        pstmt4.close();
        conn.close();
        connMySQL.close();
        
        return s.getId();
    }
    
     public List<Servicio> getAll(String filtro) throws Exception {
        // Definimos la consulta SQL:
        String sql = "SELECT * FROM v_servicios";

        // Aquí guardaremos objetos de tipo Resrvacion. Una lista es un contenedor dinámico de objetos
        List<Servicio> servicios = new ArrayList<Servicio>();

        // Una variable temporal para crear nuevas instancias de Reservación:
        Servicio s = null;

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
            s = fill(rs);
            servicios.add(s);
        }
        // Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        // Devolvemos la lista de Reservaciones:
        return servicios;
    }
    
    private Servicio fill(ResultSet rs) throws SQLException {
        Servicio s = new Servicio();
        // Una variable temporal para crear nuevos objetos de tipo Reservacion
        Reservacion r = new Reservacion();

        // Una variable temporal para crear nuevos objetos de tipo Persona
        Persona p = new Persona();
        Persona p1 = new Persona();

        // Una variable temporal para crear nuevos objetos de tipo Cliente:
        Cliente c = new Cliente();
        
        //Una variable temporal para crear nuevos objetos de tipo Empleado:
        Empleado e = new Empleado();

        // Una variable temporal para crear nuevos objetos de tipo Sala:
        Sala sa = new Sala();

        // Una variable temporal para crear nuevos objetos de tipo Sala:
        Horario h = new Horario();
        
        Producto pr = new Producto();
        
        Tratamiento t = new Tratamiento();
        
        ServicioTratamiento st = new ServicioTratamiento();

        
        
        
        // Llenamos los datos de Persona:
        p.setId(rs.getInt("idPersona"));
        p.setNombre(rs.getString("nombre"));
        p.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p.setApellidoMaterno(rs.getString("apellidoMaterno"));
        
        p1.setId(rs.getInt("idPersona"));
        p1.setNombre(rs.getString("nombre"));
        p1.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p1.setApellidoMaterno(rs.getString("apellidoMaterno"));
      

        // Llenamos los datos de Cliente
        c.setId(rs.getInt("idCliente"));
        c.setNumeroUnico(rs.getString("numeroUnico"));
        
        //Llenamos  los datos de Empleado
        e.setId(rs.getInt("idEmpleado"));
        e.setNumeroEmpleado(rs.getString("numeroEmpleado"));
        

        // Llenamos los datos de Horario
        h.setId(rs.getInt("idHorario"));
        h.setHoraInicio(rs.getString("horaInicio"));
        h.setHoraFin(rs.getString("horaFin"));

        // Llenamos los datos de Sala
        sa.setId(rs.getInt("idSala"));
        sa.setNombre(rs.getString("nombreSala"));

        // Llenamos los datos de Reservacion
        r.setId(rs.getInt("idReservacion"));
        
        
        st.setId(rs.getInt("idServicioTratamiento"));
        st.setProducto((List<Producto>) pr);
        st.setTratamiento(t);

        // Establecemos su persona al Cliente:
        c.setPersona(p);
        e.setPersona(p1);

        // Establecemos sus datos corresondientes de Cliente, Sala y Horario a la Reservacion:
        r.setCliente(c);
        r.setSala(sa);
        r.setHorario(h);
        
        s.setReservacion(r);
        s.setId(rs.getInt("idServicio"));
        s.setEmpleado(e);
        s.setServicioTratamiento((List<ServicioTratamiento>) st);

        return s;
    }

    
}