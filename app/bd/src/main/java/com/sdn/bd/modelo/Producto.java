package com.sdn.bd.modelo;

public class Producto {
    String codigo;
    String nombre;
    String abreviatura;

    public Producto() {
        this.codigo = "";
    }

    public Producto(String codigo, String nombre, String abreviatura) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
    }

    public Producto(int id, String codigo, String nombre, String abreviatura) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
/*
    @Override
    public String toString() {
        return codigo;
    }
*/
   @Override
    public String toString() {
        return "Producto{" +
                ", idproducto='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", abreviatura='" + abreviatura + '\'' +
                '}';
    }
}
