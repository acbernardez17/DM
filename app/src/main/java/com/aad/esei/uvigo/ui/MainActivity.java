package com.aad.esei.uvigo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.CategoriaGasto;
import com.aad.esei.uvigo.core.CocheDAO;
import com.aad.esei.uvigo.core.DBManager;
import com.aad.esei.uvigo.core.GastoDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int ADD_CAR_CODE = 101;
    private final int EDIT_CAR_CODE = 102;
    private final int ADD_EXPENSE = 201;
    private final int EDIT_EXPENSE = 202;

    private int categNew;
    private GastosCursorAdapter gastosCursorAdapter;
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

    }

    @Override
    public void onStart() {
        super.onStart();
        this.manager = DBManager.getManager(this.getApplicationContext());

        final ListView listView = (ListView) findViewById(R.id.list_icons);
        registerForContextMenu(listView);
        this.gastosCursorAdapter = new GastosCursorAdapter(this, this.gastoDAO.getAllGastosCoche(getSpinnerSelection()));
        listView.setAdapter(gastosCursorAdapter);
        this.updateCursorList();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.manager.close();
        this.gastosCursorAdapter.getCursor().close();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cursor;
        int ID;
        String category;
        switch (item.getItemId()) {
            case R.id.edit_OP:
                cursor = (Cursor) this.gastosCursorAdapter.getCursor();
                cursor.moveToPosition(info.position);
                ID = cursor.getInt(cursor.getColumnIndexOrThrow(DBManager.GASTO_ID));
                category = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.GASTO_CATEGORIA));
                updateExpense(ID, category);
                return true;
            case R.id.delete_op:
                cursor = (Cursor) this.gastosCursorAdapter.getCursor();
                cursor.moveToPosition(info.position);
                ID = cursor.getInt(cursor.getColumnIndexOrThrow(DBManager.GASTO_ID));
                verifyDelete(ID);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void updateExpense(int id, String categoria) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        this.categNew = CategoriaGasto.getByCode(categoria).ordinal();

        dlg.setTitle(getString(R.string.sel_tipo_gasto));

        dlg.setSingleChoiceItems(
                CategoriaGasto.arrayCategorias(),
                CategoriaGasto.getByCode(categoria).ordinal(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.categNew = which;
                    }
                }
        );
        dlg.setPositiveButton(getString(R.string.siguiente), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.startActivityForResult(
                        new Intent(MainActivity.this, ExpenseDetailsActivity.class)
                                .putExtra("cat", CategoriaGasto.values()[MainActivity.this.categNew])
                                .putExtra("pk_gasto", id)
                                .putExtra("pk", getSpinnerSelection()), EDIT_EXPENSE);
                ;
            }
        });
        dlg.setNegativeButton(getString(R.string.cancel), null);

        dlg.create().show();
    }

    private void verifyDelete(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(getString(R.string.borrar));
        builder.setMessage(getString(R.string.preg_borrar_gasto));
        builder.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                gastoDAO.delete(Integer.toString(id));
                MainActivity.this.updateCursorList();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
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
                this.perfiles.add(cursorCoches.getString(0));
            } while (cursorCoches.moveToNext());
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

        } else if (resultCode == RESULT_OK && (requestCode == ADD_EXPENSE || requestCode == EDIT_EXPENSE)) {
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
                        .putExtra("coche", getSpinnerSelection()));
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
        dlg.setTitle(getString(R.string.primera_eje));
        dlg.setMessage(getString(R.string.need_add));
        dlg.setPositiveButton(getString(R.string.new_coche), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchActivityNewCar();
            }
        });
        dlg.setNegativeButton(getString(R.string.salir), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        dlg.setCancelable(false);
        dlg.show();
    }

    private void showAddDialog() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        this.categNew = 0;

        dlg.setTitle(getString(R.string.sel_tipo_gasto));

        dlg.setSingleChoiceItems(
                CategoriaGasto.arrayCategorias(),
                0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.categNew = which;
                    }
                }
        );
        dlg.setPositiveButton(getString(R.string.siguiente), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.startActivityForResult(
                        new Intent(MainActivity.this, ExpenseDetailsActivity.class)
                                .putExtra("cat", CategoriaGasto.values()[MainActivity.this.categNew])
                                .putExtra("pk", getSpinnerSelection()), ADD_EXPENSE);
                ;
            }
        });
        dlg.setNegativeButton(getString(R.string.cancel), null);

        dlg.create().show();
    }

    private void updateCursorList() {
        Cursor cursor = this.gastoDAO.getAllGastosCoche(getSpinnerSelection());
        this.gastosCursorAdapter.changeCursor(cursor);
    }

}