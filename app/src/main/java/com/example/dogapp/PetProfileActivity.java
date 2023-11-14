package com.example.dogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.nio.Buffer;

public class PetProfileActivity extends AppCompatActivity {

    private TextView NombreMascota, EdadMascota, DireccionMascota, GeneroMascota, TelefonoDueno;
    private ProgressBar progressBar;
    private Button GenerarQR;
    private ImageView imageView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth authProfile;
    private DatabaseReference mRef;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        mDatabase = FirebaseDatabase.getInstance();
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        NombreMascota = findViewById(R.id.textView_nombre_mascota_perfil);
        EdadMascota = findViewById(R.id.textView_edad_mascota_perfil);
        DireccionMascota = findViewById(R.id.textView_direccion_pet);
        GeneroMascota = findViewById(R.id.textView_show_gender_pet);
        imageView = findViewById(R.id.imageView_petprofile_dp);
        GenerarQR = findViewById(R.id.boton_generarQR);
        progressBar = findViewById(R.id.progress_bar_profile_pic);
        Bundle bundle = getIntent().getExtras();

        GenerarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap("Nombre de la mascota: " + NombreMascota.getText().toString() + "/n" + "Edad de la mascota:" + EdadMascota.getText().toString() + "/n" + "Direccion: " + DireccionMascota.getText().toString(), BarcodeFormat.QR_CODE,500,500);
                    mRef = mDatabase.getReference("Registered Users").child(firebaseUser.getUid()).child("Mascotas").child(bundle.getString("Nombre"));
                    SubirQR(bitmap, mRef);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetProfileActivity.this, UploadPetPicActivity.class);
                intent.putExtra("flag", "B");
                intent.putExtra("Nombre", NombreMascota.getText());
                intent.putExtra("Edad", EdadMascota.getText());
                intent.putExtra("Direccion", DireccionMascota.getText());
                intent.putExtra("Genero", GeneroMascota.getText());
                startActivity(intent);
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(PetProfileActivity.this, "Algo salio mal, los detalles de la mascota no estan disponibles en este momento", Toast.LENGTH_LONG).show();

        } else {
            progressBar.setVisibility(View.VISIBLE);
            showPetProfile(firebaseUser);
        }

    }

    private void SubirQR(Bitmap bitmap, DatabaseReference mRef) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,baos);
        byte[] data = baos.toByteArray();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("qr_codes/" + NombreMascota.getText().toString());
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
           imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
               Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                   @Override
                   public void onComplete(@NonNull Task<Uri> task) {
                       String t = task.getResult().toString();
                       DatabaseReference update = mRef.child("QR");
                       update.setValue(t);
                       Toast.makeText(PetProfileActivity.this, "Se genero el codigo QR con exito", Toast.LENGTH_SHORT).show();
                       GenerarQR.setVisibility(View.GONE);
                   }
               });

           });
        });
    }

    private void showPetProfile(FirebaseUser firebaseUser) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            NombreMascota.setText(bundle.getString("Nombre"));
            EdadMascota.setText(bundle.getString("Edad"));
            DireccionMascota.setText(bundle.getString("Direccion"));
            GeneroMascota.setText(bundle.getString("Genero"));
            Picasso.with(this).load(bundle.getString("Imagen")).into(imageView);
        }



    }
}
