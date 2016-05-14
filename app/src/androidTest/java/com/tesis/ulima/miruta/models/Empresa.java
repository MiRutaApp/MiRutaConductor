package com.tesis.ulima.miruta.models;

import java.net.URL;
import java.util.List;

/**
 * Created by Christian on 5/14/2016.
 */
public class Empresa {
    String nombre;
    URL foto;
    List<User> usuarios;
    List<Ruta> rutas;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public URL getFoto() {
        return foto;
    }

    public void setFoto(URL foto) {
        this.foto = foto;
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Ruta> getRutas() {
        return rutas;
    }

    public void setRutas(List<Ruta> rutas) {
        this.rutas = rutas;
    }
}
