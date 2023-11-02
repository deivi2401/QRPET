package com.example.dogapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//import com.example.dogapp.databinding.InicioSesionBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressbar;
    private FirebaseAuth authProfile;
    private static final String TAG = "LoginActivity_Main";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressbar = findViewById(R.id.progressBar);
        
        authProfile = FirebaseAuth.getInstance();
        TextView textView = findViewById(R.id.textView_register_link);

        String text = "Registrese";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(MainActivity.this, RegistroUsuario.class);
                startActivity(intent);
            }
        };

        ss.setSpan(clickableSpan,0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        //Mostrar y esconder la contraseña
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                } else {
                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });
        
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

                    FirebaseUser firebaseUser = authProfile.getCurrentUser();

                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(MainActivity.this, "Ha iniciado sesion", Toast.LENGTH_SHORT).show();
                        //Abrir el Menu Principal
                        Intent intent = new Intent(MainActivity.this, PantallaInicio.class);
                        startActivity(intent);
                    } else {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut(); //Se cierra la sesion del usuario
                        showAlertDialog();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthInvalidUserException e){
                        editTextLoginEmail.setError("El usuario no existe o es invalido. Porfavor registrese nuevamente");
                        editTextLoginEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        editTextLoginEmail.setError("Credenciales invalidas. Revise sus datos e intentelo de nuevo");
                        editTextLoginEmail.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(MainActivity.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                    }
                }
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("El correo no esta verificado");
        builder.setMessage("Porfavor Verifique su correo electronico, de lo contrario no podra iniciar sesion");

        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    //Verificar si el usuario ya esta logeado. Si ya lo esta llevarlo al menu principal
    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() != null){
            Toast.makeText(MainActivity.this, "Ya esta logeado!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(MainActivity.this, PantallaInicio.class));
            finish();
        } else {
            Toast.makeText(MainActivity.this, "Puede logearse", Toast.LENGTH_SHORT).show();
        }
    }
}