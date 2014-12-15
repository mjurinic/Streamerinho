package com.mjurinic.streamerinho;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;


public class Main extends Activity {

    private Camera _camera;
    private CameraPreview _preview;
    private MediaRecorder _mediaRecorder;
    private MediaSaving _mediaSaving;

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _camera = getCameraInstance();
        _preview = new CameraPreview(this, _camera);

        Camera.Parameters params = _camera.getParameters();
        params.setPreviewSize(640, 480);
        _camera.setParameters(params);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(_preview);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isRecording) {
                            _mediaRecorder.stop();
                            releaseMediaRecorder();
                            _camera.lock();

                            setCaptureButtonText("Capture");
                            isRecording = false;
                        } else {
                            if (prepareVideoRecorder()) {
                                _mediaRecorder.start();

                                setCaptureButtonText("Stop");
                                isRecording = true;
                            } else {
                                releaseMediaRecorder();
                            }
                        }
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (_camera == null) {
            _camera = getCameraInstance();
            _preview = new CameraPreview(this, _camera);

            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(_preview);
        }
    }

    private void releaseMediaRecorder(){
        if (_mediaRecorder != null) {
            _mediaRecorder.reset();   // clear recorder configuration
            _mediaRecorder.release(); // release the recorder object
            _mediaRecorder = null;
            _camera.lock();
        }
    }

    private void releaseCamera(){
        if (_camera != null){
            _camera.release();        // release the camera for other applications
            _camera = null;
        }
    }

    private void setCaptureButtonText(String _text) {
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setText(_text);
    }

    private boolean prepareVideoRecorder() {
        _mediaRecorder = new MediaRecorder();

        _camera.unlock();
        _mediaRecorder.setCamera(_camera);
        _mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        _mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        _mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        _mediaSaving = new MediaSaving();
        _mediaRecorder.setOutputFile(_mediaSaving.getOutputMediaFile().toString());

        _mediaRecorder.setPreviewDisplay(_preview.getHolder().getSurface());

        try {
            _mediaRecorder.prepare();
        }
        catch (IllegalStateException e) {
            Log.d("KVE", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();

            return false;
        }
        catch (IOException e) {
            Log.d("KVE", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();

            return false;
        }

        return true;
    }

    public static Camera getCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open();
        }
        catch (Exception e){
            Log.d("KVE", "Error getting camera instance: " + e.getMessage());
        }

        return c;
    }
}
