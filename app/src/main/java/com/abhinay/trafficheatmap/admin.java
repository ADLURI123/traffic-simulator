package com.abhinay.trafficheatmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class admin extends AppCompatActivity {

    private Button addvid,viewvid,addloc,reset;
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setTitle("Traffic Simulator Admin");

        addvid = findViewById(R.id.btnaddvideo);
        viewvid = findViewById(R.id.btnviewvideo);
        addloc = findViewById(R.id.btnaddloc);
        reset = findViewById(R.id.btnresetloc);

        addloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddLocation.class);
                startActivity(intent);
            }
        });
        addvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), videoupload.class);
                startActivity(intent);
            }
        });
        viewvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewVideo.class);
                startActivity(intent);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler = new DBHandler(admin.this);
                dbHandler.reset();
            }
        });
    }
}