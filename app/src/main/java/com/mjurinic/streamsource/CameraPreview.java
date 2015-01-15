package com.mjurinic.streamsource;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder m_holder;
    private Camera m_camera;

    public CameraPreview(Context _context, Camera _camera) {
        super(_context);

        m_camera = _camera;
        m_holder = getHolder();
        m_holder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder _holder) {
        try {
            m_camera.setPreviewDisplay(_holder);

            m_camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] _data, Camera _camera) {
                    try {
                        new SendFrame(_data, _camera).execute();
                    }
                    catch (Exception e) {
                        Log.d("SendMSG Exception", e.getMessage());
                    }
                }
            });

            m_camera.startPreview();
        } catch (Exception e) {
            Log.d("surfaceCreated Exception", e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder _holder) {

    }

    public void surfaceChanged(SurfaceHolder _holder, int format, int w, int h) {
        if (m_holder.getSurface() == null) {
            return;
        }

        try {
            m_camera.stopPreview();
        }
        catch (Exception e) {

        }



        // start preview with new settings
        try {
            m_camera.setPreviewDisplay(m_holder);
            m_camera.startPreview();
        } catch (Exception e) {
            Log.d("surfaceChanged Exception", e.getMessage());
        }
    }
}
