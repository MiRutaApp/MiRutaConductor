package com.tesis.ulima.mirutaconductor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.tesis.ulima.mirutaconductor.Utils.DialogFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Christian on 5/14/2016.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";

    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
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

    LoginActivity mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate");
        mContext = LoginActivity.this;
        if(ParseUser.getCurrentUser()!=null) {
            Intent i = new Intent(getBaseContext(), MapsActivity.class);
            startActivity(i);
        }
        mEmailSingInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        Log.d(TAG, "on login");
        ParseUser.logInInBackground(mEmailTextView.getText().toString()
                , mPasswordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        Log.d(TAG, "doneUser");
                        if (e == null) {
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

                            Intent i = new Intent(getBaseContext(),MapsActivity.class);
                            startActivity(i);
                        } else {
                            DialogFactory.showErrorSnackBar(mContext,mainLinear,e).show();

                            Log.e(TAG, e.toString());
                        }
                    }
                });


    }


}
