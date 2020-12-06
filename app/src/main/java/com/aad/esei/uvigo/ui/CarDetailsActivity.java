package com.aad.esei.uvigo.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.CocheDAO;
import com.aad.esei.uvigo.core.DBManager;

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

        Button saveCarDetailsButton = this.findViewById(R.id.coche_detalles_guardar_btn);
        saveCarDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pk = (String) CarDetailsActivity.this.getIntent().getExtras().get("pk");
                if ( "".equals(pk) ) {
                    String primaryKey = CarDetailsActivity.this.saveCarDetails();
                    CarDetailsActivity.this.setResult(RESULT_OK,new Intent()
                            .putExtra("pk", primaryKey));
                } else {
                    String primaryKey = CarDetailsActivity.this.updateCarDetails(pk);
                    CarDetailsActivity.this.setResult(RESULT_OK,new Intent()
                            .putExtra("pk", primaryKey));
                }

                CarDetailsActivity.this.finish();
            }
        });

        String pk = (String) CarDetailsActivity.this.getIntent().getExtras().get("pk");
        if ( !"".equals(pk) ) {
            this.fillCarData(pk);
        }
    }

    private void fillCarData(String pk) {
        EditText matricula = this.findViewById(R.id.coche_matricula);
        EditText nombre = this.findViewById(R.id.coche_nombre);
        Spinner spinnerCombustible = this.findViewById(R.id.spinner_combustible);
        EditText combustibleMax = this.findViewById(R.id.coche_combustible_max);
        EditText kmIniciales = this.findViewById(R.id.coche_km_iniciales);
        EditText litrosIniciales = this.findViewById(R.id.coche_litros_iniciales);
        EditText marca = this.findViewById(R.id.coche_marca);
        EditText modelo = this.findViewById(R.id.coche_modelo);

        CocheDAO cocheDAO = new CocheDAO(DBManager.getManager(this.getApplicationContext()));
        Cursor cursorCoches = cocheDAO.getCocheByMatricula(pk);
        if (cursorCoches.moveToFirst()) {
            matricula.setText(cursorCoches.getString(0));
            matricula.setEnabled(false);

            nombre.setText(cursorCoches.getString(1));
            marca.setText(cursorCoches.getString(2));
            modelo.setText(cursorCoches.getString(3));
            spinnerCombustible.setSelection(cursorCoches.getInt(4));
            combustibleMax.setText(cursorCoches.getString(5));
            kmIniciales.setText(cursorCoches.getString(6));
            litrosIniciales.setText(cursorCoches.getString(7));
        }
    }

    private String updateCarDetails(String pk) {
        EditText nombre = this.findViewById(R.id.coche_nombre);
        Spinner spinnerCombustible = this.findViewById(R.id.spinner_combustible);
        EditText combustibleMax = this.findViewById(R.id.coche_combustible_max);
        EditText kmIniciales = this.findViewById(R.id.coche_km_iniciales);
        EditText litrosIniciales = this.findViewById(R.id.coche_litros_iniciales);
        EditText marca = this.findViewById(R.id.coche_marca);
        EditText modelo = this.findViewById(R.id.coche_modelo);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.COCHE_NOMBRE, nombre.getText().toString());
        valores.put(DBManager.COCHE_TIPO_COMBUSTIBLE, spinnerCombustible.getSelectedItemPosition());
        valores.put(DBManager.COCHE_COMBUSTIBLE_MAX, combustibleMax.getText().toString());
        valores.put(DBManager.COCHE_KM_INICIALES, kmIniciales.getText().toString());
        valores.put(DBManager.COCHE_LITROS_INICIALES, litrosIniciales.getText().toString());
        valores.put(DBManager.COCHE_MARCA, marca.getText().toString());
        valores.put(DBManager.COCHE_MODELO, modelo.getText().toString());

        CocheDAO cocheDAO = new CocheDAO(DBManager.getManager(this.getApplicationContext()));
        return cocheDAO.update(pk, valores);
    }

    private String saveCarDetails() {
        EditText matricula = this.findViewById(R.id.coche_matricula);
        EditText nombre = this.findViewById(R.id.coche_nombre);
        Spinner spinnerCombustible = this.findViewById(R.id.spinner_combustible);
        EditText combustibleMax = this.findViewById(R.id.coche_combustible_max);
        EditText kmIniciales = this.findViewById(R.id.coche_km_iniciales);
        EditText litrosIniciales = this.findViewById(R.id.coche_litros_iniciales);
        EditText marca = this.findViewById(R.id.coche_marca);
        EditText modelo = this.findViewById(R.id.coche_modelo);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.COCHE_NOMBRE, nombre.getText().toString());
        valores.put(DBManager.COCHE_TIPO_COMBUSTIBLE, spinnerCombustible.getSelectedItemPosition());
        valores.put(DBManager.COCHE_COMBUSTIBLE_MAX, combustibleMax.getText().toString());
        valores.put(DBManager.COCHE_KM_INICIALES, kmIniciales.getText().toString());
        valores.put(DBManager.COCHE_LITROS_INICIALES, litrosIniciales.getText().toString());
        valores.put(DBManager.COCHE_MARCA, marca.getText().toString());
        valores.put(DBManager.COCHE_MODELO, modelo.getText().toString());

        CocheDAO cocheDAO = new CocheDAO(DBManager.getManager(this.getApplicationContext()));
        return cocheDAO.insert(matricula.getText().toString(), valores);
    }
}
