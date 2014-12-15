package com.mjurinic.streamerinho;

import android.os.AsyncTask;
import android.util.Log;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendMSG extends AsyncTask<String, String, String> {

    DatagramSocket clientSocket;
    InetAddress serverIP;
    int serverPort, _size;
    byte[] _data;

    SendMSG(byte[] data, int size) {
        _data = data;
        _size = size;
    }

    protected void onPreExecute() {
        try {
            clientSocket = new DatagramSocket();
            //serverIP = InetAddress.getByName("10.0.2.2");
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
            byte[] sendData = new byte[64];

            String bit = "";
            int poss = 0;
            for (int i = 0; i < _data.length; ++i) {
                //Log.d("Bit possition:", " " + i);
                if (bit.length() == 8) {
                    sendData[poss++] = (byte) Integer.parseInt(bit, 2);
                    bit = "";
                }

                if (poss == 64 || (i + 1) == _data.length) {
                    //Log.d("PACKET #", Integer.toString(k) + " . " + Integer.toString(sendData.length));
                    poss = 0;
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
                    clientSocket.send(sendPacket);
                }

                for (int mask = 0x001; mask != 0x100; mask <<= 1) {
                    if ((_data[i] & mask) != 0) bit = bit.concat("1");
                    else bit = bit.concat("0");
                }
            }
        }
        catch (Exception e) {
            Log.d("Exception: ", e.toString());
        }

        return null;
    }
}

