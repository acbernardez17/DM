package com.aad.esei.uvigo.ui;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.DBManager;
import com.aad.esei.uvigo.core.GastoDAO;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class StatisticsActivity extends Activity implements DatePickerListener {
    private String id_coche;
    private List<DataEntry> data;
    private Pie pie;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.statistical_layout);

        id_coche = (String) StatisticsActivity.this.getIntent().getExtras().get("coche");
        pie = AnyChart.pie();

        setDatePicker();

        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.graphCustom); //Tiene que estar aquí al final
        anyChartView.setChart(pie);
    }

    private void setCategoriesValues(String coche, Date fecha) {
        GastoDAO gastodao = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        /*try {
            ContentValues valores = new ContentValues();
            SimpleDateFormat isoDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.ROOT );
            isoDateFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
            String fechaSQL = isoDateFormat.format( fecha );
            valores.put(DBManager.GASTO_ID, "2");
            valores.put(DBManager.GASTO_CATEGORIA, "ST");
            valores.put(DBManager.GASTO_FECHA, fechaSQL);
            valores.put(DBManager.GASTO_ID_COCHE, "5004FKF");
            valores.put(DBManager.GASTO_PRECIO, "19.23");
            valores.put(DBManager.GASTO_TITULO, "SerVSsada");
            gastodao.insert("2", valores);
        }catch (Exception e) {
            Toast.makeText(this, "Falla en el insertar ", Toast.LENGTH_LONG ).show();
        }*/


        Cursor cursor = gastodao.getAllGastosCoche(coche, fecha);
        Map<String,TextView> map = new LinkedHashMap<>();
        map.put("ITV" , this.findViewById(R.id.lbl_itv));
        map.put("MOD" ,this.findViewById(R.id.lbl_modificaciones));
        map.put("IR" ,this.findViewById(R.id.lbl_impuestos_rodaje));
        map.put("ST" ,this.findViewById(R.id.lbl_servicio_tecnico));
        map.put("LV" ,this.findViewById(R.id.lbl_lavados));
        map.put("SEG" ,this.findViewById(R.id.lbl_seguro));
        map.put("MT" ,this.findViewById(R.id.lbl_mantenimiento));
        map.put("PK" ,this.findViewById(R.id.lbl_parking));
        map.put("PJ" ,this.findViewById(R.id.lbl_peajes));
        map.put("OTRO" ,this.findViewById(R.id.lbl_otros));
        map.put("MUL" ,this.findViewById(R.id.lbl_multas));
        map.put("REP" ,this.findViewById(R.id.lbl_repostaje));
        data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Log.d("a-----------------", "hay datos");
            do {
                String cat = (String) cursor.getString(cursor.getColumnIndex("categoria"));
                Double gasto = (Double) cursor.getDouble(cursor.getColumnIndex("precio"));

                switch (cat) {
                    case "REP" :
                        map.get("REP").setText(gasto.toString());
                        data.add(new ValueDataEntry("Repostaje", gasto));
                        break;
                    case "MUL":
                        map.get("MUL").setText(gasto.toString());
                        data.add(new ValueDataEntry("Multas", gasto));
                        break;
                    case "PJ" :
                        map.get("PJ").setText(gasto.toString());
                        data.add(new ValueDataEntry("Peajes", gasto));
                        break;
                    case "PK":
                        map.get("PK").setText(gasto.toString());
                        data.add(new ValueDataEntry("Parking", gasto));
                        break;
                    case "MT" :
                        map.get("MT").setText(gasto.toString());
                        data.add(new ValueDataEntry("Mantenimiento", gasto));
                        break;
                    case "SEG":
                        map.get("SEG").setText(gasto.toString());
                        data.add(new ValueDataEntry("Seguro", gasto));
                        break;
                    case "LV" :
                        map.get("LV").setText(gasto.toString());
                        data.add(new ValueDataEntry("Lavados", gasto));
                        break;
                    case "ST":
                        map.get("ST").setText(gasto.toString());
                        data.add(new ValueDataEntry("Servicio Técnico", gasto));
                        break;
                    case "MOD" :
                        map.get("MOD").setText(gasto.toString());
                        data.add(new ValueDataEntry("Modificaciones", gasto));
                        break;
                    case "IR":
                        map.get("IR").setText(gasto.toString());
                        data.add(new ValueDataEntry("Impuestos Rodaje", gasto));
                        break;
                    case "ITV" :
                        map.get("ITV").setText(gasto.toString());
                        data.add(new ValueDataEntry("ITV", gasto));
                        break;
                    default:
                        map.get("OTRO").setText(gasto.toString());
                        data.add(new ValueDataEntry("Otros", gasto));
                        break;
                }
            } while (cursor.moveToNext());
        }else {
            data.add(new ValueDataEntry("SIN GASTOS", 1));
        }
        pie.data(data);
        cursor.close();
    }


    private void setDatePicker() {
        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);
        picker
                .setDayOfWeekTextColor(Color.CYAN)
                .setTodayButtonTextColor(Color.rgb(255, 143, 0))
                .setTodayDateTextColor(Color.BLACK)
                .setUnselectedDayTextColor(Color.YELLOW)
                .setMonthAndYearTextColor(Color.RED)
                .setDateSelectedTextColor(Color.BLACK)
                .setDateSelectedColor(Color.rgb(255, 143, 0))
                .setListener(this)
                .showTodayButton(true)
                .setBackgroundColor(Color.rgb(42, 42, 42));
        picker.init();
        picker.setDate(new DateTime());
    }

    //Datepicker Menu
    @Override
    public void onDateSelected(@NonNull final DateTime dateSelected) {
        setDataClean();
        this.setCategoriesValues(this.id_coche, dateSelected.toDate());
    }

    private void setDataClean() {
        Map<String,TextView> map = new LinkedHashMap<>();
        map.put("ITV" , this.findViewById(R.id.lbl_itv));
        map.put("MOD" ,this.findViewById(R.id.lbl_modificaciones));
        map.put("IR" ,this.findViewById(R.id.lbl_impuestos_rodaje));
        map.put("ST" ,this.findViewById(R.id.lbl_servicio_tecnico));
        map.put("LV" ,this.findViewById(R.id.lbl_lavados));
        map.put("SEG" ,this.findViewById(R.id.lbl_seguro));
        map.put("MT" ,this.findViewById(R.id.lbl_mantenimiento));
        map.put("PK" ,this.findViewById(R.id.lbl_parking));
        map.put("PJ" ,this.findViewById(R.id.lbl_peajes));
        map.put("OTRO" ,this.findViewById(R.id.lbl_otros));
        map.put("MUL" ,this.findViewById(R.id.lbl_multas));
        map.put("REP" ,this.findViewById(R.id.lbl_repostaje));

        for(TextView t : map.values()) {
            t.setText("0");
        }
    }
}
