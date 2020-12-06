package com.aad.esei.uvigo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.aad.esei.uvigo.core.CustomList;
import com.aad.esei.uvigo.R;

public class MainActivity extends AppCompatActivity {
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

    private String[] perfiles ={
            "Perfil1",
            "Perfil2",
            "Perfil3"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);



        ListView listView = (ListView)findViewById(R.id.list_icons);
        CustomList list =  new CustomList(this,tipoGastos, FechaGastos, KmGastos, CantidadGastos, idIcono);
        listView.setAdapter(list);

        Spinner SpPerfil = (Spinner) findViewById(R.id.sp_perfil);
        SpPerfil.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,perfiles));

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
                this.startActivity(new Intent(this, StatisticsActivity.class));
                toret = true;
                break;
            case R.id.detalles_coche:
                this.startActivity(new Intent(this, CarDetailsActivity.class));
                toret = true;
                break;
        }

        return toret;
    }
}