package com.test.buspasssystem;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Student extends AppCompatActivity {
    ImageView all,app,log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);
        all=findViewById(R.id.addsite);
        app=findViewById(R.id.addworker);
        log=findViewById(R.id.logout);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(Student.this,DisplayAll.class));
                Toast.makeText(getApplicationContext(),"Student Cant Perform This activity",Toast.LENGTH_SHORT).show();
            }
        });
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Student.this,PassDisplay.class));
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(Student.this,Login.class));
                finish();
            }
        });
    }
}
