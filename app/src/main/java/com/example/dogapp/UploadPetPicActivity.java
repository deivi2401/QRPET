package com.example.dogapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadPetPicActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imageViewUploadPic;
    private FirebaseAuth authProfile;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;
    private Button buttonUploadPicChoose;
    private Button buttonUploadPic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pet_pic);

        buttonUploadPicChoose = findViewById(R.id.upload_petpic_choose_button);
        buttonUploadPic = findViewById(R.id.upload_petpic_button);
        progressBar = findViewById(R.id.progressBar);
        imageViewUploadPic = findViewById(R.id.imageView_petprofile_dp_upload);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();


        firebaseStorage = FirebaseStorage.getInstance();

        Uri uri = firebaseUser.getPhotoUrl();

        //Set User's current DP in ImageView (if uploaded already). We will Picasso since imageViewer wont work
        //Regular URIs.
        //Picasso.with(UploadPetPicActivity.this).load(uri).into(imageViewUploadPic);

        buttonUploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //Upload Image
        buttonUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });

    }

    private void UploadPic() {
        if (uriImage != null) {
            String userID = firebaseUser.getUid();
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
            referenceProfile.child(userID).child("Mascotas").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWritePetDetails readPetDetails = snapshot.getValue(ReadWritePetDetails.class);
                    Bundle bundle = getIntent().getExtras();
                    mRef = mDatabase.getReference("Registered Users").child(firebaseUser.getUid()).child("Mascotas").child(bundle.getString("Nombre"));
                    //Guardar la imagen con el uid del usuario actual
                    StorageReference fileReference = firebaseStorage.getReference().child("Imagenes de Mascotas").child(userID + firebaseStorage.getReference("Registered Users").child(firebaseUser.getUid()).child("Mascotas").child(bundle.getString("Nombre")) + "." + getFileExtension(uriImage));

                    //Se sube la imagen a la base de datos
                    fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String t = task.getResult().toString();

                                    DatabaseReference update = mRef.child("image");

                                    update.setValue(t);

                                }
                            });


                        }

                    });



                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadPetPicActivity.this, "Se ha subido la foto con exito", Toast.LENGTH_SHORT).show();

                    Intent intent = getIntent();
                    String checkFlag = intent.getStringExtra("flag");

                    Intent intent1;
                    if (checkFlag.equals("A")) {
                        intent1 = new Intent(UploadPetPicActivity.this, RegistroMascota.class);
                    } else {
                        intent1 = new Intent(UploadPetPicActivity.this, Lista_Mascotas.class);
                    }
                    startActivity(intent1);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriImage = data.getData();
            imageViewUploadPic.setImageURI(uriImage);
        }
    }


    }

