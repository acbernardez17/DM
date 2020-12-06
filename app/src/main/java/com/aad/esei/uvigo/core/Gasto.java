package com.aad.esei.uvigo.core;

public class Gasto {
    private Categoria_Gasto categoria;
    private float precio;
    private String fecha; //Formato fecha: dd-MM-yyyy HH:mm:ss
    private String titulo;

    public Gasto(Categoria_Gasto categoria, float precio, String fecha, String titulo) {
        this.categoria = categoria;
        this.precio = precio;
        this.fecha = fecha;
        this.titulo = titulo;
    }


    public Categoria_Gasto getCategoria() {
        return categoria;
    }

    public float getPrecio() {
        return precio;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setCategoria(Categoria_Gasto categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
