package com.test.buspasssystem;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register extends AppCompatActivity {

    ImageView ivProfile;
    Button sup;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    EditText na,em,pas,ca,ba,fs,ts,mn,cl,ad;
    private static int PICK_IMAGE=123;
    String currentPhotoPath;
    Uri imagepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        na=findViewById(R.id.namee);
        ca=findViewById(R.id.cname);
        ba=findViewById(R.id.birth);
        fs=findViewById(R.id.fromstop);
        ts=findViewById(R.id.tostop);
        mn=findViewById(R.id.mnumber);
        cl=findViewById(R.id.classs);
        ad=findViewById(R.id.taddress);
        em=findViewById(R.id.temail);
        pas=findViewById(R.id.tpassword);
        sup=findViewById(R.id.btnRegister);
        ivProfile=findViewById(R.id.iamge);
        firebaseAuth=FirebaseAuth.getInstance();
//        Log.i("err1","getUID");
//        Log.i("err1","no:--> "+firebaseAuth.getUid());
//        Log.i("err1","GotUID");
        //Log.i("err2", FirebaseApp.getInstance().getOptions().getDatabaseUrl());
        firebaseDatabase=FirebaseDatabase.getInstance();
       // Log.i("err3", FirebaseApp.getInstance().getOptions().getDatabaseUrl());
        databaseReference=firebaseDatabase.getReference("student");
       // Log.i("err4","3");
        //DatabaseRefernce mdatabaseref=FirebaseDatabase.getInstance().getReferenceFromUrl(https://buspasssystem-902a7-default-rtdb.firebaseio.com/)
        firebaseStorage=FirebaseStorage.getInstance();
        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String nam=na.getText().toString().trim();
               final String col=ca.getText().toString().trim();
               final String bir=ba.getText().toString().trim();
               final String fos=fs.getText().toString().trim();
               final String tos=ts.getText().toString().trim();
               final String mon=mn.getText().toString().trim();
               final String cls=cl.getText().toString().trim();
               final String ads=ad.getText().toString().trim();
                String eme=em.getText().toString().trim();
                String psaa=pas.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(eme,psaa).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            String ke=firebaseAuth.getUid().toString();
                            storageReference=firebaseStorage.getReference().child("profile").child(firebaseAuth.getUid());
                            UploadTask uploadTask=storageReference.putFile(imagepath);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Image Upload  Fiels",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(),"Image Uploaded ",Toast.LENGTH_SHORT).show();
                                }
                            });
                            String st=" ";
                            userProfile us = new userProfile(nam,col,bir,fos,tos,mon,cls,ads,st,ke);
                            databaseReference.child(firebaseAuth.getUid()).setValue(us);
                            Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           dispatchTakePictureIntent();

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK )
        {

            File f =new File(currentPhotoPath);
            ivProfile.setImageURI(Uri.fromFile(f));
           imagepath=Uri.fromFile(f);
//            Bitmap bitmap= null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
//                ivProfile.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Bitmap image =(Bitmap) data.getExtras().get("data");
//            ivProfile.setImageBitmap(image);


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
      File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Log.i("chal gya","function");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
      //  if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.i("pic",photoFile.toString());
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
           // if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.test.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PICK_IMAGE);
            //}
        //}
    }
}
