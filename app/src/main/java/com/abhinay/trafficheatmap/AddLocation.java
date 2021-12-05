package com.abhinay.trafficheatmap;

import android.content.Intent;
import android.content.RestrictionEntry;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class AddLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText name,slat,slon,elat,elon,index,capacity;
    private DBHandler dbHandler;
    private Button addlocation;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        getSupportActionBar().setTitle("Add new Link");

        searchView = findViewById(R.id.idSearchView);
        name = findViewById(R.id.idLocation);
        slat = findViewById(R.id.idstartLatitude);
        slon = findViewById(R.id.idstartLongitude);
        elat = findViewById(R.id.idendLatitude);
        elon = findViewById(R.id.idendLongitude);
        index = findViewById(R.id.idIndex);
        capacity = findViewById(R.id.idcapacity);
        dbHandler = new DBHandler(AddLocation.this);
        addlocation = findViewById(R.id.idAddLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()|| elat.getText().toString().isEmpty() || elon.getText().toString().isEmpty() ||slat.getText().toString().isEmpty() || slon.getText().toString().isEmpty()|| index.getText().toString().isEmpty()||capacity.getText().toString().isEmpty()) {
                    Toast.makeText(AddLocation.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }
                String linkname = name.getText().toString();
                Double startlatitude = Double.parseDouble(slat.getText().toString());
                Double startlongitude = Double.parseDouble(slon.getText().toString());
                Double endlatitude = Double.parseDouble(elat.getText().toString());
                Double endlongitude = Double.parseDouble(elon.getText().toString());
                Double traffic_index = Double.parseDouble(index.getText().toString());
                Double link_capacity = Double.parseDouble(capacity.getText().toString());
                dbHandler.addNewCourse(linkname,startlatitude,startlongitude,endlatitude,endlongitude,link_capacity,traffic_index);
                Toast.makeText(AddLocation.this, "Location has been added.", Toast.LENGTH_SHORT).show();
                name.setText("");
                slat.setText("");
                slon.setText("");
                elat.setText("");
                elon.setText("");
                capacity.setText("");
                index.setText("");
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(AddLocation.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    name.setText(location);
                    slat.setText(String.valueOf(address.getLatitude()));
                    slon.setText(String.valueOf(address.getLongitude()));

                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
