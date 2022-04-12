
package com.systemComunity.myspa.controller;

import com.systemComunity.myspa.db.ConexionMySQL;
import com.scd.myspa.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ControllerProducto 
{
  /**
     * Inserta un registro de Producto en la Base de Datos y devuelve el ID generado.
     */
    public int insert(Producto p) throws Exception {
        //Definimos la consulta SQL que realiza la inserción del registro:
        String sql = "INSERT INTO producto(nombre, marca, precioUso, estatus) VALUES(?, ?, ?, ?)";
        
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
        pstmt.setString(1, p.getNombre());
        pstmt.setString(2, p.getMarca());
        pstmt.setDouble(3, p.getPrecioUso());
        pstmt.setInt(4, 1);
        
        // Ejecutamos la consulta SQL:
        pstmt.executeUpdate();
        
        // Le pedimos al PreparedStatementel valor de las claves primarias generadas, que en este caso, es solo un valor:
        rs = pstmt.getGeneratedKeys();
        
        // Intentamos movernos al primer registro:
        if (rs.next()) {
            idGenerado = rs.getInt(1);
            p.setId(idGenerado);
        }
        
        // Cerramos todos los objetos de conexión con la Base de Datos:
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        // Devolvemos el ID generado:
        return idGenerado;
    }
    
    /** 
     * Actualiza un registro de Sucursal en la Base de Datos
     */
    public void update(Producto p) throws Exception {
        //Definimos la consulta SQL que realiza la inserción del registro:
        String sql = "UPDATE producto SET nombre = ?, marca = ?, precioUso = ? WHERE idProducto = ?";
        
        // Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();
        
        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        // Llenamos el valor de campo de la consulta SQL definida antes:
        pstmt.setString(1, p.getNombre());
        pstmt.setString(2, p.getMarca());
        pstmt.setDouble(3, p.getPrecioUso());
        pstmt.setInt(4, p.getId());
        // Ejecutamos la consulta SQL:
        pstmt.executeUpdate();
        
        // Cerramos todos los objetos de conexión con la Base de Datos:
        pstmt.close();
        connMySQL.close();
    }
    
    /** 
     * Elimina de manera lógica un registro
     */
    public void delete(int idProducto) throws Exception {
        //Definimos la consulta SQL que realiza la inserción del registro:
        String sql = "UPDATE producto SET estatus = 0 WHERE idProducto = ?";
        
        // Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();
        
        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);
              
        // Obtenemos el id del producto para hacer su eliminación lógica cambiando el estatus a 0:
        pstmt.setInt(1, idProducto);
        
        // Ejecutamos la consulta SQL:
        pstmt.executeUpdate();
        
        // Cerramos todos los objetos de conexión con la Base de Datos:
        pstmt.close();
        connMySQL.close();
    }
    
    /** 
     * Busca un Producto por ID.
     */
    public Producto findByID(int id) throws Exception {
        return null;
    }
    
    /** 
     * Consulta todos los registros de sucursales encontrados en la Base de Datos, aplicando coincidencias parciales con base en el
     * parámetro <code>filtro</code>
     * 
     * Los registros encontrados se devuelven en forma de una lista denámica (List{@link  Producto}) que contiene dentro los objetos
     * de tipo {@link org.scd.myspa.model.Producto}
     * 
     * @param filtro          Es el término de coincidencia parcial que coincidirá la búsqueda a solo aquellos registros coincidentes
     *                        con el valor especificado.
     * 
     * @return                Devuelve el listado de productos encontradas en la Base de Datos , en forma de una lista dinámica.
     */ 
    public List<Producto> getAll(String filtro) throws Exception {       
        // Definimos la consulta SQL:
        String sql = "SELECT * FROM producto WHERE estatus = 1";
        
        // Aquí guardareos objetos de tipo Producto. Una lista es un contenedor dinámico de objetos.
        List<Producto> productos = new ArrayList<Producto>();
        
        // Una variable temporal para crear nuevas instancias de Producto:
        Producto p = null;
        
        // Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();
        
        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        // Ejecutamos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();
        
        // Recorremos el ResultSet:
        while(rs.next()) {
            p = fill(rs);
            productos.add(p);
        }
        
        // Cerramos todos los objetos de conexión con la Base de Datos:
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        // Devolvemos la lista de productos:
        return productos;
    }
    
    /**
     * Crea un objeto de tipo Producto y llena sus propiedades utilizando los datos proporcionados por un ResultSet.
     * @param rs
     * @return 
     */
    public Producto fill(ResultSet rs) throws SQLException {
        // Creamos una nueva instancia de Sucursal:
        Producto p = new Producto();
        
        // Llenamos sus propiedades:
        p.setId(rs.getInt("idProducto"));
        p.setNombre(rs.getString("nombre"));
        p.setMarca(rs.getString("marca"));
        p.setPrecioUso(rs.getDouble("precioUso"));
        p.setEstatus(rs.getInt("estatus"));
        
        // Devolvemos el objeto de tipo Producto:
        return p;
    }
}
