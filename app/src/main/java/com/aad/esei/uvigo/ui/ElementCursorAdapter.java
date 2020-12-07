package com.aad.esei.uvigo.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.Categoria_Gasto;

public class ElementCursorAdapter extends CursorAdapter {

    public ElementCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_elem, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final TextView LBL_TIPO_GASTO = (TextView) view.findViewById(R.id.lbl_tipo_gasto);
        final TextView LBL_FECHA_GASTO = (TextView) view.findViewById(R.id.lbl_fecha_gasto);
        final TextView LBL_KM_GASTO = (TextView) view.findViewById(R.id.lbl_km_gasto);
        final TextView LBL_CANT_GASTO = (TextView) view.findViewById(R.id.lbl_cantidad_gasto);
        final ImageView IMG_GASTO = (ImageView) view.findViewById(R.id.img_icono_gasto);

        // Extract properties from cursor
        Categoria_Gasto categoria = Categoria_Gasto.getByCode(cursor.getString(cursor.getColumnIndexOrThrow("categoria")));
        double precio_total = cursor.getDouble(cursor.getColumnIndexOrThrow("precio_total"));
        String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
        String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));

        // Populate fields with extracted properties
        LBL_TIPO_GASTO.setText(categoria.getCategoria());
        LBL_FECHA_GASTO.setText(fecha);
        //LBL_KM_GASTO.setText(KmGastos[position]);
        LBL_CANT_GASTO.setText(Double.toString(precio_total));
        if (categoria.equals(Categoria_Gasto.REP)){
            IMG_GASTO.setImageResource(R.drawable.gasol_svg);
        }else{
            IMG_GASTO.setImageResource(R.drawable.taller_svg);
        }




    }
}
