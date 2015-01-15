package com.mjurinic.streamsource;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Main extends ActionBarActivity {

    ImageView imgStream;
    ImageView imgWatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String myIP = getIPAddress() + ":8080";
        String generatedCode = Base64.getBase64(myIP);

        final AlertDialog.Builder Builder1 = new AlertDialog.Builder(this);
            Builder1.setMessage("Kod za povezivanje:\n\n" + generatedCode);
            Builder1.setCancelable(true);
            Builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
;
                    Intent i = new Intent(getApplicationContext(), StreamSetup.class);
                    startActivity(i);
                }
            });

        imgStream = (ImageView) findViewById(R.id.btnStream);
        imgStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert1 = Builder1.create();
                alert1.show();
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

    private static String getIPAddress() {
        boolean useIPv4 = true;

        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions

        return "";
    }
}
