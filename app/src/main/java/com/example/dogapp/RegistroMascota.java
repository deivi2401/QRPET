package com.example.dogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroMascota extends AppCompatActivity {

    private Button RegistrarMascota;
    private ImageButton MenuPrincipal;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_mascotas);

        RegistrarMascota = findViewById(R.id.BotonRegistroMascota);
        MenuPrincipal = (ImageButton) findViewById(R.id.BotonHome);

        MenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroMascota.this, PantallaInicio.class);
                startActivity(intent);
            }
        });

    }
}
