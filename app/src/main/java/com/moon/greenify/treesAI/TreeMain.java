package com.moon.greenify.treesAI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.util.concurrent.ListenableFuture;
import com.moon.greenify.R;
import com.moon.greenify.canteen.FoodModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class TreeMain extends AppCompatActivity {
    private final int CAMERA_REQUEST_CODE = 0;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;
    private Hashtable<String, String> treeScoreTable;
    private ImageAnalysis imageAnalyzer;
//    private ImageClassifierHelper imageClassifierHelper;
    Dialog dialog;
    private String result;
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
        int maxResults = 1;
        int currentDelegate = 0;
        int currentModel = 0;
        Context context = getApplicationContext();
//        imageClassifierHelper = new ImageClassifierHelper(threshold, numThreads, maxResults, currentDelegate, currentModel, context);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
        new Handler().postDelayed(this::capturePhoto, 3000);
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
        dialog = new Dialog(TreeMain.this);
        dialog.setContentView(R.layout.popup_tree_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showPopup(getWindow().getDecorView().getRootView());
        a = Bitmap.createScaledBitmap(a, 224, 224, true);
        if (a != null) {
            a.setConfig(Bitmap.Config.ARGB_8888);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            a.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encodstring = java.util.Base64.getEncoder().encodeToString(byteArray);
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            encodstring = "data:image/jpeg;base64," + encodstring;
            String payload = "{\"data\":[\"" +encodstring+ "\"]}";
            new Thread(new Runnable() {
                @Override
                public void run() {
                   result = identifyTree("https://0431-104-28-219-121.in.ngrok.io/api/predict", payload);
                    System.out.println(result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result != null){
                                if(result.contains("label")){
                                    LottieAnimationView animView = dialog.findViewById(R.id.loading_anim);
                                    animView.setVisibility(View.GONE);
                                    result = result.substring(result.indexOf("label")+8, result.indexOf(",")-1);
                                    ImageView imageView = dialog.findViewById(R.id.image_ai);
                                    TextView textView = dialog.findViewById(R.id.tree_name);
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText(result.replaceAll("_", " "));
                                    String imageString = "http://192.168.100.239:80/img/" + result + ".jpg";
                                    Glide.with(getApplicationContext()).load(imageString).into(imageView);
                                    imageView.setVisibility(View.VISIBLE);
                                    Button button = dialog.findViewById(R.id.wiki_button);
                                    button.setVisibility(View.VISIBLE);
//                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }).start();
        }

//        if(result != null){
//            addToTreeScores(result);
//        }
        long time = System.currentTimeMillis();
//        List<Classifications> list = imageClassifierHelper.classify(a, 0);
//        Classifications classification = list.get(0);
//        Toast.makeText(this, classification.getCategories().toString(), Toast.LENGTH_LONG).show();
//        System.out.println();
//        Toast.makeText(this, String.valueOf((System.currentTimeMillis()-time)), Toast.LENGTH_SHORT).show();
    }


    private String identifyTree(String URL, String encodedString){
        try{
            java.net.URL url = new URL(URL);
            URLConnection con = null;
            try {
                con = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpURLConnection http = (HttpURLConnection)con;
            try {
                if (http != null) {
                    http.setRequestMethod("POST"); // PUT is another valid option
                    http.setDoOutput(true);
                    http.setConnectTimeout(1000);
                    byte[] out = encodedString.getBytes();
                    int length = out.length;
                    http.setFixedLengthStreamingMode(length);
                    http.setRequestProperty("Content-Type","application/json");
                    try {
                        http.connect();
                    } catch (IOException e) {
                        return "";
                    }
                    try(OutputStream os = http.getOutputStream()) {
                        os.write(out);
                    } catch (IOException ex) {
                        return "";
                    }
                    InputStream is = null;
                    try {
                        is = http.getInputStream();
                    } catch (IOException e) {
                        return "";
                    }

                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    for (int length1; (length1 = is.read(buffer)) != -1; ) {
                        result.write(buffer, 0, length1);
                    }
                    return result.toString();
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
                return "";
            }
           return "";
            // StandardCharsets.UTF_8.name() > JDK 7

        } catch (SocketTimeoutException | MalformedURLException e) {} catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void addToTreeScores(String treeScoresJson){
        treeScoreTable = new Hashtable<>();
        try{
            JSONArray jsonArray = new JSONArray(treeScoresJson);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                treeScoreTable.put(jsonObject.getString("confidences"), jsonObject.getString("label"));
            }
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Error Fetching From Database", Toast.LENGTH_SHORT).show();
        }
    }

    public void showPopup(View v){
        LottieAnimationView animView = dialog.findViewById(R.id.loading_anim);
        ImageView imageView = dialog.findViewById(R.id.image_ai);
        Button button = dialog.findViewById(R.id.wiki_button);
        button.setOnClickListener(e->{
            String url = "http://www.stackoverflow.com";
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            finish();
        });
        animView.playAnimation();
        dialog.show();
    }
}
