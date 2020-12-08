package com.aad.esei.uvigo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.Categoria_Gasto;
import com.aad.esei.uvigo.core.CocheDAO;
import com.aad.esei.uvigo.core.DBManager;
import com.aad.esei.uvigo.core.GastoDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AddExpenseActivity extends AppCompatActivity {

    private Categoria_Gasto categoria;
    private String pk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expen_layout);

        categoria = (Categoria_Gasto) AddExpenseActivity.this.getIntent().getExtras().get("cat");
        pk = (String) AddExpenseActivity.this.getIntent().getExtras().get("pk");

        TextView tipoCateg = this.findViewById(R.id.lbl_tipo_categ);
        tipoCateg.setText("Añadir Gasto "+categoria.getCategoria());



        Button saveCarDetailsButton = this.findViewById(R.id.btn_guardar_gasto);
        saveCarDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddExpenseActivity.this.validateInput()) {
                    AddExpenseActivity.this.saveExpenseDetails();
                    AddExpenseActivity.this.setResult(RESULT_OK);
                    AddExpenseActivity.this.finish();
                }else{
                    Toast.makeText(AddExpenseActivity.this, "Informacion inválida", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnBackGasto = this.findViewById(R.id.btn_back_gasto);
        btnBackGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpenseActivity.this.setResult(RESULT_CANCELED);
                AddExpenseActivity.this.finish();
            }
        });
    }

    private boolean validateInput() {
        // Validar aquí los datos
        return true;
    }

    private void saveExpenseDetails() {

        EditText titulo = this.findViewById(R.id.titulo_gasto);
        EditText precio = this.findViewById(R.id.precio_gasto);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.GASTO_CATEGORIA,this.categoria.getCodigo());
        valores.put(DBManager.GASTO_TITULO,titulo.getText().toString());
        valores.put(DBManager.GASTO_PRECIO,precio.getText().toString());
        valores.put(DBManager.GASTO_ID_COCHE,this.pk);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        valores.put(DBManager.GASTO_FECHA,formatter.format(cal.getTime()));

        GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        gastoDAO.insert(valores);
    }
}