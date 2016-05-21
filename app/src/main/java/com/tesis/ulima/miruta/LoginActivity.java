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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

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

    ParseUser parseUser;

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
                            ParseQuery<ParseObject> unidadQuery = ParseQuery.getQuery("Unidad");
                            unidadQuery.whereEqualTo("chofer",user.getObjectId());
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
                            //Intent i = new Intent(getBaseContext(),MapsActivity.class);
                            //startActivity(i);
                        } else {
                            Log.e(TAG, e.toString());
                        }
                    }
                });


    }
}
