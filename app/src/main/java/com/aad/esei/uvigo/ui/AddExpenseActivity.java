package com.aad.esei.uvigo.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
    private String pk_gasto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expen_layout);


        categoria = (Categoria_Gasto) AddExpenseActivity.this.getIntent().getExtras().get(getString(R.string.cat));
        pk = (String) AddExpenseActivity.this.getIntent().getExtras().get(getString(R.string.pk));


        TextView tipoCateg = this.findViewById(R.id.lbl_tipo_categ);


        Bundle bundle = AddExpenseActivity.this.getIntent().getExtras();
        if (bundle.containsKey(getString(R.string.pk_gasto))){
            int id_gasto = (Integer) AddExpenseActivity.this.getIntent().getExtras().get(getString(R.string.pk_gasto));
            pk_gasto = Integer.toString(id_gasto);
        }else{
            pk_gasto= "";
        }

        if ( !"".equals(pk_gasto) ) {
            tipoCateg.setText(getString(R.string.edit_gasto)+ categoria.getCategoria());
            this.fillExpenseData();
        } else{
            tipoCateg.setText(getString(R.string.add_gasto)+ categoria.getCategoria());
        }


        Button saveCarDetailsButton = this.findViewById(R.id.btn_guardar_gasto);
        saveCarDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddExpenseActivity.this.validateInput()) {
                    if (!"".equals(pk_gasto)){
                        AddExpenseActivity.this.updateExpenseDetails();
                        AddExpenseActivity.this.setResult(RESULT_OK);
                        AddExpenseActivity.this.finish();
                    }else{
                        AddExpenseActivity.this.saveExpenseDetails();
                        AddExpenseActivity.this.setResult(RESULT_OK);
                        AddExpenseActivity.this.finish();
                    }

                }else{
                    Toast.makeText(AddExpenseActivity.this, getString(R.string.info_valida), Toast.LENGTH_LONG).show();
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

    private void fillExpenseData() {
        EditText titulo = this.findViewById(R.id.titulo_gasto);
        EditText precio = this.findViewById(R.id.precio_gasto);

        GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        Cursor cursorGasto = gastoDAO.getGastoById(this.pk_gasto);
        if (cursorGasto.moveToFirst()){
            titulo.setText(cursorGasto.getString(cursorGasto.getColumnIndexOrThrow(DBManager.GASTO_TITULO)));
            precio.setText(Double.toString(cursorGasto.getDouble(cursorGasto.getColumnIndexOrThrow(DBManager.GASTO_PRECIO))));
        }
    }

    private void updateExpenseDetails(){
        EditText titulo = this.findViewById(R.id.titulo_gasto);
        EditText precio = this.findViewById(R.id.precio_gasto);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.GASTO_CATEGORIA,this.categoria.getCodigo());
        valores.put(DBManager.GASTO_TITULO,titulo.getText().toString());
        valores.put(DBManager.GASTO_PRECIO,precio.getText().toString());
        GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        gastoDAO.update(this.pk_gasto,valores);
    }

    private void saveExpenseDetails() {

        EditText titulo = this.findViewById(R.id.titulo_gasto);
        EditText precio = this.findViewById(R.id.precio_gasto);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.GASTO_CATEGORIA,this.categoria.getCodigo());
        valores.put(DBManager.GASTO_TITULO,titulo.getText().toString());
        valores.put(DBManager.GASTO_PRECIO,precio.getText().toString());
        valores.put(DBManager.GASTO_ID_COCHE,this.pk);

        SimpleDateFormat formatter=new SimpleDateFormat(getString(R.string.pattern), Locale.ROOT);
        formatter.setTimeZone(TimeZone.getTimeZone(getString(R.string.utc)));
        valores.put(DBManager.GASTO_FECHA, formatter.format(Calendar.getInstance().getTime()));

        GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        gastoDAO.insert(valores);
    }
}