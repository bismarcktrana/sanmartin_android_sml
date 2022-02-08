package com.sdn.bd.modelo;

import java.util.Date;

public class Lectura2 {
    int id;
    Producto producto = new Producto();
    String barra;
    Date fecha_empaque;
    Date fecha_proceso;
    Double peso_libra = 0.00;
    Double peso_kilogramo = 0.00;
    String lote;
    String serie;
    Integer cant_pieza=0;
    Integer um =0;  //0=libras ! 1=kilogramo
    Integer _idoperador;
    Integer enviado;

    public Lectura2() {
        this.id = -1;
    }

    public Lectura2(int id, Producto producto, String barra, Date fecha_empaque, Date fecha_proceso, Double peso_libra, Double peso_kilogramo, String lote, String serie, Integer cant_pieza,Integer um, Integer _idoperador, Integer enviado) {
        this.id = id;
        this.producto = producto;
        this.barra = barra;
        this.fecha_empaque = fecha_empaque;
        this.fecha_proceso = fecha_proceso;
        this.peso_libra = peso_libra;
        this.peso_kilogramo = peso_kilogramo;
        this.lote = lote;
        this.serie = serie;
        this.cant_pieza = cant_pieza;
        this.um = um;
        this._idoperador = _idoperador;
        this.enviado = enviado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getBarra() {
        return barra;
    }

    public void setBarra(String barra) {
        this.barra = barra;
    }

    public Date getFecha_empaque() {
        return fecha_empaque;
    }

    public void setFecha_empaque(Date fecha_empaque) {
        this.fecha_empaque = fecha_empaque;
    }

    public Date getFecha_proceso() {
        return fecha_proceso;
    }

    public void setFecha_proceso(Date fecha_proceso) {
        this.fecha_proceso = fecha_proceso;
    }

    public Double getPeso_libra() {
        return peso_libra;
    }

    public void setPeso_libra(Double peso_libra) {
        this.peso_libra = peso_libra;
    }

    public Double getPeso_kilogramo() {
        return peso_kilogramo;
    }

    public void setPeso_kilogramo(Double peso_kilogramo) {
        this.peso_kilogramo = peso_kilogramo;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getCant_pieza() {
        return cant_pieza;
    }

    public void setCant_pieza(Integer cant_pieza) {
        this.cant_pieza = cant_pieza;
    }

    public Integer getUm() {
        return um;
    }

    public void setUm(Integer um) {
        this.um = um;
    }

    public Integer get_idoperador() {
        return _idoperador;
    }

    public void set_idoperador(Integer _idoperador) {
        this._idoperador = _idoperador;
    }

    public Integer getEnviado() {
        return enviado;
    }

    public void setEnviado(Integer enviado) {
        this.enviado = enviado;
    }

    @Override
    public String toString() {
        return barra;
    }

    public String toString2() {
        return "Lectura2{" +
                "id=" + id +
                ", producto=" + producto +
                ", barra='" + barra + '\'' +
                ", fecha_empaque=" + fecha_empaque +
                ", fecha_proceso=" + fecha_proceso +
                ", peso_libra=" + peso_libra +
                ", peso_kilogramo=" + peso_kilogramo +
                ", lote='" + lote + '\'' +
                ", serie='" + serie + '\'' +
                ", cant_pieza=" + cant_pieza +
                ", um=" + um +
                ", _idoperador=" + _idoperador +
                ", enviado=" + enviado +
                '}';
    }

}
