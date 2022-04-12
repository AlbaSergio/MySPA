package com.systemComunity.myspa.controller;

import com.systemComunity.myspa.db.ConexionMySQL;
import com.scd.myspa.model.Empleado;
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

public class ControllerEmpleado {
    //Inserta un registro en la base d datos y devuelve el ID generado

    public int insert(Empleado e) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call insertarEmpleado(?, ?, ?, ?, ?, ?, ?,"
                + "?, ?, ?,"
                + "?, ?, ?,"
                + "?, ?, ?, ?)}"; //Valores de Retorno

        //Aquí guardaremos los ID's que se generán: 
        int idPersonaGenerado = -1;
        int idUsuarioGenerado = -1;
        int idEmpleadoGenerado = -1;

        String numEmpleadoGenerado = "";

        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos el StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden en que
        //los pide el procedimiento almacenado comenzando en 1:
        cstmt.setString(1, e.getPersona().getNombre());
        cstmt.setString(2, e.getPersona().getApellidoPaterno());
        cstmt.setString(3, e.getPersona().getApellidoMaterno());
        cstmt.setString(4, e.getPersona().getGenero());
        cstmt.setString(5, e.getPersona().getDomicilio());
        cstmt.setString(6, e.getPersona().getTelefono());
        cstmt.setString(7, e.getPersona().getRfc());

        //Establecemos los parámetros de los datos de Usuario:
        cstmt.setString(8, e.getUsuario().getNombreUsuario());
        cstmt.setString(9, e.getUsuario().getContrasenia());
        cstmt.setString(10, e.getUsuario().getRol());

        //Establecemos los parámetros de los datos de Empleado:
        cstmt.setString(11, e.getPuesto());
        cstmt.setString(12, e.getFoto());
        cstmt.setString(13, e.getRutaFoto());

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
        idEmpleadoGenerado = cstmt.getInt(16);
        numEmpleadoGenerado = cstmt.getString(17);

        //Los guardamos en el objeto Empleado que nos pasaron como parámetro:
        e.getPersona().setId(idPersonaGenerado);
        e.getUsuario().setId(idUsuarioGenerado);
        e.setId(idEmpleadoGenerado);
        e.setNumeroEmpleado(numEmpleadoGenerado);

        //Cerramos los objetos de Base de datos:
        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Empleado generado:
        return idEmpleadoGenerado;
    }

    public void delete(int id) throws Exception {
        String sql = "UPDATE empleado SET estatus = 0 WHERE idEmpleado = ?";

        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, id);
        pstmt.executeUpdate();

        pstmt.close();
        connMySQL.close();
    }

    public void update(Empleado e) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call actualizarEmpleado(?, ?, ?, ?, ?, ?, ?, "
                +// Datos Persona
                "?, ?, ?,"
                +//Datos Usuario
                "?, ?, ?,"
                +//Datos Empleado
                "?, ?, ?)}"; //Valores de Retorno

        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos el StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden en que
        //los pide el procedimiento almacenado comenzando en 1:
        cstmt.setString(1, e.getPersona().getNombre());
        cstmt.setString(2, e.getPersona().getApellidoPaterno());
        cstmt.setString(3, e.getPersona().getApellidoMaterno());
        cstmt.setString(4, e.getPersona().getGenero());
        cstmt.setString(5, e.getPersona().getDomicilio());
        cstmt.setString(6, e.getPersona().getTelefono());
        cstmt.setString(7, e.getPersona().getRfc());

        //Establecemos los parámetros de los datos de Usuario:
        cstmt.setString(8, e.getUsuario().getNombreUsuario());
        cstmt.setString(9, e.getUsuario().getContrasenia());
        cstmt.setString(10, e.getUsuario().getRol());

        //Establecemos los parámetros de los datos de Empleado:
        cstmt.setString(11, e.getPuesto());
        cstmt.setString(12, e.getFoto());
        cstmt.setString(13, e.getRutaFoto());

        //Registramos los parametros de Salida
        cstmt.setInt(14, e.getPersona().getId());
        cstmt.setInt(15, e.getUsuario().getId());
        cstmt.setInt(16, e.getId());

        //Ejecutamos el StoredProcedure
        cstmt.executeUpdate();
        //Cerramos los objetos de Base de datos:
        cstmt.close();
        connMySQL.close();
    }

    // Buscar un empleado por su ID
    public Empleado findByID(int id) throws Exception {
        return null;
    }

    // Consulta todos los registros de empleado en la BD y devuelve una 
    // lista dinamica de objetos de tipo empleado.
    public List<Empleado> getAll(String filtro) throws Exception {

        //Definimos la consulta SQL:
        String sql = "SELECT * FROM v_empleados WHERE estatus = 1";

        //Aquí guardaremos objetos de tipo Empleado. Una lista es un contenedor
        //dinámico de objetos
        List<Empleado> empleados = new ArrayList<Empleado>();

        //Una variable temporal para crear nuevas instancias de Empleado:
        Empleado e = null;

        //Con este objeto vamos a conectar a la Base de Datos:
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
            e = fill(rs);
            empleados.add(e);
        }
        //Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        //Devolvemos la lista de Sucursales:
        return empleados;

    }

    /**
     * Crear un objeto de tipo sucursal y llena sus propiedades utilizando los
     * datos proporcionados por un ResultSet.
     *
     * @param rs
     * @return
     */
    private Empleado fill(ResultSet rs) throws SQLException {

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
