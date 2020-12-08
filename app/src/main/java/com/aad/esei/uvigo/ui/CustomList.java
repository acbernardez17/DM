package com.aad.esei.uvigo.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aad.esei.uvigo.R;

public class CustomList extends ArrayAdapter{
    private String[] tipoGastos;
    private String[] FechaGastos;
    private String[] KmGastos;
    private String[] CantidadGastos;
    private Integer[] idIcono;
    public CustomList(Activity context, String[] tipoGastos, String[] FechaGastos, String[] KmGastos, String[] CantidadGastos, Integer[] idIcono) {
        super(context, R.layout.listview_elem, tipoGastos);
        this.tipoGastos = tipoGastos;
        this.FechaGastos = FechaGastos;
        this.KmGastos = KmGastos;
        this.CantidadGastos = CantidadGastos;
        this.idIcono = idIcono;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View elemento=convertView;
        final Activity ACTIVITY = (Activity) this.getContext();
        LayoutInflater inflater = ACTIVITY.getLayoutInflater();
        if(convertView==null)
            elemento = inflater.inflate(R.layout.listview_elem, null, true);
        final TextView LBL_TIPO_GASTO = (TextView) elemento.findViewById(R.id.lbl_tipo_gasto);
        final TextView LBL_FECHA_GASTO = (TextView) elemento.findViewById(R.id.lbl_fecha_gasto);
        //final TextView LBL_KM_GASTO = (TextView) elemento.findViewById(R.id.lbl_km_gasto);
        final TextView LBL_CANT_GASTO = (TextView) elemento.findViewById(R.id.lbl_cantidad_gasto);
        final ImageView IMG_GASTO = (ImageView) elemento.findViewById(R.id.img_icono_gasto);
        if (position==0) {
            LBL_TIPO_GASTO.setText("");
            LBL_FECHA_GASTO.setText("");
            //LBL_KM_GASTO.setText("");
            LBL_CANT_GASTO.setText("");
            IMG_GASTO.setImageResource(R.drawable.inicio_svg);
            return elemento;
        }else if(position==(tipoGastos.length-1)){
            LBL_TIPO_GASTO.setText("");
            LBL_FECHA_GASTO.setText("");
            //LBL_KM_GASTO.setText("");
            LBL_CANT_GASTO.setText("");
            IMG_GASTO.setImageResource(R.drawable.fin_svg);
            return elemento;
        }else {
            LBL_TIPO_GASTO.setText(tipoGastos[position]);
            LBL_FECHA_GASTO.setText(FechaGastos[position]);
            //LBL_KM_GASTO.setText(KmGastos[position]);
            LBL_CANT_GASTO.setText(CantidadGastos[position]);
            IMG_GASTO.setImageResource(idIcono[position]);
            return elemento;
        }
    }
}