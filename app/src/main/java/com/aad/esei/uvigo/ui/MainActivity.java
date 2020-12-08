package com.aad.esei.uvigo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.Categoria_Gasto;
import com.aad.esei.uvigo.core.CocheDAO;
import com.aad.esei.uvigo.core.DBManager;
import com.aad.esei.uvigo.core.GastoDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int ADD_CAR_CODE = 101;
    private final int EDIT_CAR_CODE = 102;
    private final int ADD_EXPENSE = 201;

    private int categNew;
    private ElementCursorAdapter elementCursorAdapter;
    private GastoDAO gastoDAO;
    private CocheDAO cocheDAO;

    private DBManager manager;
    private ArrayList<String> perfiles;
    private String selectedPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.manager = DBManager.getManager(this.getApplicationContext());
        setContentView(R.layout.main_layout);

        FloatingActionButton fab = (FloatingActionButton) this.findViewById(R.id.fbtn_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.showAddDialog();
            }
        });

        this.cocheDAO = new CocheDAO(this.manager);
        this.gastoDAO = new GastoDAO(manager);

        this.updateSpinnerPerfiles();
        this.setSpinnerSelection(0);

        ListView listView = (ListView)findViewById(R.id.list_icons);
        this.elementCursorAdapter = new ElementCursorAdapter(this, this.gastoDAO.getAllGastosCoche(getSpinnerSelection()));
        listView.setAdapter(elementCursorAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateSpinnerPerfiles();
        this.updateCursorList();
    }

    private void setupPerfilSelector() {
        AutoCompleteTextView spinnerCombustibleText = this.findViewById(R.id.sp_perfil);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, perfiles
        );
        spinnerCombustibleText.setAdapter(spinnerAdapter);

        spinnerCombustibleText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPerfil = perfiles.get(position);
                MainActivity.this.updateCursorList();
            }
        });
    }

    private void updateSpinnerPerfiles() {
        Cursor cursorCoches = cocheDAO.getAllCoches();
        this.perfiles = new ArrayList<>();
        if (cursorCoches.moveToFirst()) {
            do {
                this.perfiles.add( cursorCoches.getString(0) );
            } while (cursorCoches.moveToNext() );
        } else {
            this.showFirstTimeDialog();
        }

        this.setupPerfilSelector();
    }

    private String getSpinnerSelection() {
        return selectedPerfil;
    }

    private void setSpinnerSelection(int pos) {
        if (perfiles.size() > pos) {
            AutoCompleteTextView spinnerCombustibleText = this.findViewById(R.id.sp_perfil);
            String text = perfiles.get(pos);

            spinnerCombustibleText.setText(text);
            this.selectedPerfil = text;
            this.setupPerfilSelector();
        } else {
            this.selectedPerfil = "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ADD_CAR_CODE) {
            String pk = data.getExtras().getString("pk");

            Toast.makeText(this, "Nuevo coche añadido con PK: " + pk, Toast.LENGTH_LONG).show();
            this.updateSpinnerPerfiles();
            this.setSpinnerSelection(perfiles.size() - 1);
            this.updateCursorList();

        } else if (resultCode == RESULT_CANCELED && requestCode == ADD_CAR_CODE) {
            if (cocheDAO.getCochesCount() == 0) {
                this.showFirstTimeDialog();
            }

        } else if (resultCode == RESULT_OK && requestCode == EDIT_CAR_CODE) {
            this.updateSpinnerPerfiles();

        } else if (resultCode == RESULT_CANCELED && requestCode == EDIT_CAR_CODE) {
            // Nada

        } else if(resultCode == RESULT_OK && requestCode == ADD_EXPENSE){
            this.updateCursorList();
        }
    }

    // Options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean toret = false;

        switch (item.getItemId()) {
            case R.id.nuevo_coche:
                this.launchActivityNewCar();
                toret = true;
                break;

            case R.id.estadisticas:
                this.startActivity(new Intent(this, StatisticsActivity.class)
                        .putExtra("coche", getSpinnerSelection() ));
                toret = true;
                break;

            case R.id.detalles_coche:
                this.startActivityForResult(
                        new Intent(this, CarDetailsActivity.class)
                        .putExtra("pk", getSpinnerSelection()), EDIT_CAR_CODE);
                toret = true;
                break;
        }

        return toret;
    }

    private void launchActivityNewCar() {
        this.startActivityForResult(
                new Intent(this, CarDetailsActivity.class)
                        .putExtra("pk", ""), ADD_CAR_CODE);
    }

    private void showFirstTimeDialog() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("Primera ejecución");
        dlg.setMessage("Necesitas añadir al menos un coche para poder usar la aplicación");
        dlg.setPositiveButton("Nuevo coche", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchActivityNewCar();
            }
        });
        dlg.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        dlg.setCancelable(false);
        dlg.show();
    }

    private void showAddDialog(){
        AlertDialog.Builder dlg = new AlertDialog.Builder( this );
        this.categNew = 0;

        dlg.setTitle( "Selecciona tipo de gasto:" );

        dlg.setSingleChoiceItems(
                //new String[]{Categoria_Gasto.REP.toString(), "Otro"},
                Categoria_Gasto.arrayCategorias(),
                0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.categNew = which;
                    }
                }
        );
        dlg.setPositiveButton("Siguiente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.startActivityForResult(
                        new Intent(MainActivity.this, AddExpenseActivity.class)
                                .putExtra("cat", Categoria_Gasto.values()[MainActivity.this.categNew])
                                .putExtra("pk", getSpinnerSelection()),ADD_EXPENSE);
                                ;
            }
        });
        dlg.setNegativeButton("Cancelar", null);

        dlg.create().show();
    }

    private void updateCursorList(){
        Cursor cursor = this.gastoDAO.getAllGastosCoche(getSpinnerSelection());
        this.elementCursorAdapter.changeCursor(cursor);
    }

}