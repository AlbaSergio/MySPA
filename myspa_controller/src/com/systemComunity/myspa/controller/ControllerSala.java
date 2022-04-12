/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.systemComunity.myspa.controller;

import com.systemComunity.myspa.db.ConexionMySQL;
import com.scd.myspa.model.Sala;
import com.scd.myspa.model.Sucursal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ControllerSala {
    //Inserta un registro de sucursal en la BD y devulve el ID generado
//Inserta un registro de sucursal en la BD y devulve el ID generado

    public int insert(Sala s) throws Exception {
        //Definimos la consulta SQL que realiza la insercion del registro:
        String sql = "INSERT INTO sala(nombre, descripcion, foto, rutaFoto, estatus, idSucursal)"
                + " VALUES(?, ?, ?, ?, ?, ?)";
        //Aqui guardaremos el id generado
        int idGenerado = -1;

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion de la base de datos:
        Connection conn = connMySQL.open();

        //  Con este objetoejecuitaremos la sentancia SQL que realiza 
        //  la insercion en la tabla. Debemos especificarle que queremos 
        // que nos devulva el ID que se generara al realizar la 
        // inserccion del registro.
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

//        En este objeto guardaremos el resultado de la consulta,
//        la cual nos devolvera los ID`s que se generaron. En este
//        caso, solo generara un ID
        ResultSet rs = null;

        //Llenamos el valor de campo de la consulta SQL definida antes:
        pstmt.setString(1, s.getNombre());
        pstmt.setString(2, s.getDescripcion());
        pstmt.setString(3, s.getFoto());
        pstmt.setString(4, s.getRutaFoto());
        pstmt.setInt(5, 1);
        pstmt.setInt(6, s.getIdSucursal());

        //Ejecutamos la consulta:
        pstmt.executeUpdate();

//        Le pedimos al PreparedStatement el valor de las claves 
//        primarias generadas, que en este caso, es solo un valor.
        rs = pstmt.getGeneratedKeys();

        //Intentamos movernos al primer registro:
        if (rs.next()) {
            idGenerado = rs.getInt(1);
            s.setId(idGenerado);
        }
        //Cerramos todos los objetos de conexion con la Base de Datos
        rs.close();
        pstmt.close();
        connMySQL.close();

        //Devolvemos el ID que se genero:
        return idGenerado;

    }

    //Actualiza un registro de sucursal en la BD
    public void update(Sala s) throws Exception {
        //Definimos la consulta SQL que realiza la actualizacion del registro:
        String sql = "UPDATE sala SET nombre = ?, descripcion = ?, foto = ?, rutaFoto = ?,idSucursal= ? WHERE idSala = ?";
        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();
        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();
        //Invocamos la centencia
        PreparedStatement pstmt = conn.prepareStatement(sql);
        //Establecemos los parámetros de los datos personales en el orden en que
        //los pide
        pstmt.setString(1, s.getNombre());
        pstmt.setString(2, s.getDescripcion());
        pstmt.setString(3, s.getFoto());
        pstmt.setString(4, s.getRutaFoto());
        pstmt.setInt(5, s.getIdSucursal());
        pstmt.setInt(6, s.getId());

        //Ejecutaremos
        pstmt.executeUpdate();
        //Cerramos todos los objetos de conexion con la Base de Datos
        pstmt.close();
        connMySQL.close();

    }

    //Elimina de forma logica un registro de sucursal basado en su ID
    public void delete(int idSala) throws Exception {
        //Definimos la consulta SQL que realiza la eliminacion del registro:
        String sql = "UPDATE sala SET estatus = 0 Where idSala = ?";
        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();
        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();

        //Invocamos la sentencia
        PreparedStatement pstmt = conn.prepareStatement(sql);
        //Ejecutaremos
        pstmt.setInt(1, idSala);
        pstmt.executeUpdate();
        //Cerramos todos los objetos de conexion con la Base de Datos
        pstmt.close();
        connMySQL.close();
    }

    //Busca una sucursal por su ID
    public Sala findByID(int id) throws Exception {
        return null;
    }

    /**
     * Consulta y devulve los registros de sucursales encontrados en la Base de
     * Datos, aplicando coincidencias parciales con base en el
     * parametro<code>filtro</code>
     *
     * Los registros encontrados se devulven en forma de una lista dinamica
     * (List{@link Sucursal}) que contiene dentro los objetos de tipo
     * {@link com.CuatroFantasticosMenosUno.myspa.model.Sucursal}
     *
     *
     * @param filtro Es el termino de coincidencia parcial que condicionara la
     * busqueda a solo aquellos registros coincidentes con el valor
     * especificado.
     *
     * @return Devulve el listado de sucursales encontradas en la base de datos,
     * en forma de una lista dinamica <code>List{@link Sucursal}</code>
     * @throws Exception
     */
    public List<Sala> getAll(String filtro) throws Exception {
        //Definimos la consulta SQL:
        String sql = "SELECT * FROM sala WHERE estatus=1";

        //Aqui guardaremos objetos de tipo Sucursal. Una lista es un contenedor
        //dinamico de objetos.
        List<Sala> salas = new ArrayList<Sala>();//<Operador diamante>

        //Una variable temporal para crear nuevas instancias de Sucursal:
        Sala s = null;

        //Con este objeto vamos a conectar la base de datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion con MySQL:
        Connection conn = connMySQL.open();

        //Declaramos e inicializamos el objeto con el que ejecutaremos
        //la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Ejecutaremos la consulta y guardaremos su resultado:
        ResultSet rs = pstmt.executeQuery();

        //Recorremos el ResultSet:
        while (rs.next()) {
            s = fill(rs);
            salas.add(s);
        }
        //Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        //Devolvemos la lista de sucursales:
        return salas;
    }

    public List<Sala> getAllBySucursal(String filtro, int idSucursal) throws Exception {
        //Definimos la consulta SQL:
        String sql = "SELECT * FROM sala WHERE estatus=1 AND idSucursal=?";

        //Aqui guardaremos objetos de tipo Sucursal. Una lista es un contenedor
        //dinamico de objetos.
        List<Sala> salas = new ArrayList<Sala>();//<Operador diamante>

        //Una variable temporal para crear nuevas instancias de Sucursal:
        Sala s = null;

        //Con este objeto vamos a conectar la base de datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexion con MySQL:
        Connection conn = connMySQL.open();

        //Declaramos e inicializamos el objeto con el que ejecutaremos
        //la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Ejecutaremos la consulta y guardaremos su resultado:
        ResultSet rs = null;
        pstmt.setInt(1, idSucursal);
        rs = pstmt.executeQuery();

        //Recorremos el ResultSet:
        while (rs.next()) {
            s = fill(rs);
            salas.add(s);
        }
        //Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();

        //Devolvemos la lista de sucursales:
        return salas;
    }
    /**
     * Crea un objeto de tipo Sucursal y llena sus propiedades utilizando los
     * datos proporcionados por un ResultSet
     *
     * @param rs
     * @return
     * @throws Exception
     */
    private Sala fill(ResultSet rs) throws Exception {
        //Creamos una nueva instancia de Sucursal:
        Sala s = new Sala();
        //Llenamos sus propiedades:
        s.setId(rs.getInt("idSala"));
        s.setNombre(rs.getString("nombre"));
        s.setDescripcion(rs.getString("descripcion"));
        s.setFoto(rs.getString("foto"));
        s.setRutaFoto(rs.getString("rutaFoto"));
        s.setEstatus(rs.getInt("estatus"));
        s.setIdSucursal(rs.getInt("idSucursal"));
        //Devolvemos el objeto de tipo Sucursal:
        return s;
    }
}
