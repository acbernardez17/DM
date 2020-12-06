package com.aad.esei.uvigo.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Coche {
    public enum TIPO_COMB {GASOLINA,DIESEL}


    private String matricula;
    private String nombre;
    private String marca;
    private String modelo;
    private TIPO_COMB combustible;
    private int combustible_max;
    private int km_iniciales;
    private int l_iniciales;
    private List<Gasto> gastos;

    public Coche(){
        this.matricula = null;
        this.nombre = null;
        this.marca = null;
        this.modelo = null;
        this.combustible = null;
        this.combustible_max = 0;
        this.km_iniciales = 0;
        this.l_iniciales = 0;
        this.gastos = new ArrayList<>();
    }

    public Coche(String matricula, String nombre, String marca, String modelo, TIPO_COMB combustible, int combustible_max, int km_iniciales, int l_iniciales) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.combustible = combustible;
        this.combustible_max = combustible_max;
        this.km_iniciales = km_iniciales;
        this.l_iniciales = l_iniciales;
        this.gastos = new ArrayList<>();
    }

    //Gastos en lo que va de anho
    public float gastosAnho() throws ParseException {
        float toret = 0;
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date fecha;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Calendar cal = Calendar.getInstance();
        for (Gasto g:gastos) {
            fecha = formatter.parse(g.getFecha());
            cal.setTime(fecha);
            if (cal.get(Calendar.YEAR)==year ){
                toret+=g.getPrecio();
            }
        }
        return toret;
    }

    //Gastos en el ultimo año
    public float gastosUltimoAnho() throws ParseException {
        float toret = 0;
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date fecha;
        Calendar yearAgo = Calendar.getInstance();
        yearAgo.add(Calendar.YEAR,-1);
        Calendar cal = Calendar.getInstance();
        for (Gasto g:gastos) {
            fecha = formatter.parse(g.getFecha());
            cal.setTime(fecha);
            if (cal.after(yearAgo)){
                toret+=g.getPrecio();
            }
        }
        return toret;
    }

    public float gastosMantenimiento(){
        float toret = 0;
        for (Gasto g:gastos) {
            if (g.getCategoria().equalsCategoria("XXXXXXXXX")){ //Introducir categorías
                toret+=g.getPrecio();
            }
        }
        return toret;
    }

    //Gastos en lo que va de mes de combustible
    public float gastoCombusMes() throws ParseException {
        float toret = 0;
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date fecha;
        int month = Calendar.getInstance().get(Calendar.MONTH);
        Calendar cal = Calendar.getInstance();
        for (Gasto g:gastos) {
            if (g.getCategoria().equalsCategoria("Repostaje")){
                fecha = formatter.parse(g.getFecha());
                cal.setTime(fecha);
                if (cal.get(Calendar.MONTH)==month){
                    toret+=g.getPrecio();
                }
            }
        }
        return toret;
    }

    //Gastos en los ultimos 30 dias de combustible
    public float gastoCombus30Dias() throws ParseException {
        float toret = 0;
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date fecha;
        Calendar monthAgo = Calendar.getInstance();
        monthAgo.add(Calendar.DATE,-30);
        Calendar cal = Calendar.getInstance();
        for (Gasto g:gastos) {
            if (g.getCategoria().equalsCategoria("Repostaje")){
                fecha = formatter.parse(g.getFecha());
                cal.setTime(fecha);
                if (cal.after(monthAgo)){
                    toret+=g.getPrecio();
                }
            }
        }
        return toret;
    }

    //Media de gasto de combustible por KMs recorridos
    public float gastoCombusKm(){
        float toret = 0;
        int km_actuales = 0;
        Repostaje rep;
        for (Gasto g:gastos) {
            if (g.getCategoria().equalsCategoria("Repostaje")){

                toret+=g.getPrecio();
                km_actuales= ((Repostaje) g).getKm_actuales();
            }
        }
        if (km_actuales!=0){
            toret = toret/(km_actuales-this.getKm_iniciales());
        }
        return toret;
    }

    public void añadirGasto(Gasto gasto){
        gastos.add(gasto);
    }

    public void modificarGasto(int pos, Gasto gasto){
        gastos.set(pos,gasto);
    }

    public void eliminarGasto(int pos){
        gastos.remove(pos);
    }

    public List<Gasto> getGastos(){
        return gastos;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public TIPO_COMB getCombustible() {
        return combustible;
    }

    public int getCombustible_max() {
        return combustible_max;
    }

    public int getKm_iniciales() {
        return km_iniciales;
    }

    public int getL_iniciales() {
        return l_iniciales;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setCombustible(TIPO_COMB combustible) {
        this.combustible = combustible;
    }

    public void setCombustible_max(int combustible_max) {
        this.combustible_max = combustible_max;
    }

    public void setKm_iniciales(int km_iniciales) {
        this.km_iniciales = km_iniciales;
    }

    public void setL_iniciales(int l_iniciales) {
        this.l_iniciales = l_iniciales;
    }


}
