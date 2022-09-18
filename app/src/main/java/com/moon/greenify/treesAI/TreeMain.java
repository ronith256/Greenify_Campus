package com.moon.greenify.treesAI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.moon.greenify.R;

import org.tensorflow.lite.task.vision.detector.Detection;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class TreeMain extends AppCompatActivity {
    private final int CAMERA_REQUEST_CODE = 0;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;
    private ImageAnalysis imageAnalyzer;
    private ObjectDetectorHelper objectDetectorHelper;
    ImageProxy imageProxy;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity_main);
        System.out.println("sdfsdsd");
        previewView = findViewById(R.id.previewView);
        boolean cameraGranted = checkCameraPermission();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        float threshold = 0.5f;
        int numThreads = 2;
        int maxResults = 3;
        int currentDelegate = 0;
        int currentModel = 0;
        Context context = getApplicationContext();
        objectDetectorHelper = new ObjectDetectorHelper(context);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
        new Handler().postDelayed(this::capturePhoto, 5000);
    }

    private boolean checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED;
    }

    Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        // Video capture use case
        videoCapture = new VideoCapture.Builder()
                .setVideoFrameRate(30)
                .build();

       imageAnalyzer = new ImageAnalysis.Builder()
               .setTargetAspectRatio(AspectRatio.RATIO_4_3)
               .setTargetRotation(getDisplay().getRotation())
               .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
               .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
               .build();
        //bind to lifecycle:
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture, videoCapture);
    }

    private void capturePhoto(){
        Bitmap a = previewView.getBitmap();
        a = Bitmap.createScaledBitmap(a, 300, 300, true);
        if (a != null) {
            a.setConfig(Bitmap.Config.ARGB_8888);
        }
        long time = System.currentTimeMillis();
        List<Detection> list = objectDetectorHelper.detect(a, 0);
        Detection detection = list.get(0);
        Toast.makeText(this, detection.getCategories().toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Toast.makeText(this, String.valueOf((System.currentTimeMillis()-time)), Toast.LENGTH_SHORT).show();
    }

//    private void detectObjects(Bitmap a){
////        int imageRotation = imageProxy.getImageInfo().getRotationDegrees();
//
//    }
}
