
package com.systemComunity.myspa.controller;

import com.systemComunity.myspa.db.ConexionMySQL;
import com.scd.myspa.model.Sucursal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ControllerSucursal 
{
     /*
    *Inserta un registro de sucursal en la BD y devuelve el ID generado.
    */
    public int insert(Sucursal s)throws Exception
    {
        //Definimos la consulta SQL que realiza la insercion del registro:
        String sql= "INSERT INTO sucursal(nombre, domicilio, latitud, longitud, estatus )"+
                    "VALUES(?, ?, ?, ?, ?)";
        //Aqui guardaremos el ID que se genera:
        int idGenerado = -1;
        
        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la sentencia SQL que realiza la insercion 
        //en la tabla. Debemos especificarle que queremos que nos devuelva el ID
        //que se genera al realizar la insecion del registro.
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
        //En este objeto guardaremos el resultado de la consulta, la cual 
        //nos devolvera  los ID´s que se generaron. En este caso solo se genera un ID: 
        ResultSet rs = null;
        //LLenamos el valor de campo de la consulta SQL definida antes.
        pstmt.setString(1, s.getNombre());
        pstmt.setString(2, s.getDomicilio());
        pstmt.setDouble(3, s.getLatitud());
        pstmt.setDouble(4, s.getLongitud());
        pstmt.setInt(5,1);
        
        //Ejecutamos la consulta:
        pstmt.executeUpdate();
        
        //Le pedimos al PreparedStatement el valor de las claves primarias generadas
        //que en este caso, es solo un valor.
        rs = pstmt.getGeneratedKeys();
        
        //Intentamos movernos al primer registro
        if (rs.next()) 
        {
         idGenerado = rs.getInt(1);
         s.setId(idGenerado);
        }
        
        //Cerramos todos los objetos de la conexion con la base de datos :
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        //Devolvemos el ID que se genero:
        return idGenerado;
    }
    
    // Actualiza un registro de sucursal en el BD.
    public void update(Sucursal s)throws Exception
    {
        //Definimos la consulta SQL que realiza la insercion del registro:
        String sql= "UPDATE sucursal SET nombre=?, domicilio=?, latitud=?, longitud=? "
                  + "WHERE idSucursal=?";
    
        //Declaramos un objeto de tipo conexion
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexion a la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la sentencia SQL que realiza la actualización 
        //en la tabla. Debemos especificarle que queremos que nos devuelva el ID
        //que se genera al realizar la actualización del registro.
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        //LLenamos el valor de campo de la consulta SQL definida antes.
        pstmt.setString(1, s.getNombre());
        pstmt.setString(2, s.getDomicilio());
        pstmt.setDouble(3, s.getLatitud());
        pstmt.setDouble(4, s.getLongitud());
        pstmt.setInt(5,s.getId());
        
        //Ejecutamos la consulta:
        pstmt.executeUpdate();
        
        //Cerramos todos los objetos de la conexion con la base de datos :
        pstmt.close();
        connMySQL.close();
    }
    
   public void delete(int idSucursal) throws Exception {
        String sql = "UPDATE sucursal SET estatus = 0 WHERE idSucursal = ?";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        // Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();
        
        // Declaramos e inicializamos el objeto con el que ejecutaremos la consulta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);
              
        // Obtenemos el id de la Sucursal para hacer su eliminación lógica cambiando el estatus a 0:
        pstmt.setInt(1, idSucursal);
        
        // Ejecutamos la consulta SQL:
        pstmt.executeUpdate();
        
        // Cerramos todos los objetos de conexión con la Base de Datos:
        pstmt.close();
        connMySQL.close();
    }
   
    /**
     * Busca una sucursal por su ID
     */
    public Sucursal finByID(int id)throws Exception {
        return null;
    }
    
    /**
     * Consulta y devuelve los registros de sucursales encontrados en la 
     * Base de Datos, aplicando coincidencias parciales con base en el
     * parámetro <code>filtro</code>.
     * 
     * Los registros encontrados se devuelven en forma de una lista dinámica
     * (List{@link Sucursal}) que contiene dentro los objetos de tipo
     * {@link com.scd.myspa.db}
     * 
     * @param filtro    Es el término de coincidencia parcial que condicionará
     *                  la búsqueda a solo aquellos registros coincidentes
     *                  con el valor especificado.
     * @return          Devuelve el listado de sucursales encontradas
     *                  en la base de datos, en forma de una lista dinámica
     *                  <code>(List{@link Sucursal})</code>.
     */
    public List<Sucursal> getAll(String filtro)throws Exception {
        // Definimos la consulta SQL:
        String sql = "SELECT * FROM sucursal WHERE estatus = 1";
        
        // Aquí guardamos objetos de tipo Sucursal. Una lista es un
        // contenedor dinámico de objetos.
        List<Sucursal> sucursales = new ArrayList<Sucursal>();
        
        // Una variable temporal para crear nuevas instancias de ucursal:
        Sucursal s = null;
        
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
        while (rs.next()) {
            s = fill(rs);
            sucursales.add(s);
        }
        
        // Cerramos lo objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        // Devolvemos la lista de sucursales:
        return sucursales;
    }
    
    /**
     * Crea un objeto de tipo Sucursal y llena sus propiedades utilizando
     * los datos proporcionados por un ResultSet.
     * @param rs
     * @return 
     */
    private Sucursal fill(ResultSet rs) throws Exception  {
        //Creamos una nueva instancia de Sucursal:
        Sucursal s = new Sucursal();
        
        //LLenamos sus propiedades
        s.setDomicilio(rs.getString("domicilio"));
        s.setEstatus(rs.getInt("estatus"));
        s.setId(rs.getInt("idSucursal"));
        s.setLatitud(rs.getDouble("latitud"));
        s.setLongitud(rs.getDouble("longitud"));
        s.setNombre(rs.getString("Nombre"));

        //Devolvemos el objeto de tipo sucursal:
        return s;
    }
}
