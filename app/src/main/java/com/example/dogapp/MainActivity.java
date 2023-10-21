package com.example.dogapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dogapp.databinding.InicioSesionBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressbar;
    private FirebaseAuth authProfile;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressbar = findViewById(R.id.progressBar);
        
        authProfile = FirebaseAuth.getInstance();
        
        //Login User
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();
                
                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(MainActivity.this, "Porfavor ingrese un correo electronico", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Se requiere de un correo electronico");
                    editTextLoginEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(MainActivity.this, "Porfavor re-ingrese un correo electronico", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Se requiere de un correo electronico valido");
                    editTextLoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(MainActivity.this, "Porfavor ingrese una contraseña", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Se requiere de una contraseña");
                    editTextLoginPwd.requestFocus();
                } else{
                    progressbar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPwd);
                }
            }
        }));

    }

    private void loginUser(String email, String pwd) {
        authProfile.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Ha iniciado sesion", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
                progressbar.setVisibility(View.GONE);
            }
        });
    }


}