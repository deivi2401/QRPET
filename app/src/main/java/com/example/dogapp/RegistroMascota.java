package com.example.dogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.dogapp.MapaPetV2;

import java.util.Map;

public class RegistroMascota extends AppCompatActivity {

    private EditText editTextNombreMascota, editTextEdadMascota, editTextDireccion;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegistrarGeneroPet;
    private RadioButton radioButtonRegistrarGeneroSelecPet;
    private Button RegistrarMascota;
    private ImageButton MenuPrincipal,Mapa;
    public String Direccion;


    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                Intent data = result.getData();
                String Direccion = data.getStringExtra("Direccion");
                if (!TextUtils.isEmpty(Direccion)){
                    editTextDireccion.setText(Direccion);
                }
            }
        }
    });

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_mascota);
        Toast.makeText(RegistroMascota.this, "Ya puede registrar a su mascota", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressBar);

        RegistrarMascota = findViewById(R.id.button_register_pet);
        MenuPrincipal = (ImageButton) findViewById(R.id.BotonHome);
        Mapa = (ImageButton) findViewById(R.id.imageViewMapa);

        editTextNombreMascota = findViewById(R.id.editText_register_full_nombre_mascota);
        editTextEdadMascota = findViewById(R.id.editText_edad_mascota);
        editTextDireccion = findViewById(R.id.editText_register_direccion);
        radioGroupRegistrarGeneroPet = findViewById(R.id.radio_group_register_gender_mascota);
        radioGroupRegistrarGeneroPet.clearCheck();



        MenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroMascota.this, PantallaInicio.class);
                startActivity(intent);
            }
        });

        Mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroMascota.this, MapaPetV2.class);
                Toast.makeText(RegistroMascota.this, "Seleccione su direccion", Toast.LENGTH_SHORT).show();
                activityResultLaunch.launch(intent);
            }
        });



        RegistrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedGeneroId = radioGroupRegistrarGeneroPet.getCheckedRadioButtonId();
                radioButtonRegistrarGeneroSelecPet = findViewById(selectedGeneroId);

                String textNombreMascota = editTextNombreMascota.getText().toString();
                String textEdadMascota = editTextEdadMascota.getText().toString();
                String textDireccion = editTextDireccion.getText().toString();
                String GeneroMascota;

                if (TextUtils.isEmpty(textNombreMascota)){
                    Toast.makeText(RegistroMascota.this, "Porfavor Ingrese el nombre de su mascota", Toast.LENGTH_LONG).show();
                    editTextNombreMascota.setError("Se requiere el nombre de la mascota");
                    editTextNombreMascota.requestFocus();
                } else if (TextUtils.isEmpty(textEdadMascota)) {
                    Toast.makeText(RegistroMascota.this, "Porfavor Ingrese el nombre de su mascota", Toast.LENGTH_SHORT).show();
                    editTextEdadMascota.setError("Se requiere la edad de su mascota");
                    editTextEdadMascota.requestFocus();
                } else if (TextUtils.isEmpty(textDireccion)) {
                    Toast.makeText(RegistroMascota.this, "Porfavor ingrese una direccion", Toast.LENGTH_SHORT).show();
                    editTextDireccion.setError("Se requiere un domicilio para su mascota");
                    editTextDireccion.requestFocus();
                }else {
                    GeneroMascota = radioButtonRegistrarGeneroSelecPet.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerPet(textNombreMascota,textEdadMascota,textDireccion,GeneroMascota);
                }

            }
        });



    }


    private void registerPet(String textNombreMascota, String textEdadMascota, String textDireccion, String generoMascota) {
        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users").child(usuarioActual.getUid()).child("Mascotas").child(textNombreMascota);


        ReadWritePetDetails writePetDetails = new ReadWritePetDetails(textNombreMascota, textEdadMascota, textDireccion,generoMascota);

        referenceProfile.setValue(writePetDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistroMascota.this, "Se registro la mascota con exito", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegistroMascota.this, "Se fallo al registrar la mascota", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
