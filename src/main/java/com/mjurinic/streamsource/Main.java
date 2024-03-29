package com.mjurinic.streamsource;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;


public class Main extends ActionBarActivity {

    private Camera m_camera;
    private CameraPreview m_preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_camera = getCameraInstance();
        m_preview = new CameraPreview(this, m_camera);

        initCameraParams();

        FrameLayout framePreview = (FrameLayout) findViewById(R.id.camera_preview);
        framePreview.addView(m_preview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (m_camera == null) {
            m_camera = getCameraInstance();
            m_preview = new CameraPreview(this, m_camera);

            FrameLayout framePreview = (FrameLayout) findViewById(R.id.camera_preview);
            framePreview.addView(m_preview);
        }
    }

    private void releaseCamera() {
        if (m_camera != null) {
            m_camera.setPreviewCallback(null);
            m_preview.getHolder().removeCallback(m_preview);
            m_camera.release();
            m_camera = null;
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open();
        }
        catch (Exception e) {
            Log.d("getCameraInstance", "Error getting camera instance: " + e.getMessage());
        }

        return c;
    }

    private void initCameraParams() {
        Camera.Parameters params = m_camera.getParameters();

        params.setPreviewSize(1280, 720);
        params.setPreviewFormat(ImageFormat.NV21);
        params.setSceneMode(Camera.Parameters.SCENE_MODE_STEADYPHOTO);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        m_camera.setParameters(params);
    }
}
