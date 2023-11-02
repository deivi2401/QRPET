package com.example.dogapp;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;

public class PantallaInicio extends AppCompatActivity {

    private Button RegistroMascota, ConsultaMascota, Perfil,LectorQR, CerrarSesion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());
        setContentView(R.layout.pantalla_inicio);
        RegistroMascota = findViewById(R.id.BotonRegistroMascota);
        ConsultaMascota = findViewById(R.id.BotonConsulta);
        Perfil = findViewById(R.id.BotonPerfil);
        LectorQR = findViewById(R.id.botonAbrirEscaner);
        CerrarSesion = findViewById(R.id.BotonCerrar_Sesion);

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaInicio.this, MainActivity.class);
                startActivity(intent);
            }
        });

        LectorQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador = new IntentIntegrator(PantallaInicio.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector QR");
                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
            }
        });

        RegistroMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaInicio.this, RegistroMascota.class);
                startActivity(intent);
            }
        });

        ConsultaMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaInicio.this, Lista_Mascotas.class);
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
