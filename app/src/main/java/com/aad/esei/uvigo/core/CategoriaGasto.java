package com.aad.esei.uvigo.core;

public enum CategoriaGasto {
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

    CategoriaGasto(String categoria) {
        this.codigo = this.name();
        this.categoria = categoria;
    }

    public static CategoriaGasto getByCode(String code) {
        for (CategoriaGasto g : values()) {
            if (g.getCodigo().equals(code)) return g;
        }
        return null;
    }

    public static String[] arrayCategorias() {
        CategoriaGasto[] arr = values();
        String[] toret = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            toret[i] = arr[i].getCategoria();
        }
        return toret;
    }

    public static String[] arrayCodigos() {
        CategoriaGasto[] arr = values();
        String[] toret = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            toret[i] = arr[i].getCodigo();
        }
        return toret;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public boolean equalsCategoria(String str) {
        return this.categoria.equals(str);
    }
}
