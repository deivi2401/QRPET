package com.example.dogapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroUsuario extends AppCompatActivity {

    private EditText editTextRegisterNombreCompleto, editTextRegistrarEmail, editTextRegistrarDoB, editTextRegistrarTelefono, editTextRegistrarContra, editTextRegistrarConfirmContra;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegistrarGenero;
    private RadioButton radioButtonRegistrarGeneroSelec;
    private DatePickerDialog picker;
    private static final String TAG = "RegistroUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);

        Toast.makeText(RegistroUsuario.this, "Ya se puede registrar", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressBar);
        editTextRegisterNombreCompleto = findViewById(R.id.editText_register_full_name);
        editTextRegistrarEmail = findViewById(R.id.editText_edad_mascota);
        editTextRegistrarDoB = findViewById(R.id.editText_register_dob);
        editTextRegistrarTelefono = findViewById(R.id.editText_register_mobile);
        editTextRegistrarContra = findViewById(R.id.editText_register_password);

        radioGroupRegistrarGenero = findViewById(R.id.radio_group_register_gender);
        radioGroupRegistrarGenero.clearCheck();

        //Calendario
        editTextRegistrarDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(RegistroUsuario.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextRegistrarDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button buttonRegister = findViewById(R.id.button_register_pet);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedGenderId = radioGroupRegistrarGenero.getCheckedRadioButtonId();
                radioButtonRegistrarGeneroSelec = findViewById(selectedGenderId);

                String textFullName = editTextRegisterNombreCompleto.getText().toString();
                String textEmail = editTextRegistrarEmail.getText().toString();
                String textDoB = editTextRegistrarDoB.getText().toString();
                String textMobile = editTextRegistrarTelefono.getText().toString();
                String textPwd = editTextRegistrarContra.getText().toString();
                String textGender; //No se pudo obtener ningun valor al verificar

                //Se validara el numero de telefono usando el metodo Matcher and Pattern
                String mobileRegex = "[2-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textMobile);


                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(RegistroUsuario.this, "Porfavor Ingrese su nombre completo", Toast.LENGTH_LONG).show();
                    editTextRegisterNombreCompleto.setError("Se requiere un Nombre Completo");
                    editTextRegisterNombreCompleto.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegistroUsuario.this, "Porfavor Ingrese su email", Toast.LENGTH_LONG).show();
                    editTextRegistrarEmail.setError("Se requiere un Email");
                    editTextRegistrarEmail.requestFocus();
                } else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(RegistroUsuario.this, "Porfavor Ingrese su fecha de nacimiento", Toast.LENGTH_LONG).show();
                    editTextRegistrarDoB.setError("Se requiere una fecha de nacimiento");
                    editTextRegistrarDoB.requestFocus();
                } else if (radioGroupRegistrarGenero.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegistroUsuario.this, "Porfavor seleccione un genero", Toast.LENGTH_SHORT).show();
                    radioButtonRegistrarGeneroSelec.setError("El genero es requerido");
                    radioButtonRegistrarGeneroSelec.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegistroUsuario.this, "Porfavor Ingrese su numero de telefono", Toast.LENGTH_LONG).show();
                    editTextRegistrarTelefono.setError("Se requiere un numero de telefono");
                    editTextRegistrarTelefono.requestFocus();
                } else if (textMobile.length() != 10) {
                    Toast.makeText(RegistroUsuario.this, "Porfavor reingrese su numero de telefono", Toast.LENGTH_LONG).show();
                    editTextRegistrarTelefono.setError("Se requiere un numero de telefono de 10 digitos");
                    editTextRegistrarTelefono.requestFocus();
                } else if (!mobileMatcher.find()) {
                    Toast.makeText(RegistroUsuario.this, "Porfavor reingrese su numero de telefono", Toast.LENGTH_LONG).show();
                    editTextRegistrarTelefono.setError("Numero de telefono no valido");
                    editTextRegistrarTelefono.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegistroUsuario.this, "Porfavor Ingrese su contraseña", Toast.LENGTH_LONG).show();
                    editTextRegistrarContra.setError("Se requiere una contraseña");
                    editTextRegistrarContra.requestFocus();
                } else if (textPwd.length() < 6) {
                    Toast.makeText(RegistroUsuario.this, "La contraseña debe de ser de al menos 6 digitos", Toast.LENGTH_LONG).show();
                    editTextRegistrarContra.setError("Contraseña muy debil");
                    editTextRegistrarContra.requestFocus();
                } else {
                    textGender = radioButtonRegistrarGeneroSelec.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textDoB, textGender, textMobile, textPwd);
                }
            }
        });

    }
    //Reg
    private void registerUser(String textFullName, String textEmail, String textDoB,String textGender, String textMobile, String textPwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegistroUsuario.this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegistroUsuario.this, "Se registro el usuario con exito", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    //Se ingresan los datos del usuario en la base de datos de Firebase.
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textDoB, textGender, textMobile);

                    //Se extraen la referencia del usuario de la base de datos de usuarios registrados
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                //Enviar correo de verificacion
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(RegistroUsuario.this, "Se registro el usuario con exito. Verifique su correo", Toast.LENGTH_LONG).show();

                                //Abrir el perfil del usuario despues de registro
                                Intent intent = new Intent(RegistroUsuario.this, UserProfileActivity.class);
                                //Se usa para prevenir que el usuario vuelva a la actividad de registro despues de que se registra
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {

                                Toast.makeText(RegistroUsuario.this, "Se fallo al registrar el usuario. Por favor vuelva a intentarlo", Toast.LENGTH_SHORT).show();

                            }
                            //Se esconde la barra de progreso independientemente si el usuario o no se termino de registrar
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        editTextRegistrarContra.setError("Su contraseña es muy debil, porfavor use combinaciones de letras y simbolos");
                        editTextRegistrarContra.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        editTextRegistrarContra.setError("Su correo es invalido o ya esta en uso. Intentelo de nuevo");
                        editTextRegistrarContra.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e){
                        editTextRegistrarContra.setError("El usuario ya fue registrado con este correo. Use otro correo");
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegistroUsuario.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
