package com.aad.esei.uvigo.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.Categoria_Gasto;
import com.aad.esei.uvigo.core.DBManager;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        final TextView LBL_TITULO_GASTO = (TextView) view.findViewById(R.id.lbl_titulo_gasto);
        final TextView LBL_CANT_GASTO = (TextView) view.findViewById(R.id.lbl_cantidad_gasto);
        final ImageView IMG_GASTO = (ImageView) view.findViewById(R.id.img_icono_gasto);

        // Extract properties from cursor
        Categoria_Gasto categoria = Categoria_Gasto.getByCode(cursor.getString(cursor.getColumnIndexOrThrow(DBManager.GASTO_CATEGORIA)));
        double precio_total = cursor.getDouble(cursor.getColumnIndexOrThrow(DBManager.GASTO_PRECIO));
        String fecha = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.GASTO_FECHA));
        String titulo = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.GASTO_TITULO));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        Date date;
        try {
            date = formatter.parse(fecha);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            Calendar weekAgo = Calendar.getInstance();
            weekAgo.add(Calendar.DATE,-7);
            if (cal.after(weekAgo)){ //Hace menos de una semana
                formatter = new SimpleDateFormat("E, d MMMM");
                formatter.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
            }else if (cal.get(Calendar.YEAR)==year ){ //En el año actual
                formatter = new SimpleDateFormat("MMMM, d");
            }else{//Años pasados
                formatter = new SimpleDateFormat("dd-MM-yyyy");
            }
            fecha = formatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Populate fields with extracted properties
        LBL_TIPO_GASTO.setText(categoria.getCategoria());
        LBL_FECHA_GASTO.setText(fecha);
        LBL_TITULO_GASTO.setText(titulo);
        LBL_CANT_GASTO.setText(new DecimalFormat("#.##").format(precio_total)+"€");

        if (categoria.equals(Categoria_Gasto.REP)){
            IMG_GASTO.setImageResource(R.drawable.ic_gasolinera);
        }else{
            IMG_GASTO.setImageResource(R.drawable.ic_itv);
        }

        switch (categoria.getCodigo()) {
            case "REP" :
                IMG_GASTO.setImageResource(R.drawable.ic_gasolinera);
                break;
            case "MUL":
                IMG_GASTO.setImageResource(R.drawable.ic_multas);
                break;
            case "PJ" :
                IMG_GASTO.setImageResource(R.drawable.ic_peaje);
                break;
            case "PK":
                IMG_GASTO.setImageResource(R.drawable.ic_parking);
                break;
            case "MT" :
                IMG_GASTO.setImageResource(R.drawable.ic_mantenimiento);
                break;
            case "SEG":
                IMG_GASTO.setImageResource(R.drawable.ic_seguro_coche);
                break;
            case "LV" :
                IMG_GASTO.setImageResource(R.drawable.ic_lavados);
                break;
            case "ST":
                IMG_GASTO.setImageResource(R.drawable.ic_servicio_tecnico);
                break;
            case "MOD" :
                IMG_GASTO.setImageResource(R.drawable.ic_coche_deportivo);
                break;
            case "IR":
                IMG_GASTO.setImageResource(R.drawable.ic_rueda);
                break;
            case "ITV" :
                IMG_GASTO.setImageResource(R.drawable.ic_itv);
                break;
            default:
                IMG_GASTO.setImageResource(R.drawable.ic_tres_puntos);
                break;
        }





    }
}
