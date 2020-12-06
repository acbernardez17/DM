package com.aad.esei.uvigo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aad.esei.uvigo.R;

public class CarDetailsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.car_details_editor_layout);

        Spinner spinnerCombustible = this.findViewById(R.id.spinner_combustible);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
            this, R.array.tipos_combustible, android.R.layout.simple_spinner_item
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCombustible.setAdapter(spinnerAdapter);

        spinnerCombustible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Aquí se seleccionaría el string para ser posteriormente guardado en la clase Car
                String selected = (String) parent.getItemAtPosition(position);
                Toast.makeText(CarDetailsActivity.this, selected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


    }
}
