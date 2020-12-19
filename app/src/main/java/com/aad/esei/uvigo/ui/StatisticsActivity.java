package com.aad.esei.uvigo.ui;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.CategoriaGasto;
import com.aad.esei.uvigo.core.DBManager;
import com.aad.esei.uvigo.core.GastoDAO;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.mikephil.charting.animation.Easing.EaseInCirc;


public class StatisticsActivity extends Activity implements DatePickerListener {

    private DateTime dateSelected;
    private Map<String, TextView> mapTextViewPorCateg;

    private String id_coche;
    private PieChart pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.statistical_layout);

        mapTextViewPorCateg = this.initTextViewMap();
        id_coche = (String) StatisticsActivity.this.getIntent().getExtras().get("coche");
        pieChart = this.findViewById(R.id.graphCustom);

        setDatePicker();
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
        pieChart.invalidate();
        this.setCategoriesValues(this.id_coche, dateSelected.toDate(), dateSelected.plusDays(1).toDate());
        this.dateSelected = dateSelected;
    }

    private void setCategoriesValues(String coche, Date fechaInicio, Date fechaFin) {
        GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        Cursor cursor = gastoDAO.getAllGastosCoche(coche, fechaInicio, fechaFin);

        Map<String, Float> mapSumaTotalPorCateg = new LinkedHashMap<>();
        List<PieEntry> data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String cat = (String) cursor.getString(cursor.getColumnIndex("categoria"));
                Float gasto = (Float) cursor.getFloat(cursor.getColumnIndex("precio"));

                if (mapSumaTotalPorCateg.containsKey(cat)) {
                    mapSumaTotalPorCateg.put(cat, mapSumaTotalPorCateg.get(cat) + gasto);
                } else {
                    mapSumaTotalPorCateg.put(cat, gasto);
                }

            } while (cursor.moveToNext());

            for (String cat : mapSumaTotalPorCateg.keySet()) {
                double precio = Double.parseDouble(mapSumaTotalPorCateg.get(cat).toString());
                mapTextViewPorCateg.get(cat).setText(new DecimalFormat("0.00").format(precio));
                data.add(new PieEntry( mapSumaTotalPorCateg.get(cat), CategoriaGasto.valueOf(cat).getCategoria()));
            }

        } else {
            data.add(new PieEntry(0f,getString(R.string.singastos)));
        }

        setPieChart(data);
        cursor.close();
    }

    private void setPieChart(List<PieEntry> data) {
        pieChart.invalidate();

        pieChart.animateX(3000, EaseInCirc);
        pieChart.setEntryLabelColor(Color.rgb(0,0,0));
        PieDataSet set = new PieDataSet(data, "Gastos");
        set.setValueTextColor(Color.rgb(0,0,0));
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData pieData = new PieData(set);
        pieChart.setData(pieData);
    }

    private void setDatePicker() {
        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);
        picker
                .setDayOfWeekTextColor(Color.BLACK)
                .setTodayButtonTextColor(Color.rgb(255, 143, 0))
                .setTodayDateTextColor(Color.BLACK)
                .setUnselectedDayTextColor(getResources().getColor(R.color.dorado))
                .setMonthAndYearTextColor(Color.BLACK)
                .setDateSelectedTextColor(Color.BLACK)
                .setDateSelectedColor(getResources().getColor(R.color.verdeClaro))
                .setListener(this)
                .showTodayButton(true)
                .setBackgroundColor(getResources().getColor(R.color.barColor));
        picker.init();
        picker.setDate(new DateTime());
        Log.d("###############",new DateTime().toString());
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
