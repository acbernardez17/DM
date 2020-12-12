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
import com.aad.esei.uvigo.core.Categoria_Gasto;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class StatisticsActivity extends Activity implements DatePickerListener {

    private DateTime dateSelected;
    private Map<String,TextView> mapTextViewPorCateg;

    private String id_coche;
    private Pie pie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.statistical_layout);

        mapTextViewPorCateg = this.initTextViewMap();
        id_coche = (String) StatisticsActivity.this.getIntent().getExtras().get(getString(R.string.car));
        pie = AnyChart.pie();

        setDatePicker();

        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.graphCustom); //Tiene que estar aquí al final
        anyChartView.setChart(pie);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this.onDateSelected(this.dateSelected);
    }

    //Datepicker Menu
    @Override
    public void onDateSelected(@NonNull final DateTime dateSelected) {
        this.setDataClean(this.mapTextViewPorCateg);
        this.setCategoriesValues(this.id_coche, dateSelected.toDate(), dateSelected.plusDays(1).toDate());
        this.dateSelected = dateSelected;
    }

    private void setCategoriesValues(String coche, Date fechaInicio, Date fechaFin) {
        GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        Cursor cursor = gastoDAO.getAllGastosCoche(coche, fechaInicio, fechaFin);

        Map<String,Double> mapSumaTotalPorCateg = new LinkedHashMap<>();
        List<DataEntry> data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String cat = (String) cursor.getString(cursor.getColumnIndex(getString(R.string.cat)));
                Double gasto = (Double) cursor.getDouble(cursor.getColumnIndex(getString(R.string.precio_solo)));

                if (mapSumaTotalPorCateg.containsKey(cat)) {
                    mapSumaTotalPorCateg.put(cat, mapSumaTotalPorCateg.get(cat) + gasto);
                } else {
                    mapSumaTotalPorCateg.put(cat, gasto);
                }

            } while (cursor.moveToNext());

            for (String cat : mapSumaTotalPorCateg.keySet()) {
                mapTextViewPorCateg.get(cat).setText(mapSumaTotalPorCateg.get(cat).toString());
                data.add(new ValueDataEntry(Categoria_Gasto.valueOf(cat).getCategoria(), mapSumaTotalPorCateg.get(cat)));
            }

        } else {
            data.add(new ValueDataEntry(getString(R.string.singastos), 0));
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

    private void setDataClean(Map<String, TextView> map) {
        for(TextView t : map.values()) {
            t.setText("0");
        }
    }

    private Map<String, TextView> initTextViewMap() {
        Map<String, TextView> map = new LinkedHashMap<>();
        map.put(Categoria_Gasto.ITV.getCodigo(), this.findViewById(R.id.lbl_itv));
        map.put(Categoria_Gasto.MOD.getCodigo(), this.findViewById(R.id.lbl_modificaciones));
        map.put(Categoria_Gasto.IR.getCodigo(), this.findViewById(R.id.lbl_impuestos_rodaje));
        map.put(Categoria_Gasto.ST.getCodigo(), this.findViewById(R.id.lbl_servicio_tecnico));
        map.put(Categoria_Gasto.LV.getCodigo(), this.findViewById(R.id.lbl_lavados));
        map.put(Categoria_Gasto.SEG.getCodigo(), this.findViewById(R.id.lbl_seguro));
        map.put(Categoria_Gasto.MT.getCodigo(), this.findViewById(R.id.lbl_mantenimiento));
        map.put(Categoria_Gasto.PK.getCodigo(), this.findViewById(R.id.lbl_parking));
        map.put(Categoria_Gasto.PJ.getCodigo(), this.findViewById(R.id.lbl_peajes));
        map.put(Categoria_Gasto.OTRO.getCodigo(), this.findViewById(R.id.lbl_otros));
        map.put(Categoria_Gasto.MUL.getCodigo(), this.findViewById(R.id.lbl_multas));
        map.put(Categoria_Gasto.REP.getCodigo(), this.findViewById(R.id.lbl_repostaje));

        return map;
    }
}
