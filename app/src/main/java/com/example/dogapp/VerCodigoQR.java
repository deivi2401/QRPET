package com.example.dogapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class VerCodigoQR extends AppCompatActivity {

    private ImageView CodigoQR;
    private Button Regresar;
    private FirebaseAuth authProfile;
    private DatabaseReference mRef;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase mDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        CodigoQR = findViewById(R.id.ImagendelQR);
        Regresar = findViewById(R.id.boton_regresardelQR);
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        Bundle bundle = getIntent().getExtras();

        mRef = mDatabase.getReference("Registered Users").child(firebaseUser.getUid()).child("Mascotas").child(bundle.getString("Nombre"));
        SetImage(mRef, bundle);

        Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerCodigoQR.this, Lista_Mascotas.class);
                startActivity(intent);

            }
        });
        
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
