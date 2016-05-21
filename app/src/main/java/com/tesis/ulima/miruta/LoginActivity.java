package com.tesis.ulima.miruta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Christian on 5/14/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.email)
    AutoCompleteTextView mEmailTextView;
    @BindView(R.id.password)
    EditText mPasswordEditText;
    @BindView(R.id.email_sign_in_button)
    Button mEmailSingInButton;
    /*{lat:-12.092187, lon:-77.032341},
    {lat:-12.091956, lon:-77.030807},
    {lat:-12.090139, lon:-77.017281},
    {lat:-12.088314, lon:-77.003784},
    {lat:-12.086792, lon:-76.992843},
    {lat:-12.085358, lon:-76.985688},
    {lat:-12.084697, lon:-76.978391},
    {lat:-12.083791, lon:-76.972038}*/

    List<ParseGeoPoint> points = Arrays.asList(
            new ParseGeoPoint(-12.092187,-77.032341),
            new ParseGeoPoint(-12.091956,-77.030807),
            new ParseGeoPoint(-12.090139,-77.017281),
            new ParseGeoPoint(-12.088314,-77.003784),
            new ParseGeoPoint(-12.086792,-76.992843),
            new ParseGeoPoint(-12.085358,-76.985688),
            new ParseGeoPoint(-12.084697,-76.978391),
            new ParseGeoPoint(-12.083791,-76.972038));




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate");
        //Intent i = new Intent(getBaseContext(), MapsActivity.class);
       // startActivity(i);
        mEmailSingInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        ParseQuery<ParseObject> unidadQuery = ParseQuery.getQuery("Unidad");
        unidadQuery.whereEqualTo("chofer", ParseUser.getCurrentUser());
        unidadQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.d(TAG, "doneUnidad");
                if(e==null){
                    ParseQuery<ParseObject> rutaQuery = ParseQuery.getQuery("Ruta");
                    rutaQuery.getInBackground(objects.get(0).getParseObject("ruta").getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if(e==null){
                                for(ParseGeoPoint parseGeoPoint : points){
                                    object.put("camino",parseGeoPoint);
                                }
                                object.saveInBackground();
                                Log.d(TAG, object.get("nombre").toString());
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

    public void login() {
        Log.d(TAG, "on login");
        ParseUser.logInInBackground("conductor"
                , "123", new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        Log.d(TAG, "doneUser");
                        if (user != null) {
                            /*ParseQuery<ParseObject> empresaQuery = ParseQuery.getQuery("Empresa");
                            empresaQuery.getInBackground(user.getParseObject("empresa").getObjectId(), new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    Log.d(TAG, "doneEmpresa");
                                    if (object != null) {
                                        Log.d(TAG, object.get("rutas").toString());
                                        object.getRelation("rutas").getQuery().findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                Log.d(TAG, "doneRutas");
                                                if (objects != null) {
                                                    Log.d(TAG, objects.isEmpty() + " ");
                                                } else {
                                                    Log.d(TAG, "objects null");
                                                }
                                            }
                                        });
                                    } else {
                                        Log.e(TAG, e.toString());
                                    }
                                }
                            });*/

                            //Intent i = new Intent(getBaseContext(),MapsActivity.class);
                            //startActivity(i);
                        } else {
                            Log.e(TAG, e.toString());
                        }
                    }
                });


    }
}
