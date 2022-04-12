/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scd.myspa.model;

import java.util.List;

/**
 *
 * @author eveli
 */
public class ServicioTratamiento {

    private int id;
    private Tratamiento tratamiento;
    private List<Producto> productos;
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the tratamiento
     */
    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    /**
     * @param tratamiento the tratamiento to set
     */
    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    /**
     * @return the producto
     */
    public List<Producto> getProducto() {
        return productos;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(List<Producto> producto) {
        this.productos = producto;
    }   
}