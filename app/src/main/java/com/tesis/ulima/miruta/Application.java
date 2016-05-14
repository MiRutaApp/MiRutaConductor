package com.tesis.ulima.miruta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.parse.Parse;

/**
 * Created by Christian on 5/14/2016.
 */
public class Application extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("449e9382040418fff7bd75dfae5c6a7260abbc69")
                .server("http://miruta.frikicorp.com/parse")

        .build()
        );
        startActivity(new Intent(this,LoginActivity.class));
    }
}
