package com.sdn.bd.modelo;

public class BorrarLectura {
    int idservidor;
    String barra;
    int idorden;

    public BorrarLectura() {
        this.idservidor = -1;
    }

    public BorrarLectura(int idservidor, String barra, int idorden) {
        this.idservidor = idservidor;
        this.barra = barra;
        this.idorden =idorden;
    }

    public int getIdservidor() {
        return idservidor;
    }

    public void setIdservidor(int idservidor) {
        this.idservidor = idservidor;
    }

    public String getBarra() {
        return barra;
    }

    public void setBarra(String barra) {
        this.barra = barra;
    }

    public int getIdorden() {
        return idorden;
    }

    public void setIdorden(int idorden) {
        this.idorden = idorden;
    }

    @Override
    public String toString() {
        return ""+idservidor;
    }
}
