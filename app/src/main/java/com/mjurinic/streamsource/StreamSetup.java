package com.mjurinic.streamsource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StreamSetup extends Activity {

    boolean vrti = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_setup);

        new Prepare().execute();
    }

    public class Prepare extends AsyncTask<Void, Void, Boolean> {

        DatagramSocket serverSocket, clientSocket;
        InetAddress clientIP;
        int clientPort;

        byte[] sendData;
        byte[] receiveData = new byte[1024];
        private ProgressDialog pDialog;

        protected void onPreExecute() {
            try {
                serverSocket = new DatagramSocket(8080);
                clientSocket = new DatagramSocket();

                Log.d("DEBUG", "Created socket 8080");

                pDialog = new ProgressDialog(StreamSetup.this);
                pDialog.setMessage("Waiting for connections...");
                pDialog.setCancelable(true);
                pDialog.show();

            } catch (Exception e) {
                Log.d("Exception: ", e.toString());
            }
        }

        protected Boolean doInBackground(Void... args) {
            try {
                while (vrti) {
                    Log.d("DEBUG", "Vrtim se");

                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    String msg = new String(receivePacket.getData());
                    msg = msg.substring(0, 13);

                    Log.d("DEBUG", msg);

                    String respo = "Connection OK";

                    if (msg.equals(respo)) {
                        vrti = false;
                        Log.d("DEBUG", "Primil sam poruku");

                        clientIP = receivePacket.getAddress();
                        clientPort = receivePacket.getPort();

                        byte[] answer = respo.getBytes();

                        DatagramPacket sendPacket = new DatagramPacket(answer, answer.length, clientIP, clientPort);

                        Log.d("DEBUG", clientIP + " " + clientPort);
                        clientSocket.send(sendPacket);

                        return true;
                    }
                }
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            if (result == true) {
                Log.d("DEBUG", "PostExecute");

                SendFrame.setClientIP(clientIP);
                SendFrame.setClientPort(clientPort);

                pDialog.dismiss();
                startActivity(new Intent(StreamSetup.this, StartStream.class));
            }
        }
    }

}
