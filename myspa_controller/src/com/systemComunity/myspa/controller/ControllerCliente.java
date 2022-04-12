package com.systemComunity.myspa.controller;

import com.systemComunity.myspa.db.ConexionMySQL;
import com.scd.myspa.model.Cliente;
import com.scd.myspa.model.Persona;
import com.scd.myspa.model.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControllerCliente {
    //Inserta un registro en la base d datos y devuelve el ID generado
    public int insert(Cliente c) throws Exception {
        String sql = "{call insertarCliente(?, ?, ?, ?, ?, ?, ?, " +// Datos Persona
                                            "?, ?, ?," +//Datos Usuario
                                            "?, ?, ?," +//Datos Cliente
                                            "?, ?, ?, ?)}"; //Valores de Retorno

        //Aquí guardaremos los ID's que se generán: 
        int idPersonaGenerado = -1;
        int idUsuarioGenerado = -1;
        int idClienteGenerado = -1;
        
        String numClienteGenerado = "";

        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos el StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden en que
        //los pide el procedimiento almacenado comenzando en 1:
        cstmt.setString(1, c.getPersona().getNombre());
        cstmt.setString(2, c.getPersona().getApellidoPaterno());
        cstmt.setString(3, c.getPersona().getApellidoMaterno());
        cstmt.setString(4, c.getPersona().getGenero());
        cstmt.setString(5, c.getPersona().getDomicilio());
        cstmt.setString(6, c.getPersona().getTelefono());
        cstmt.setString(7, c.getPersona().getRfc());

        //Establecemos los parámetros de los datos de Usuario:
        cstmt.setString(8, c.getUsuario().getNombreUsuario());
        cstmt.setString(9, c.getUsuario().getContrasenia());
        cstmt.setString(10, c.getUsuario().getRol());

        //Establecemos los parámetros de los datos de Empleado:
        cstmt.setString(11, c.getCorreo());
        cstmt.setString(12, c.getFoto());
        cstmt.setString(13, c.getRutaFoto());

        //Registramos los parámetros de salida:
       
        cstmt.registerOutParameter(14, Types.INTEGER);
        cstmt.registerOutParameter(15, Types.INTEGER);
        cstmt.registerOutParameter(16, Types.INTEGER);
        cstmt.registerOutParameter(17, Types.INTEGER);
        
        //Ejecutamos el StoredProcedure
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idPersonaGenerado = cstmt.getInt(14);
        idUsuarioGenerado = cstmt.getInt(15);
        idClienteGenerado = cstmt.getInt(16);
        numClienteGenerado = cstmt.getString(17);

        //Los guardamos en el objeto Empleado que nos pasaron como parámetro:
        c.getPersona().setId(idPersonaGenerado);
        c.getUsuario().setId(idUsuarioGenerado);
        c.setId(idClienteGenerado);
        c.setNumeroUnico(numClienteGenerado);

        //Cerramos los objetos de Base de datos:
        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Empleado generado:
        return idClienteGenerado;

    }

    public void delete(int idCliente) throws Exception {
         String sql = "UPDATE cliente SET estatus = 0 WHERE idCliente = ?";

        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, idCliente);
        pstmt.executeUpdate();

        pstmt.close();
        connMySQL.close();
    }

    //Actualiza un registro de sucursal en la BD
    public void update(Cliente c) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call actualizarCliente(?, ?, ?, ?, ?, ?, ?, "
                +"?, ?, ?,"
                +"?, ?, ?,"
                +"?, ?, ?)}"; //Valores de Retorno

         //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos el StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden en que
        //los pide el procedimiento almacenado comenzando en 1:
        cstmt.setString(1, c.getPersona().getNombre());
        cstmt.setString(2, c.getPersona().getApellidoPaterno());
        cstmt.setString(3, c.getPersona().getApellidoMaterno());
        cstmt.setString(4, c.getPersona().getGenero());
        cstmt.setString(5, c.getPersona().getDomicilio());
        cstmt.setString(6, c.getPersona().getTelefono());
        cstmt.setString(7, c.getPersona().getRfc());

        //Establecemos los parámetros de los datos de Usuario:
        cstmt.setString(8, c.getUsuario().getNombreUsuario());
        cstmt.setString(9, c.getUsuario().getContrasenia());
        cstmt.setString(10, c.getUsuario().getRol());

        //Establecemos los parámetros de los datos de Cliente:
        cstmt.setString(11, c.getCorreo());
        cstmt.setString(12, c.getFoto());
        cstmt.setString(13, c.getRutaFoto());
        
        //Registramos los parametros de Salida
        cstmt.setInt(14, c.getPersona().getId());
        cstmt.setInt(15, c.getUsuario().getId());
        cstmt.setInt(16, c.getId());
        //Ejecutamos el StoredProcedure
        cstmt.executeUpdate();

        //Cerramos los objetos de Base de datos:
        cstmt.close();
        connMySQL.close();
    }   
    
    public boolean validateToken(String token) throws Exception {
        boolean valid = false;
        
        // Generamos la consulta:
        String sql = "SELECT * FROM v_clientes WHERE token = '" + token + "';";
        
        // Generamos el objeto de la conexión:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión:
        Connection conn = connMySQL.open();
        
        // Objeto para ejecutar la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        // Ejecutammos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();
        
        // Recorremos el ResultSet:
        while (rs.next()) {
            valid = true;
        }
        
        // Cerramos los objetos de BD:
        pstmt.close();
        conn.close();
        connMySQL.close();      

        // Devolvemos la validación del token:
        return valid;
    }

     // Buscar una sucursal por su ID
    public Cliente findByID(int id) throws Exception {
        return null;
    }
       
    // Buscar un cliente por su nombre
    public List<Cliente> findCliente(String nombre) throws Exception {
        // Consulta SQL a ejecutar:
        String sql = "SELECT * FROM v_clientes WHERE nombre LIKE '%" + nombre + "%'";
        
        // Aquí guardaremos objetos de tipo Cliente. Una lista es un contenedor dinámico de objetos
        List<Cliente> clientes = new ArrayList<Cliente>();

        // Una variable temporal para crear nuevas instancias de Cliente:
        Cliente c = null;
        
        // Generamos el objeto de la conexión:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión:
        Connection conn = connMySQL.open();
        
        // Objeto para ejecutar la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        // Ejecutammos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();
        
        //Recorremos el ResultSet:
        while (rs.next()) {
            c = fill(rs);
            //Agregamos el objeto de tipo Empleado a la lista dinámica:
            clientes.add(c);
        }
        //Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        //Devolvemos la lista de Sucursales:
        return clientes;
    }
    
    
    // Consulta todos los registros de clientes en la BD y devuelve una 
    // lista dinamica de objetos de tipo Cliente.
    public List<Cliente> getAll(String filtro) throws Exception {
        //Definimos la consulta SQL:
        String sql = "SELECT * FROM v_clientes WHERE estatus = 1";

        //Aquí guardaremos objetos de tipo Cliente. Una lista es un contenedor
        //dinámico de objetos
        List<Cliente> clientes = new ArrayList<Cliente>();

        //Una variable temporal para crear nuevas instancias de Cliente:
        Cliente c = null;

        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con MySQL:
        Connection conn = connMySQL.open();

        //Declaramos e inicializamos el objeto con el que ejecutaremos 
        //la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Ejecutammos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();

        //Recorremos el ResultSet:
        while (rs.next()) {
            c = fill(rs);
            //Agregamos el objeto de tipo Empleado a la lista dinámica:
            clientes.add(c);
        }
        //Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        //Devolvemos la lista de Sucursales:
        return clientes;
    }
    
    private Cliente fill(ResultSet rs) throws SQLException {
        //Una variable temporal para crear nuevos objetos de tipo Empleado
        Cliente c = new Cliente();

        //Una variable temporal para crear nuevos objetos de tipo Persona
        Persona p = new Persona();

        //Una variable temporal para crear nuevos objetos de tipo usuario:
        Usuario u = new Usuario();

        //Llenamos sus datos:
        p.setApellidoMaterno(rs.getString("apellidoMaterno"));
        p.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p.setDomicilio(rs.getString("domicilio"));
        p.setGenero(rs.getString("genero"));
        p.setId(rs.getInt("idPersona"));
        p.setNombre(rs.getString("nombre"));
        p.setRfc(rs.getString("rfc"));
        p.setTelefono(rs.getString("telefono"));

        //Creamos un nuevo objeto de tipo Usuario
        u.setContrasenia(rs.getString("contrasenia"));
        u.setId(rs.getInt("idUsuario"));
        u.setNombreUsuario(rs.getString("nombreUsuario"));
        u.setRol(rs.getString("rol"));

        //Establecemos sus datos personales
        c.setFoto(rs.getString("foto"));
        c.setCorreo(rs.getString("correo"));
        c.setId(rs.getInt("idCliente"));
        c.setNumeroUnico(rs.getString("numeroUnico"));
        c.setEstatus(rs.getInt("estatus"));
        c.setRutaFoto(rs.getString("rutaFoto"));
        
        //Establecemos su persona:
        c.setPersona(p);

        //Establecemos su Usuario:
        c.setUsuario(u);

        return c;
    }
}
