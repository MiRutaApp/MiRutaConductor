package com.tesis.ulima.mirutaconductor.prod;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tesis.ulima.mirutaconductor.Utils.Utils;
import com.tesis.ulima.mirutaconductor.data.MiRutaConductorApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 5/28/2016.
 */
public class BusesConductorApiClient implements MiRutaConductorApiService {
    static BusesConductorApiClient mApiClient;
    ParseObject unidad ;

    public static BusesConductorApiClient getInstance(){
        if(mApiClient==null){
            mApiClient = new BusesConductorApiClient();
        }
        return mApiClient;
    }




    @Override
    public void fetchBusesPosition(final FetchBusesPositionApiCallback<List<ParseObject>> callback) {
        Log.d("fetchBusesPosition", "Obteniendo lista de buses");
        ParseQuery<ParseObject> unidadQuery = ParseQuery.getQuery("Unidad");
        unidadQuery.whereEqualTo("chofer", ParseUser.getCurrentUser());
        unidadQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null) {
                    Log.d("unidadQuery", "Unidad obtenida");
                    unidad=objects.get(0);
                    ParseQuery<ParseObject> unidadQuery2= ParseQuery.getQuery("Unidad");
                    unidadQuery2.whereEqualTo("ruta",objects.get(0).getParseObject("ruta"));
                    unidadQuery2.whereNotEqualTo("chofer",ParseUser.getCurrentUser());
                    unidadQuery2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e==null){
                                Log.d("unidadQuery2", "Unidades obtenida");
                                callback.onLoaded(objects);
                            }else {
                                Log.e("unidadQuery2",e.toString());
                                callback.onError(e);
                            }
                        }
                    });
                }else {
                    Log.e("unidadQuery",e.getMessage());
                    callback.onError(e);
                }
            }
        });

    }

    @Override
    public void fetchRuta(final FetchRutaApiCallBack<List<ParseObject>> callBack) {
        Log.d("fetchRuta", "Obteniendo ruta de buses");
        ParseQuery<ParseObject> rutaQuery = ParseQuery.getQuery("Ruta");
        //Un chofer puede estar asignado a varias unidades?
        rutaQuery.getInBackground(unidad.getParseObject("ruta").getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> rutasQuery = ParseQuery.getQuery("Ruta");
                    rutasQuery.whereEqualTo("nombre", object.get("nombre"));
                    rutasQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                callBack.onLoaded(objects);
                            } else {
                                Log.e("fetchRuta", e.toString());
                                callBack.onError(e);
                            }

                        }
                    });

                } else {
                    Log.e("fetchRuta", e.toString());
                }
            }
        });
    }

    public void fetchSnapRoad(Context context,List<ParseGeoPoint> points){
        Log.d("fetchSnapRoad","Making the call");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                            Log.d("volley call",response.getJSONArray("snappedPoints").getJSONObject(0).getJSONObject("location").get("latitude").toString());
                            for(int i=0; i<response.getJSONArray("snappedPoints").length();i++){
                                JSONObject jsonObject= response.getJSONArray("snappedPoints").getJSONObject(i);
                                Utils.list.add(new LatLng(jsonObject.getDouble("latitude"),jsonObject.getDouble("longitude")));
                            }
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


}
