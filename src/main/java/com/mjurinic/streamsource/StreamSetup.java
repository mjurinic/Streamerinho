package com.mjurinic.streamsource;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mjurinic.streamsource.SendFrame;

public class StreamSetup extends ActionBarActivity {

    public EditText _etIPAddress;
    public String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_setup);

       Button btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _etIPAddress = (EditText) findViewById(R.id.etIPAddress);
                input = _etIPAddress.getText().toString();

                if (!input.equals("")) {
                    SendFrame.setIP(input);

                    if (!SendFrame.getIP().equals("")) {
                        Intent i = new Intent(getApplicationContext(), StartStream.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Failed to asign IP address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "All fields are requied!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
