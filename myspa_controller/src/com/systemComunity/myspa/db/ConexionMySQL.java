
package com.systemComunity.myspa.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {
    //Aqui guardamos y gestinamos la conexion con MySQL:
    Connection conexion;

    /*
    *Realizamos la conexión con la Base de datos y devolvemos 
    *un objeto de tipo Connection.
     */
    public Connection open() throws Exception {
        //Establecemos el Driver de MySQL:
        String driver = "com.mysql.jdbc.Driver";
        //Establecemos la ruta de conexion:
        String url = "jdbc:mysql://localhost:3307/myspa";

        //Establecemos el usuario y la contraseña:
        String usuario = "root";
        String contrasenia = "";

        //Registramos el Driver 
        //Nos sirve para ejecutar clases en tiempo de ejecucion 
        Class.forName(driver);

        //Abrimos la conexión con MySQL;
        conexion = DriverManager.getConnection(url, usuario, contrasenia);

        //Devolvemos el objeto que mantiene la conexion con MySQL:
        return conexion;
    }

    //Cerramos la conexion con MySQL
    public void close() {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
