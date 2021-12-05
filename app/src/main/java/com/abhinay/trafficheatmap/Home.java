package com.abhinay.trafficheatmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Button login,viewheatmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Traffic Simulator");

        viewheatmap = findViewById(R.id.btnviewheatmap);
        login = findViewById(R.id.btnlogin);

        viewheatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), trafficheatmap.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });
    }
}