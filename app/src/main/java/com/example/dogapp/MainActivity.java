package com.example.dogapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dogapp.databinding.InicioSesionBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button InicioSesion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);
        InicioSesion = findViewById(R.id.IniciarSesion);


        InicioSesion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, PantallaInicio.class);
                startActivity(intent);
            }
        });
    }


}