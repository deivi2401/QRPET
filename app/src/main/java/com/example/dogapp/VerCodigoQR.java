package com.example.dogapp;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.security.Permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.OutputStream;
import java.util.Objects;

public class VerCodigoQR extends AppCompatActivity {

    private ImageView CodigoQR;
    private Button Regresar,ExportarQR;
    private FirebaseAuth authProfile;
    private DatabaseReference mRef;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase mDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        ActivityCompat.requestPermissions(this, new String[]{READ_MEDIA_IMAGES,WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);


        CodigoQR = findViewById(R.id.ImagendelQR);
        Regresar = findViewById(R.id.boton_regresardelQR);
        ExportarQR = findViewById(R.id.boton_exportarqr);
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        Bundle bundle = getIntent().getExtras();

        mRef = mDatabase.getReference("Registered Users").child(firebaseUser.getUid()).child("Mascotas").child(bundle.getString("Nombre"));
        SetImage(mRef, bundle);

        ExportarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    saveImage();

                }

        });
        Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerCodigoQR.this, Lista_Mascotas.class);
                startActivity(intent);

            }
        });
        
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){
            if (grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                saveImage();
            } else {
                Toast.makeText(this, "Porfavor otorgue los permisos necesarios", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    private void saveImage() {

        Uri images;
        ContentResolver contentResolver = getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else{
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*");
        Uri uri = contentResolver.insert(images,contentValues);

        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) CodigoQR.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,outputStream);
            Objects.requireNonNull(outputStream);

            Toast.makeText(this, "Se guardo la imagen con exito en la galeria", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "No se pudo guardar la imagen", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void SetImage(DatabaseReference mRef, Bundle bundle) {

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = (String) snapshot.child("QR").getValue();
                Picasso.with(VerCodigoQR.this).load(link).into(CodigoQR);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText( VerCodigoQR.this, "Error Loading Image", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
