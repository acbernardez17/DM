package com.aad.esei.uvigo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.aad.esei.uvigo.R;
import com.aad.esei.uvigo.core.CocheDAO;
import com.aad.esei.uvigo.core.DBManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int ADD_CAR_CODE = 101;
    private final int EDIT_CAR_CODE = 102;

    private ListView listView;

    private String[] tipoGastos= {
            "",
            "Reabastecimiento",
            "Taller",
            "Taller",
            "Reabastecimiento",
            "Reabastecimiento",
            ""
    };
    private String[] FechaGastos= {
            "",
            "10-11-2020",
            "15-11-2020",
            "20-11-2020",
            "20-11-2020",
            "28-11-2020",
            ""
    };
    private String[] KmGastos= {
            "",
            "200.000 km",
            "200.100 km",
            "200.150 km",
            "200.200 km",
            "200.250 km",
            ""
    };
    private String[] CantidadGastos= {
            "",
            "50,00€",
            "100,00€",
            "80,00€",
            "45,00€",
            "60,00€",
            ""
    };
    private Integer[] idIcono = {
            0,
            R.drawable.gasol_svg,
            R.drawable.taller_svg,
            R.drawable.taller_svg,
            R.drawable.gasol_svg,
            R.drawable.gasol_svg,
            0

    };

    private DBManager manager;
    private ArrayList<String> perfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.manager = DBManager.getManager(this.getApplicationContext());
        setContentView(R.layout.main_layout);

        ListView listView = (ListView)findViewById(R.id.list_icons);
        CustomList list =  new CustomList(this,tipoGastos, FechaGastos, KmGastos, CantidadGastos, idIcono);
        listView.setAdapter(list);

        ImageButton btnAddCar = (ImageButton) findViewById(R.id.btn_profile);
        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivityForResult(
                        new Intent(MainActivity.this, CarDetailsActivity.class)
                        .putExtra("pk", ""), ADD_CAR_CODE);
            }
        });

        this.updateSpinnerPerfiles();
    }

    private void updateSpinnerPerfiles() {
        Cursor cursorCoches = new CocheDAO(this.manager).getAllCoches();
        this.perfiles = new ArrayList<>();
        if (cursorCoches.moveToFirst()) {
            do {
                perfiles.add( cursorCoches.getString(0) );
            } while (cursorCoches.moveToNext() );
        } else {
            Toast.makeText(this, "Primera ejecución de la app: necesitas registrar un coche",
                    Toast.LENGTH_LONG).show();
            this.startActivityForResult(
                    new Intent(this, CarDetailsActivity.class)
                    .putExtra("pk", ""), ADD_CAR_CODE);
        }

        Spinner spinnerPerfil = (Spinner) findViewById(R.id.sp_perfil);
        spinnerPerfil.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, perfiles
        ));
    }

    private String getSpinnerSelection() {
        Spinner spinnerPerfil = (Spinner) findViewById(R.id.sp_perfil);
        return spinnerPerfil.getSelectedItem().toString();
    }

    private void setSpinnerSelection(String text) {
        Spinner spinnerPerfil = (Spinner) findViewById(R.id.sp_perfil);
        int pos = this.perfiles.indexOf(text);
        spinnerPerfil.setSelection(pos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ADD_CAR_CODE) {
            String pk = data.getExtras().getString("pk");

            Toast.makeText(this, "Nuevo coche añadido con PK: " + pk, Toast.LENGTH_LONG).show();
            this.updateSpinnerPerfiles();
            this.setSpinnerSelection(pk);

        } else if (resultCode == RESULT_CANCELED && requestCode == ADD_CAR_CODE) {
            if (new CocheDAO(this.manager).getCochesCount() == 0) {
                this.finish();
            }

        } else if (resultCode == RESULT_OK && requestCode == EDIT_CAR_CODE) {
            this.updateSpinnerPerfiles();
        }
    }

    // Options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean toret = false;

        switch (item.getItemId()) {
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
}