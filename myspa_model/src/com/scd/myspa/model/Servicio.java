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
public class Servicio {

    private int id;
    private String fecha;
    private Empleado empleado;
    private Reservacion reservacion;
    private List<ServicioTratamiento> servicioTratamiento; 
    private double total;

    public Servicio() {}
    
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
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the empleado
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    /**
     * @return the reservacion
     */
    public Reservacion getReservacion() {
        return reservacion;
    }

    /**
     * @param reservacion the reservacion to set
     */
    public void setReservacion(Reservacion reservacion) {
        this.reservacion = reservacion;
    }

    /**
     * @return the servicioTratamiento
     */
    public List<ServicioTratamiento> getServicioTratamiento() {
        return servicioTratamiento;
    }

    /**
     * @param servicioTratamiento the servicioTratamiento to set
     */
    public void setServicioTratamiento(List<ServicioTratamiento> servicioTratamiento) {
        this.servicioTratamiento = servicioTratamiento;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }
}