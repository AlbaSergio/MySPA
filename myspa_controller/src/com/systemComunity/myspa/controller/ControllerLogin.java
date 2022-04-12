package com.systemComunity.myspa.controller;

import com.systemComunity.myspa.db.ConexionMySQL;
import com.scd.myspa.model.Empleado;
import com.scd.myspa.model.Persona;
import com.scd.myspa.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerLogin {

    public Empleado login(String nombreUsuario, String contrasenia) throws Exception {
        //Consulta todos los registros de empleado en la BD 

        //Definimos la consulta SQL:
        String sql = "SELECT * FROM v_empleados WHERE nombreUsuario = ? AND contrasenia = ? "
                + "AND estatus=1 AND (token IS NULL OR token = '');";

        //Una variable temporal para crear nuevas instancias de Empleado:
        Empleado e = null;

        //Con este objeto vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con MySQL:
        Connection conn = connMySQL.open();

        //Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Llenamos los parámetros de la consulta:
        pstmt.setString(1, nombreUsuario);
        pstmt.setString(2, contrasenia);
        
        // Ejecutammos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();

        //Recorremos el ResultSet:
        if (rs.next()) {
            e = fillEmpleado(rs);
            saveToken(e.getUsuario());
        }
        
        //Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        //Devolvemos el empleado que se genero:
        return e;
    }
    
    public void saveToken(Usuario u) throws Exception {
        // Gwneramos la consuta:
        String sql = "UPDATE usuario SET token = '" + u.getToken() + "' WHERE idUsuario = " + u.getId() + ";";
        
        // Generamos el objeto de la conexión:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión:
        Connection conn = connMySQL.open();
        
        // Objeo para ejecutarla consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.execute();
        pstmt.close();
        conn.close();
        connMySQL.close();
    }
    
    public void deleteToken(Usuario u) throws Exception {
        // Gwneramos la consuta:
        String sql = "UPDATE usuario SET token = '' WHERE idUsuario = " + u.getId()+ ";";
        
        // Generamos el objeto de la conexión:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión:
        Connection conn = connMySQL.open();
        
        // Objeto para ejecutar la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.execute();
        pstmt.close();
        conn.close();
        connMySQL.close();
    }

    public boolean validateToken(String token) throws Exception {
        boolean valid = false;
        
        // Generamos la consulta:
        String sql = "SELECT * FROM v_empleados WHERE token = '" + token + "';";
        
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
    
    public boolean validateTokenCliente(String token) throws Exception {
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
    
    /**
     * Crear un objeto de tipo empleado y llena sus propiedades utilizando los
     * datos proporcionados por un ResultSet.
     *
     * @param rs
     * @return
     */
    private Empleado fillEmpleado(ResultSet rs) throws SQLException {

        //Una variable temporal para crear nuevos objetos de tipo Empleado
        Empleado e = new Empleado();

        //Una variable temporal para crear nuevos objetos de tipo Persona
        Persona p = new Persona();

        //Una variable temporal para crear nuevos objetos de tipo usuario:
        Usuario u = new Usuario();

        //Llenamos sus datos:
        p.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p.setApellidoMaterno(rs.getString("apellidoMaterno"));
        p.setDomicilio(rs.getString("domicilio"));
        p.setId(rs.getInt("idPersona"));
        p.setGenero(rs.getString("genero"));
        p.setNombre(rs.getString("nombre"));
        p.setRfc(rs.getString("rfc"));
        p.setTelefono(rs.getString("telefono"));

        //Creamos un nuevo objeto de tipo Usuario
        u.setContrasenia(rs.getString("contrasenia"));
        u.setId(rs.getInt("idUsuario"));
        u.setNombreUsuario(rs.getString("nombreUsuario"));
        u.setRol(rs.getString("rol"));
        u.setToken();

        //Establecemos sus datos personales
        e.setFoto(rs.getString("foto"));
        e.setId(rs.getInt("idEmpleado"));
        e.setNumeroEmpleado(rs.getString("numeroEmpleado"));
        e.setPuesto(rs.getString("puesto"));
        e.setRutaFoto(rs.getString("rutaFoto"));
        e.setEstatus(rs.getInt("estatus"));

        //Establecemos su persona:
        e.setPersona(p);

        //Establecemos su Usuario:
        e.setUsuario(u);

        return e;
    }
}