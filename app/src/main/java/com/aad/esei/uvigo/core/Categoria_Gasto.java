package com.aad.esei.uvigo.core;

public enum Categoria_Gasto {
    REP("Repostaje"),
    ST("Servicio Técnico"),
    MT("Mantenimiento"),
    IR("Impuestos Rodaje"),
    PK("Parking"),
    LV("Lavados"),
    PJ("Peajes"),
    MUL("Multas"),
    MOD("Modificaciones"),
    SEG("Seguro"),
    ITV("ITV"),
    OTRO("Otro");

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

    public static Categoria_Gasto getByCode(String code) {
        for(Categoria_Gasto g : values()) {
            if(g.getCodigo().equals(code)) return g;
        }
        return null;
    }

    public static String[] arrayCategorias(){
        Categoria_Gasto[] arr = values();
        String[] toret = new String[arr.length];
        for (int i = 0; i <arr.length; i++){
            toret[i] = arr[i].getCategoria();
        }
        return toret;
    }

    public static String[] arrayCodigos(){
        Categoria_Gasto[] arr = values();
        String[] toret = new String[arr.length];
        for (int i = 0; i <arr.length; i++){
            toret[i] = arr[i].getCodigo();
        }
        return toret;
    }
}
