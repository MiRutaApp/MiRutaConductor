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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Christian on 5/14/2016.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";

    @BindView(R.id.email)
    AutoCompleteTextView mEmailTextView;
    @BindView(R.id.password)
    EditText mPasswordEditText;
    @BindView(R.id.email_sign_in_button)
    Button mEmailSingInButton;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate");
       // Button mEmailSingInButton = (Button)findViewById(R.id.email_sign_in_button);

        mEmailSingInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login(){
        Log.d(TAG,"on login");
        ParseUser.logInInBackground(mEmailTextView.getText().toString()
                , mPasswordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent i = new Intent(getBaseContext(),MainActivity.class);
                            startActivity(i);
                        } else {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
    }
}
