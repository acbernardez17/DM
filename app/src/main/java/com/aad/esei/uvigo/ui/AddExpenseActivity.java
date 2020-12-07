package com.aad.esei.uvigo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.Categoria_Gasto;
import com.aad.esei.uvigo.core.CocheDAO;
import com.aad.esei.uvigo.core.DBManager;

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

        if (!categoria.equals(Categoria_Gasto.REP)){
            View v = findViewById(R.id.layout_rep);
            v.setVisibility(View.GONE);
        }

        Button saveCarDetailsButton = this.findViewById(R.id.btn_guardar_gasto);
        saveCarDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpenseActivity.this.saveExpenseDetails();
                AddExpenseActivity.this.finish();
            }
        });

        Button btnBackGasto = this.findViewById(R.id.btn_back_gasto);
        btnBackGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpenseActivity.this.finish();
            }
        });
    }

    private void saveExpenseDetails() {
        EditText titulo = this.findViewById(R.id.titulo_gasto);
        EditText precio = this.findViewById(R.id.precio_gasto);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.GASTO_TITULO,titulo.getText().toString());
        valores.put(DBManager.GASTO_PRECIO,precio.getText().toString());
        valores.put(DBManager.GASTO_ID_COCHE,precio.getText().toString());

        if (categoria.equals(Categoria_Gasto.REP)){
            EditText precio_litro = this.findViewById(R.id.precio_litro);
            EditText litros_antes = this.findViewById(R.id.litros_antes);
            EditText litros_repostados = this.findViewById(R.id.litros_repostados);
            EditText km_actuales = this.findViewById(R.id.km_actuales);

        }

        //GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        //gastoDAO.insert(pkdeGasto.getText().toString(), valores);
    }
}