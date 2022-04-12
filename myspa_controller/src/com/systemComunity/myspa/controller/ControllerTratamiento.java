/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.systemComunity.myspa.controller;

import com.systemComunity.myspa.db.ConexionMySQL;
import com.scd.myspa.model.Tratamiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DanielAbrahamSanchez
 */
public class ControllerTratamiento {
    
    public int insert(Tratamiento t) throws Exception {
        //Definimos la consulta SQL que realiza la inserción del registro:
        String sql = "INSERT INTO tratamiento(nombre, costo, descripcion, estatus) VALUES(?, ?, ?, ?)";
        
        // Aquí guardaremos el ID que se generará:
        int idGenerado = -1;
        
        // Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
       //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();
        
        // Con este objeto ejecutaremos la sentencia SQL que realiza la inserción en la tabla. Debemos especificarle que queremos que nos devuelva el ID
        // que se genera al realizar la inserción del registro.
       PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
        
        // En este objeto guardaremos el resultado de la consulta, la cual nos devolverá los ID's que se generaron. En este caso, solo se generará un ID:      
        ResultSet rs = null;
        
        // Llenamos el valor de campo de la consulta SQL definida antes:
        pstmt.setString(1, t.getNombre());
        pstmt.setDouble(2, t.getCosto());
        pstmt.setString(3, t.getDescripcion());
        pstmt.setInt(4, 1);
        
        // Ejecutamos la consulta SQL:
        pstmt.executeUpdate();
        
        // Le pedimos al PreparedStatementel valor de las claves primarias generadas, que en este caso, es solo un valor:
        rs = pstmt.getGeneratedKeys();
        
        // Intentamos movernos al primer registro:
        if (rs.next()) {
            idGenerado = rs.getInt(1);
            t.setId(idGenerado);
        }
        
        // Cerramos todos los objetos de conexión con la Base de Datos:
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        // Devolvemos el ID generado:
        return idGenerado;
    }
     public void update(Tratamiento t) throws Exception {
        //Definimos la consulta SQL que realiza la inserción del registro:
        String sql = "UPDATE tratamiento SET nombre = ?, costo = ? , descripcion = ? WHERE idTratamiento = ?";
        
        // Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();
        
        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.setString(1, t.getNombre());
        pstmt.setDouble(2, t.getCosto());
        pstmt.setString(3, t.getDescripcion());
        pstmt.setInt(4, t.getId());
        // Ejecutamos la consulta SQL:
        pstmt.executeUpdate();
        
        // Cerramos todos los objetos de conexión con la Base de Datos:
        pstmt.close();
        connMySQL.close();
    }
     public void delete(int idTratamiento) throws Exception {
        //Definimos la consulta SQL que realiza la inserción del registro:
        String sql = "UPDATE tratamiento SET estatus = 0 Where idTratamiento = ?";
        // Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();
        
        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);
              
        // Obtenemos el id del producto para hacer su eliminación lógica cambiando el estatus a 0:
        pstmt.setInt(1, idTratamiento);
        
        // Ejecutamos la consulta SQL:
        pstmt.executeUpdate();
        
        // Cerramos todos los objetos de conexión con la Base de Datos:
        pstmt.close();
        connMySQL.close();
    }
    public List<Tratamiento> getAll(String filtro)throws Exception
    {
        // Definimos la consulta SQL:
        String sql = "SELECT * FROM tratamiento WHERE estatus = 1";
        
        // Aquí guardamos objetos de tipo Tratamiento. Una lista es un
        // contenedor dinámico de objetos.
        List<Tratamiento> tratamiento = new ArrayList<Tratamiento>();
        
        // Una variable temporal para crear nuevas instancias de Tratamiento:
        Tratamiento t = null;
        
        //Con este objeto vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
       
        //Abrimos la conexión con MySQL:
        Connection conn = connMySQL.open();
       
        //Declaramos e inicializamos el objeto con el que ejecutaremos
        // la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        // Ejecutamos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();
        
        // Recorremos el ResultSet:
        while (rs.next())
        {
            t = fill(rs);
            tratamiento.add(t);
        }
        
        // Cerramos lo objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        // Devolvemos la lista de Tratamiento:
        return tratamiento;
        
    }
    
    /**
     * Crea un objeto de tipo Tratamiennto y llena sus propiedades utilizando
     * los datos proporcionados por un ResultSet.
     * @param rs
     * @return 
     */
    private Tratamiento fill(ResultSet rs) throws Exception
    {
        //Creamos una nueva instancia de Tratamiento:
        Tratamiento t = new Tratamiento();
        
        //LLenamos sus propiedades
        t.setId(rs.getInt("idTratamiento"));
        t.setNombre(rs.getString("nombre"));
        t.setCosto(rs.getFloat("costo"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setEstatus(rs.getInt("estatus"));
       

        //Devolvemos el objeto de tipo sucursal:
        return t;
    }
}
