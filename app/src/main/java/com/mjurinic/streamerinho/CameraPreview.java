package com.mjurinic.streamerinho;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder _holder;
    private Camera _camera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        _camera = camera;

        _holder = getHolder();
        _holder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            _camera.setPreviewDisplay(holder);

            _camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    new SendMSG(data.length).execute();
                }
            });

            _camera.startPreview();
        } catch (Exception e) {
            Log.d("KVE", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (_holder.getSurface() == null) {
            return;
        }

        try {
            _camera.stopPreview();
        }
        catch (Exception e) {

        }

        try {
            _camera.setPreviewDisplay(_holder);
            _camera.startPreview();
        } catch (Exception e) {
            Log.d("KVE", "Error starting camera preview: " + e.getMessage());
        }
    }
}
