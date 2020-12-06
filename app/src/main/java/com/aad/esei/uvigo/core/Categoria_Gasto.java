package com.aad.esei.uvigo.core;

public enum Categoria_Gasto {
    ST(""),
    MT(""),
    IR(""),
    PK(""),
    LV(""),
    PJ(""),
    MUL(""),
    MOD(""),
    SEG("Seguro"),
    ITV("ITV"),
    REP("Repostaje");

    private final String codigo;
    private final String categoria;
    Categoria_Gasto(String categoria){
        this.codigo = this.name();
        this.categoria = categoria;
    }
    public String getCategoria(){
        return this.categoria;
    }
    public  String getCodigo(){
        return this.codigo;
    }
    public boolean equalsCategoria(String str) {
        return this.categoria.equals(str);
    }
}
