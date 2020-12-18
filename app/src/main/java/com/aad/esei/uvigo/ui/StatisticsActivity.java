package com.aad.esei.uvigo.ui;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.CategoriaGasto;
import com.aad.esei.uvigo.core.DBManager;
import com.aad.esei.uvigo.core.GastoDAO;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class StatisticsActivity extends Activity implements DatePickerListener {

    private DateTime dateSelected;
    private Map<String, TextView> mapTextViewPorCateg;

    private String id_coche;
    private Pie pie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.statistical_layout);

        mapTextViewPorCateg = this.initTextViewMap();
        id_coche = (String) StatisticsActivity.this.getIntent().getExtras().get("coche");
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

        Map<String, Double> mapSumaTotalPorCateg = new LinkedHashMap<>();
        List<DataEntry> data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String cat = (String) cursor.getString(cursor.getColumnIndex("categoria"));
                Double gasto = (Double) cursor.getDouble(cursor.getColumnIndex("precio"));

                if (mapSumaTotalPorCateg.containsKey(cat)) {
                    mapSumaTotalPorCateg.put(cat, mapSumaTotalPorCateg.get(cat) + gasto);
                } else {
                    mapSumaTotalPorCateg.put(cat, gasto);
                }

            } while (cursor.moveToNext());

            for (String cat : mapSumaTotalPorCateg.keySet()) {
                double precio = Double.parseDouble(mapSumaTotalPorCateg.get(cat).toString());
                mapTextViewPorCateg.get(cat).setText(new DecimalFormat("#.00").format(precio));
                data.add(new ValueDataEntry(CategoriaGasto.valueOf(cat).getCategoria(), mapSumaTotalPorCateg.get(cat)));
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
        for (TextView t : map.values()) {
            t.setText("0");
        }
    }

    private Map<String, TextView> initTextViewMap() {
        Map<String, TextView> map = new LinkedHashMap<>();
        map.put(CategoriaGasto.ITV.getCodigo(), this.findViewById(R.id.lbl_itv));
        map.put(CategoriaGasto.MOD.getCodigo(), this.findViewById(R.id.lbl_modificaciones));
        map.put(CategoriaGasto.IR.getCodigo(), this.findViewById(R.id.lbl_impuestos_rodaje));
        map.put(CategoriaGasto.ST.getCodigo(), this.findViewById(R.id.lbl_servicio_tecnico));
        map.put(CategoriaGasto.LV.getCodigo(), this.findViewById(R.id.lbl_lavados));
        map.put(CategoriaGasto.SEG.getCodigo(), this.findViewById(R.id.lbl_seguro));
        map.put(CategoriaGasto.MT.getCodigo(), this.findViewById(R.id.lbl_mantenimiento));
        map.put(CategoriaGasto.PK.getCodigo(), this.findViewById(R.id.lbl_parking));
        map.put(CategoriaGasto.PJ.getCodigo(), this.findViewById(R.id.lbl_peajes));
        map.put(CategoriaGasto.OTRO.getCodigo(), this.findViewById(R.id.lbl_otros));
        map.put(CategoriaGasto.MUL.getCodigo(), this.findViewById(R.id.lbl_multas));
        map.put(CategoriaGasto.REP.getCodigo(), this.findViewById(R.id.lbl_repostaje));

        return map;
    }
}
