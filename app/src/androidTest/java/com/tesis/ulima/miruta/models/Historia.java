package com.tesis.ulima.miruta.models;

import com.parse.ParseGeoPoint;

/**
 * Created by Christian on 5/14/2016.
 */
public class Historia {
    Ruta ruta;
    Unidad unidad;
    ParseGeoPoint posicion;

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public ParseGeoPoint getPosicion() {
        return posicion;
    }

    public void setPosicion(ParseGeoPoint posicion) {
        this.posicion = posicion;
    }
}
