package com.aad.esei.uvigo.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.CocheDAO;
import com.aad.esei.uvigo.core.DBManager;

public class CarDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.car_details_editor_layout);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.setupTipoCombustibleDropdown();

        String pk = (String) CarDetailsActivity.this.getIntent().getExtras().get("pk");
        if ( !"".equals(pk) ) {
            actionBar.setTitle("Editar coche");
            this.fillCarData(pk);
        } else {
            actionBar.setTitle("Nuevo coche");
        }
    }

    private void setupTipoCombustibleDropdown() {
        AutoCompleteTextView spinnerCombustibleText = this.findViewById(R.id.spinner_combustible);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.tipos_combustible, android.R.layout.simple_spinner_dropdown_item
        );
        spinnerCombustibleText.setAdapter(spinnerAdapter);
    }

    private void fillCarData(String pk) {
        EditText matricula = this.findViewById(R.id.coche_matricula);
        EditText nombre = this.findViewById(R.id.coche_nombre);
        AutoCompleteTextView spinnerCombustible = this.findViewById(R.id.spinner_combustible);
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
            spinnerCombustible.setText(cursorCoches.getString(4), false);
            combustibleMax.setText(cursorCoches.getString(5));
            kmIniciales.setText(cursorCoches.getString(6));
            litrosIniciales.setText(cursorCoches.getString(7));
        }
    }

    private String updateCarDetails(String pk) {
        EditText nombre = this.findViewById(R.id.coche_nombre);
        AutoCompleteTextView spinnerCombustible = this.findViewById(R.id.spinner_combustible);
        EditText combustibleMax = this.findViewById(R.id.coche_combustible_max);
        EditText kmIniciales = this.findViewById(R.id.coche_km_iniciales);
        EditText litrosIniciales = this.findViewById(R.id.coche_litros_iniciales);
        EditText marca = this.findViewById(R.id.coche_marca);
        EditText modelo = this.findViewById(R.id.coche_modelo);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.COCHE_NOMBRE, nombre.getText().toString());
        valores.put(DBManager.COCHE_TIPO_COMBUSTIBLE, spinnerCombustible.getText().toString());
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
        AutoCompleteTextView spinnerCombustible = this.findViewById(R.id.spinner_combustible);
        EditText combustibleMax = this.findViewById(R.id.coche_combustible_max);
        EditText kmIniciales = this.findViewById(R.id.coche_km_iniciales);
        EditText litrosIniciales = this.findViewById(R.id.coche_litros_iniciales);
        EditText marca = this.findViewById(R.id.coche_marca);
        EditText modelo = this.findViewById(R.id.coche_modelo);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.COCHE_NOMBRE, nombre.getText().toString());
        valores.put(DBManager.COCHE_TIPO_COMBUSTIBLE, spinnerCombustible.getText().toString());
        valores.put(DBManager.COCHE_COMBUSTIBLE_MAX, combustibleMax.getText().toString());
        valores.put(DBManager.COCHE_KM_INICIALES, kmIniciales.getText().toString());
        valores.put(DBManager.COCHE_LITROS_INICIALES, litrosIniciales.getText().toString());
        valores.put(DBManager.COCHE_MARCA, marca.getText().toString());
        valores.put(DBManager.COCHE_MODELO, modelo.getText().toString());

        CocheDAO cocheDAO = new CocheDAO(DBManager.getManager(this.getApplicationContext()));
        return cocheDAO.insert(matricula.getText().toString(), valores);
    }

    private void saveCar() {
        if (this.validateInput()) {
            String pk = (String) CarDetailsActivity.this.getIntent().getExtras().get("pk");
            String primaryKey = "";
            if ("".equals(pk)) {
                primaryKey = CarDetailsActivity.this.saveCarDetails();
            } else {
                primaryKey = CarDetailsActivity.this.updateCarDetails(pk);
            }

            CarDetailsActivity.this.setResult(RESULT_OK, new Intent()
                    .putExtra("pk", primaryKey));

            CarDetailsActivity.this.finish();
        }
    }

    private boolean validateInput() {
        // Validar aquí los datos
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.add_car_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_car_icon:
                this.saveCar();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
