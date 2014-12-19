package com.mjurinic.streamsource;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class Main extends ActionBarActivity {

    ImageView imgStream;
    ImageView imgWatch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgStream = (ImageView) findViewById(R.id.btnStream);
        imgStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StreamSetup.class);
                startActivity(i);
            }
        });

        imgWatch = (ImageView) findViewById(R.id.btnWatch);
        imgWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "TBD", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
