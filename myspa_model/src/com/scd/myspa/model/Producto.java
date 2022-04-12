package com.scd.myspa.model;


public class Producto {
    private int id;
    private String nombre;
    private String marca;
    private int estatus;
    private double precioUso;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public double getPrecioUso() {
        return precioUso;
    }

    public void setPrecioUso(double precioUso) {
        this.precioUso = precioUso;
    }    
}
