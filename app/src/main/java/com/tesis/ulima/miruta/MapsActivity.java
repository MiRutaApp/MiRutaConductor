package com.tesis.ulima.miruta;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ParseGeoPoint geolocation;
    private LatLng latLng;
    private Marker marker;
    List<ParseGeoPoint> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //noinspection MissingPermission
        //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
        ParseQuery<ParseObject> unidadQuery = ParseQuery.getQuery("Unidad");
        unidadQuery.whereEqualTo("chofer", ParseUser.getCurrentUser());
        unidadQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.d(TAG, "doneUnidad");
                if(e==null){
                    ParseQuery<ParseObject> rutaQuery = ParseQuery.getQuery("Ruta");
                    //Un chofer puede estar asignado a varias unidades?
                    rutaQuery.getInBackground(objects.get(0).getParseObject("ruta").getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(final ParseObject object, ParseException e) {
                            if(e==null){
                                ParseQuery<ParseObject> rutasQuery = ParseQuery.getQuery("Ruta");
                                rutasQuery.whereEqualTo("nombre", object.get("nombre"));
                                rutasQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if(e==null){
                                            points= (ArrayList<ParseGeoPoint>)objects.get(0).get("camino");
                                            populateMap(points);
                                        }else {
                                            Log.e(TAG,e.toString());
                                        }

                                    }
                                });

                            }else {
                                Log.e(TAG, e.toString());
                            }
                        }
                    });
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });


    }

    private void populateMap(List<ParseGeoPoint> points) {
        PolylineOptions polylineOptions = new PolylineOptions().geodesic(true);

        for(ParseGeoPoint parseGeoPoint : points){
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng;
            latLng = new LatLng(parseGeoPoint.getLatitude(),parseGeoPoint.getLongitude());
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp));
            polylineOptions.color(ContextCompat.getColor(this,R.color.polyline));
            polylineOptions.add(latLng);
            mMap.addMarker(markerOptions);
        }
        mMap.addPolyline(polylineOptions);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //noinspection MissingPermission
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.moveCamera(cameraUpdate);


        //noinspection MissingPermission
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                if (marker!=null){
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.orange)));
                //mMap.animateCamera(cameraUpdate);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ParseUser.logOut();
    }
}
