package com.abhinay.trafficheatmap;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private DBHandler dbHandler;
    protected LatLng start = null;
    protected LatLng end = null;
    private List<Polyline> polylines = null;
    public int status=0;

    LatLng delhi = new LatLng(28.644800, 77.216721);

    private Button hybridMapBtn, terrainMapBtn, satelliteMapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Traffic Viewer");

        hybridMapBtn = findViewById(R.id.idBtnHybridMap);
        terrainMapBtn = findViewById(R.id.idBtnTerrainMap);
        satelliteMapBtn = findViewById(R.id.idBtnSatelliteMap);
        dbHandler = new DBHandler(MainActivity.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
        addHeatMap();
    }
    private void addHeatMap()
    {
        ArrayList <location> list = dbHandler.readLocations();

        for (int i=0;i<list.size();i++)
        {
            start = new LatLng(list.get(i).getStartlatitude(),list.get(i).getStartlongitude());
            end =  new LatLng(list.get(i).getEndlatitude(),list.get(i).getEndlongitude());
            if(list.get(i).getIndex()<=0.50)
            {
                mMap.addPolyline((new PolylineOptions()).add(start,end).width(5).color(GREEN).geodesic(true));
                status = 0;
            }
            else if(list.get(i).getIndex()<=1)
            {
                mMap.addPolyline((new PolylineOptions()).add(start,end).width(5).color(Color.BLUE).geodesic(true));
                status = 1;
            }
            else
            {
                mMap.addPolyline((new PolylineOptions()).add(start,end).width(5).color(Color.RED).geodesic(true));
                status = 2;
            }
            //Findroutes(start,end);
        }
    }
    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(MainActivity.this,"Unable to get location",Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyDuo1XxvB4Z7id_W7NmC84N5hDui74iv8Q")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(MainActivity.this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.green));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline=mMap.addPolyline(polyOptions);
                if(status==0)
                {
                    mMap.addPolyline(polyOptions).setColor(GREEN);
                }
                else if(status==1)
                {
                    mMap.addPolyline(polyOptions).setColor(BLUE);
                }
                else if(status==2)
                {
                    mMap.addPolyline(polyOptions).setColor(RED);
                }
                int k=polyline.getPoints().size();
                polylineStartLatLng=polyline.getPoints().get(0);
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);
            }
        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start,end);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
