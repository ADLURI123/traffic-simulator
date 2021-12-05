package com.abhinay.trafficheatmap;

import static android.graphics.Color.GREEN;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewvolumetrictraffic extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBHandler dbHandler;
    protected LatLng start = null;
    protected LatLng end = null;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private List<Polyline> polylines = null;
    private TextView txt;
    int res = 0;

    private Button hybridMapBtn, terrainMapBtn, satelliteMapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Traffic Viewer");

        hybridMapBtn = findViewById(R.id.idBtnHybridMap);
        terrainMapBtn = findViewById(R.id.idBtnTerrainMap);
        satelliteMapBtn = findViewById(R.id.idBtnSatelliteMap);
        dbHandler = new DBHandler(viewvolumetrictraffic.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        firebaseDatabase = FirebaseDatabase.getInstance();


        hybridMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        terrainMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });

        satelliteMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        update();
        addHeatMap();
    }
    public void update()
    {
        Intent intent = getIntent();
        String a = intent.getStringExtra("locid");
        String b = intent.getStringExtra("date");
        String c = intent.getStringExtra("time");
        databaseReference = firebaseDatabase.getReference(a+"-"+b+"-"+c+"/left count");
        getdata();
    }
    public void addHeatMap()
    {
        ArrayList <location> list = dbHandler.readLocations();
        for (int i=0;i<list.size();i++)
        {
            start = new LatLng(list.get(i).getStartlatitude(),list.get(i).getStartlongitude());
            end =  new LatLng(list.get(i).getEndlatitude(),list.get(i).getEndlongitude());
            if(list.get(i).getIndex()<=2)
            {
                mMap.addPolyline((new PolylineOptions()).add(start,end).width(5).color(GREEN).geodesic(true));
            }
            else if(list.get(i).getIndex()<=4)
            {
                mMap.addPolyline((new PolylineOptions()).add(start,end).width(5).color(Color.BLUE).geodesic(true));
            }
            else
            {
                mMap.addPolyline((new PolylineOptions()).add(start,end).width(5).color(Color.RED).geodesic(true));
            }
        }
    }

    public void getdata()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = Integer.toString(snapshot.getValue(Integer.class));
                try
                {
                    txt.setText(value);
                }
                catch(Exception e)
                {
                    Toast.makeText(viewvolumetrictraffic.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewvolumetrictraffic.this, error + "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
