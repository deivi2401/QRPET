package com.example.dogapp;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PantallaInicio extends AppCompatActivity {

    private Button RegistroMascota, ConsultaMascota, Perfil, CerrarSesion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio);
        RegistroMascota = findViewById(R.id.BotonRegistroMascota);
        ConsultaMascota = findViewById(R.id.BotonConsulta);
        Perfil = findViewById(R.id.BotonPerfil);
        CerrarSesion = findViewById(R.id.BotonCerrar_Sesion);

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaInicio.this, MainActivity.class);
                startActivity(intent);
            }
        });

        RegistroMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaInicio.this, RegistroMascota.class);
                startActivity(intent);
            }
        });

        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaInicio.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(PantallaInicio.this, "Se ha cerrado sesion", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PantallaInicio.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
