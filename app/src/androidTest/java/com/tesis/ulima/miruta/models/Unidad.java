package com.tesis.ulima.miruta.models;

import com.parse.ParseGeoPoint;

/**
 * Created by Christian on 5/14/2016.
 */
public class Unidad {
    User chofer;
    String nombre;
    int capacidad, estado, maxCapacidad;
    ParseGeoPoint posicion;

    public User getChofer() {
        return chofer;
    }

    public void setChofer(User chofer) {
        this.chofer = chofer;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getMaxCapacidad() {
        return maxCapacidad;
    }

    public void setMaxCapacidad(int maxCapacidad) {
        this.maxCapacidad = maxCapacidad;
    }

    public ParseGeoPoint getPosicion() {
        return posicion;
    }

    public void setPosicion(ParseGeoPoint posicion) {
        this.posicion = posicion;
    }
}
