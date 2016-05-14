package com.tesis.ulima.miruta.models;

import com.parse.ParseGeoPoint;

import java.util.List;

/**
 * Created by Christian on 5/14/2016.
 */
public class Ruta {
    String nombre;
    ParseGeoPoint camino [];
    List<Unidad> unidades;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ParseGeoPoint[] getCamino() {
        return camino;
    }

    public void setCamino(ParseGeoPoint[] camino) {
        this.camino = camino;
    }

    public List<Unidad> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<Unidad> unidades) {
        this.unidades = unidades;
    }
}
