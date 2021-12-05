package com.abhinay.trafficheatmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity
{
    private Button addloc,login;
    private EditText x,y;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Admin Login");

        addloc = findViewById(R.id.idAddLocation);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), admin.class);
                startActivity(intent);
            }
        });
    }
}