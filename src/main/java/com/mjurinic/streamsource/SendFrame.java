package com.mjurinic.streamsource;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class SendFrame extends AsyncTask<String, String, String> {

    DatagramSocket clientSocket;
    InetAddress serverIP;
    int serverPort;
    byte[] data;

    SendFrame(byte[] _data) {
        data = _data;
    }

    protected void onPreExecute() {
        try {
            clientSocket = new DatagramSocket();
            //serverIP = InetAddress.getByName("10.0.2.2");         // virtual device
            serverIP = InetAddress.getByName("192.168.1.5");        // moj komp
            //serverIP = InetAddress.getByName("192.168.1.10");     // laptop
            serverPort = 8080;
        }
        catch (Exception e) {
            Log.d("Exception: ", e.toString());
        }
    }

    protected String doInBackground(String... args) {
        try {
            byte[] sendData;
            sendData = Arrays.copyOf(data, data.length);

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
            clientSocket.send(sendPacket);
        }
        catch (Exception e) {
            Log.d("Exception: ", e.toString());
        }

        return null;
    }
}
