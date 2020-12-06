package com.aad.esei.uvigo.core;

public class Repostaje extends Gasto{

    private float precio_litro;
    private int litros_antes;
    private int litros_repostados;
    private int km_actuales;


    public Repostaje(Categoria_Gasto categoria, float precio_litro, int litros_antes, int litros_repostados,int km_actuales, String fecha, String titulo) {

        super(categoria, ((float) precio_litro/litros_antes), fecha, titulo);
        this.precio_litro = precio_litro;
        this.litros_antes = litros_antes;
        this.litros_repostados = litros_repostados;
        this.km_actuales = km_actuales;
    }

    public float getPrecio_litro() {
        return precio_litro;
    }

    public int getLitros_antes() {
        return litros_antes;
    }

    public int getLitros_repostados() {
        return litros_repostados;
    }

    public int getKm_actuales() {
        return km_actuales;
    }

    public void setPrecio_litro(float precio_litro) {
        this.precio_litro = precio_litro;
    }

    public void setLitros_antes(int litros_antes) {
        this.litros_antes = litros_antes;
    }

    public void setLitros_repostados(int litros_repostados) {
        this.litros_repostados = litros_repostados;
    }

    public void setKm_actuales(int km_actuales) {
        this.km_actuales = km_actuales;
    }
}
