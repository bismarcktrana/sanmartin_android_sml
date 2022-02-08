package com.sdn.bd.modelo;

public class Destino {
    Integer id;
    String nombre;

    public Destino() {
        this.id=-1;
        this.nombre="";
    }

    public Destino(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Destino{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
