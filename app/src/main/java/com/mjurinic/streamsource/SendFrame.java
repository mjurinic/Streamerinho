package com.mjurinic.streamsource;

import android.app.ProgressDialog;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class SendFrame extends AsyncTask<String, String, String> {

    DatagramSocket serverSocket;
    static InetAddress clientIP;
    static int clientPort;
    static byte[] sendData;
    static byte[] receiveData = new byte[1024];

    SendFrame(byte[] _data, Camera _camera) {
        Camera.Size previewSize = _camera.getParameters().getPreviewSize();
        YuvImage yuvImage = new YuvImage(_data, ImageFormat.NV21, previewSize.width, previewSize.height, null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 45, out);

        byte[] jpegData = out.toByteArray();

        sendData = jpegData;
    }

    public static void setClientPort(int port) {
        clientPort = port;
    }

    public static void setClientIP(InetAddress IP) {
        try {
            clientIP = IP;
        }
        catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }

    protected void onPreExecute() {
        try {
            serverSocket = new DatagramSocket();
        } catch (Exception e) {
            Log.d("Exception: ", e.toString());
        }
    }

    protected String doInBackground(String... args) {
        try {
            byte[] finalData;
            finalData = Arrays.copyOf(sendData, sendData.length);

            DatagramPacket sendPacket = new DatagramPacket(finalData, finalData.length, clientIP, clientPort);
            serverSocket.send(sendPacket);
        }
        catch (Exception e) {
            Log.d("Exception: ", e.toString());
        }

        return null;
    }
}
