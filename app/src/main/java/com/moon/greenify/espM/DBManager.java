package com.moon.greenify.espM;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import is.arontibo.library.ElasticDownloadView;
import is.arontibo.library.ProgressDownloadView;

public class DBManager {
    Context context;
    Activity activity;
    ElasticDownloadView elasticDownloadView;
    boolean updated = false;
    String timeFromDb;
    DBManager(Context context, Activity activity, ElasticDownloadView elasticDownloadView){
        this.context = context;
        this.activity = activity;
        this.elasticDownloadView = elasticDownloadView;
    }

    public void start(String downloadUrl, String tableName){

           Thread a = new Thread(new Runnable() {
               @Override
               public void run() {
                   if(isDBUpdated("waterData")){
                       File file = context.getDatabasePath("waterData.db");
                   try {
                       downloadDB(downloadUrl, file);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                } else {activity.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           elasticDownloadView.success();
                       }
                   });}
               }
           });
           a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isDBUpdated(String tableName){
        try{
            java.net.URL url = new URL("http://192.168.99.178:80/espDemo/getDB.php");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);
            http.setConnectTimeout(1000);
            Map<String,String> arguments = new HashMap<>();
            arguments.put("action", "1");
            arguments.put("tableName", tableName);
            StringJoiner sj = new StringJoiner("&");
            for(Map.Entry<String,String> entry : arguments.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;
            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }
            InputStream is = http.getInputStream();

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length1; (length1 = is.read(buffer)) != -1; ) {
                result.write(buffer, 0, length1);
            }

            // StandardCharsets.UTF_8.name() > JDK 7

            timeFromDb = result.toString();
            String ogTime = context.getSharedPreferences("default", 0).getString("ogTime","");
            return !timeFromDb.equals(ogTime);
        } catch (java.net.SocketTimeoutException e){
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Showing Cached Data", Toast.LENGTH_LONG).show();
                }
            }, 2*ProgressDownloadView.ANIMATION_DURATION_BASE);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void downloadDB(String URL, File ngrokArchive) throws IOException {
        long timeStarted = System.currentTimeMillis();
        URL url = new URL(URL);
        double fileSize = (double)getFileSize(url);
        int pg = 0;
        System.out.println(fileSize);
        try (InputStream in = url.openStream();
             BufferedInputStream bis = new BufferedInputStream(in);
             FileOutputStream fos = new FileOutputStream(ngrokArchive)) {
            byte[] data = new byte[1024];
            int counter = 0;
            int count;
            while ((count = bis.read(data, 0, 1024)) != -1) {
                // For timeout
                if(System.currentTimeMillis() - timeStarted >= 10000){elasticDownloadView.fail(); return;}

                // Calculating file size
                counter+=1024;
                int progress = pg = (int)((counter/fileSize)*100);
                fos.write(data, 0, count);

                // To update the progress bar
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progress<=100){
                        elasticDownloadView.setProgress(progress);}
                        if(progress >= 100){
                            elasticDownloadView.success();
                        }
                    }
                });
            }
            if(pg>=100){context.getSharedPreferences("default", 0).edit().putString("ogTime",timeFromDb).apply();}
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    elasticDownloadView.success();
//                }
//            });
        }
    }

    private int getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    }
}

