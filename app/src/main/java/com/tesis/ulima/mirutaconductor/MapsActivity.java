package com.tesis.ulima.mirutaconductor;

import android.content.Context;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tesis.ulima.mirutaconductor.Utils.Utils;
import com.tesis.ulima.mirutaconductor.prod.BusesConductorApiClient;
import com.tesis.ulima.mirutaconductor.prod.Injection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, BusesPositionContract.View {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ParseGeoPoint geolocation;
    private LatLng latLng;
    private Marker marker;
    List<ParseGeoPoint> points = new ArrayList<>();

    BusesPositionContract.UserActionListener mActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mActionListener == null) {
            mActionListener = new BusesPositionPresenter(this, Injection.provideBusesPositionRepository());
        }
        mActionListener.requestBusesPositon();


        //noinspection MissingPermission -12.086340,-76.990059
        //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

        ParseQuery<ParseObject> unidadQuery = ParseQuery.getQuery("Unidad");
        unidadQuery.whereEqualTo("chofer", ParseUser.getCurrentUser());
        unidadQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.d(TAG, "doneUnidad");
                if (e == null) {
                    ParseQuery<ParseObject> rutaQuery = ParseQuery.getQuery("Ruta");
                    //Un chofer puede estar asignado a varias unidades?
                    rutaQuery.getInBackground(objects.get(0).getParseObject("ruta").getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(final ParseObject object, ParseException e) {
                            if (e == null) {
                                ParseQuery<ParseObject> rutasQuery = ParseQuery.getQuery("Ruta");
                                rutasQuery.whereEqualTo("nombre", object.get("nombre"));
                                rutasQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            points = (ArrayList<ParseGeoPoint>) objects.get(0).get("camino");
                                            fetchSnapRoad(points);
                                            //populateMap(points);
                                        } else {
                                            Log.e(TAG, e.toString());
                                        }

                                    }
                                });

                            } else {
                                Log.e(TAG, e.toString());
                            }
                        }
                    });
                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });


    }

    private void populateMap(List<LatLng> points) {
        Log.d("populateMap","making before call");

        PolylineOptions polylineOptions = new PolylineOptions().geodesic(true);

        for (LatLng latLng: points) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop));
            polylineOptions.color(ContextCompat.getColor(this, R.color.polyline)).width(15);
            polylineOptions.add(latLng);
            mMap.addMarker(markerOptions);
        }
        mMap.addPolyline(polylineOptions);

    }
    public void fetchSnapRoad(List<ParseGeoPoint> points){
        Log.d("fetchSnapRoad","Making the call");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://roads.googleapis.com/v1/snapToRoads?path=" +
                points.get(0).getLatitude()+","+points.get(0).getLongitude()+"|"+
                points.get(1).getLatitude()+","+points.get(1).getLongitude()+"|"+
                points.get(2).getLatitude()+","+points.get(2).getLongitude()+"|"+
                points.get(3).getLatitude()+","+points.get(3).getLongitude()+"|"+
                points.get(4).getLatitude()+","+points.get(4).getLongitude()+"|"+
                points.get(5).getLatitude()+","+points.get(5).getLongitude()+"|"+
                points.get(6).getLatitude()+","+points.get(6).getLongitude()+"|"+
                points.get(7).getLatitude()+","+points.get(7).getLongitude()+
                "&interpolate=true&key="+ "AIzaSyAyS7fcTfZEdP24kLe4vrvtRpXkl-dHkDI";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Utils.list=new ArrayList<>();
                            Log.d("volley call",response.getJSONArray("snappedPoints").getJSONObject(0).getJSONObject("location").get("latitude").toString());
                            for(int i=0; i<response.getJSONArray("snappedPoints").length();i++){
                                JSONObject jsonObject= response.getJSONArray("snappedPoints").getJSONObject(i);
                                Utils.list.add(new LatLng(jsonObject.getJSONObject("location").getDouble("latitude"),jsonObject.getJSONObject("location").getDouble("longitude")));

                            }
                            populateMap(Utils.list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                });
        requestQueue.add(jsObjRequest);
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
        Criteria criteria = new Criteria();
        //noinspection MissingPermission
        mMap.setMyLocationEnabled(true);
        //noinspection MissingPermission

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mMap.moveCamera(cameraUpdate);


        //noinspection MissingPermission
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                /*latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                if (marker!=null){
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.self)));
                animateMarker(marker,latLng,false);
                mMap.animateCamera(cameraUpdate);*/
                Toast.makeText(MapsActivity.this, "Changed", Toast.LENGTH_SHORT).show();
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

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ParseUser.logOut();
    }

    @Override
    public void showPositions(List<ParseObject> positions) {
        for (ParseObject parseObject : positions) {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng;
            latLng = new LatLng(((ParseGeoPoint) parseObject.get("posicion")).getLatitude()
                    , ((ParseGeoPoint) parseObject.get("posicion")).getLongitude());
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
            mMap.addMarker(markerOptions);
        }
    }



    @Override
    public void showRequestError() {
        Log.d("showRequestError", "oops, something happened");
    }
}
