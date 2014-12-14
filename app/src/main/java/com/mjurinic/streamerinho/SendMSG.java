package com.mjurinic.streamerinho;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendMSG extends AsyncTask<String, String, String> {

    DatagramSocket clientSocket;
    InetAddress serverIP;
    int serverPort, _size;
    byte[] _data;

    SendMSG(byte[] data) {
        _data = data;
    }

    SendMSG(int size) {
        _size = size;
    }

    protected void onPreExecute() {
        try {
            clientSocket = new DatagramSocket();
            serverIP = InetAddress.getByName("192.168.1.5");
            serverPort = 8080;
        }
        catch (Exception e) {
            Log.d("Exception: ", e.toString());
        }
    }

    protected String doInBackground(String... args) {
        try {
            String msg = "Message from an android device!\n";
            byte[] sendData;

            sendData = Integer.toString(_size).getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
            clientSocket.send(sendPacket);

            Log.d("Creating packet...", "Success! Message has been sent!");
        }
        catch (Exception e) {
            Log.d("Exception: ", e.toString());
        }
        finally {
            clientSocket.close();
        }

        return null;
    }

}

