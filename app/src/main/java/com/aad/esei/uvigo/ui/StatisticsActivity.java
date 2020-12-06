package com.aad.esei.uvigo.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aad.esei.uvigo.R;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

public class StatisticsActivity extends Activity implements DatePickerListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.statistical_layout);

        // Date picker
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
        // log it for demo
        Log.i("HorizontalPicker", "Selected date is " + dateSelected.toString());
    }
}
