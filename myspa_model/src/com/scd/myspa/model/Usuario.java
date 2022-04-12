package com.scd.myspa.model;

import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;


public class Usuario {
    int id;
    String nombreUsuario;
    String contrasenia;
    String rol;
    String token;
    
    public Usuario() {}
    
    public Usuario(String nombreUsuario, String contrasenia, String rol, String token) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.token = token;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public String getToken() {
        return token;
    }

    public void setToken() {
        String u = this.nombreUsuario;
        String k = new Date().toString();
        String t = DigestUtils.sha256Hex(u + ";" + k);
        this.token = t;
    }
}