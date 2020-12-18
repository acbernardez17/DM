package com.aad.esei.uvigo.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.CategoriaGasto;
import com.aad.esei.uvigo.core.DBManager;
import com.aad.esei.uvigo.core.GastoDAO;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ExpenseDetailsActivity extends AppCompatActivity {

    private CategoriaGasto categoria;
    private String pk;
    private String pk_gasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_expen_layout);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        categoria = (CategoriaGasto) ExpenseDetailsActivity.this.getIntent().getExtras().get("cat");
        pk = (String) ExpenseDetailsActivity.this.getIntent().getExtras().get("pk");

        TextView tipoCateg = this.findViewById(R.id.lbl_tipo_categ);

        Bundle bundle = ExpenseDetailsActivity.this.getIntent().getExtras();
        if (bundle.containsKey("pk_gasto")) {
            int id_gasto = (Integer) ExpenseDetailsActivity.this.getIntent().getExtras().get("pk_gasto");
            pk_gasto = Integer.toString(id_gasto);
        } else {
            pk_gasto = "";
        }

        if (!"".equals(pk_gasto)) {
            actionBar.setTitle(getString(R.string.edit_gasto));
            tipoCateg.setText(categoria.getCategoria());
            this.fillExpenseData();
        } else {
            actionBar.setTitle(getString(R.string.add_gasto));
            tipoCateg.setText(categoria.getCategoria());
        }
    }

    private boolean validateInput() {
        TextInputEditText titulo = this.findViewById(R.id.titulo_gasto);
        TextInputEditText precio = this.findViewById(R.id.precio_gasto);

        boolean correcto = true;
        String str_titulo = titulo.getText().toString();
        String str_precio = precio.getText().toString();

        try {
            Double d = Double.valueOf(str_precio);
        } catch (NumberFormatException e) {
            correcto = false;
        }
        if (str_titulo.trim().isEmpty()) {
            correcto = false;
        }
        return correcto;
    }

    private void fillExpenseData() {
        TextInputEditText titulo = this.findViewById(R.id.titulo_gasto);
        TextInputEditText precio = this.findViewById(R.id.precio_gasto);

        GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        Cursor cursorGasto = gastoDAO.getGastoById(this.pk_gasto);
        if (cursorGasto.moveToFirst()) {
            titulo.setText(cursorGasto.getString(cursorGasto.getColumnIndexOrThrow(DBManager.GASTO_TITULO)));
            precio.setText(Double.toString(cursorGasto.getDouble(cursorGasto.getColumnIndexOrThrow(DBManager.GASTO_PRECIO))));
        }
    }

    private void updateExpenseDetails() {
        TextInputEditText titulo = this.findViewById(R.id.titulo_gasto);
        TextInputEditText precio = this.findViewById(R.id.precio_gasto);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.GASTO_CATEGORIA, this.categoria.getCodigo());
        valores.put(DBManager.GASTO_TITULO, titulo.getText().toString());
        valores.put(DBManager.GASTO_PRECIO, precio.getText().toString());
        GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        gastoDAO.update(this.pk_gasto, valores);
    }

    private void saveExpenseDetails() {
        TextInputEditText titulo = this.findViewById(R.id.titulo_gasto);
        TextInputEditText precio = this.findViewById(R.id.precio_gasto);

        ContentValues valores = new ContentValues();
        valores.put(DBManager.GASTO_CATEGORIA, this.categoria.getCodigo());
        valores.put(DBManager.GASTO_TITULO, titulo.getText().toString());
        valores.put(DBManager.GASTO_PRECIO, precio.getText().toString());
        valores.put(DBManager.GASTO_ID_COCHE, this.pk);

        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.pattern), Locale.ROOT);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        valores.put(DBManager.GASTO_FECHA, formatter.format(Calendar.getInstance().getTime()));

        GastoDAO gastoDAO = new GastoDAO(DBManager.getManager(this.getApplicationContext()));
        gastoDAO.insert(valores);
    }

    private void saveGasto() {
        if (this.validateInput()) {
            if (!"".equals(pk_gasto)) {
                this.updateExpenseDetails();
            } else {
                this.saveExpenseDetails();
            }
            this.setResult(RESULT_OK);
            this.finish();
        } else {
            Toast.makeText(this, getString(R.string.info_valida), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_icon:
                this.saveGasto();
                return true;
            case android.R.id.home:
                this.setResult(RESULT_CANCELED);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}