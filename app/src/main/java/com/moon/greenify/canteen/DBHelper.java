package com.moon.greenify.canteen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.moon.greenify.InternetErrorActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import is.arontibo.library.ElasticDownloadView;

public class DBHelper {

     public String getData(String tableName){
        try{
          URL url = new URL("http://192.168.100.239:80/espDemo/getCanteens.php");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);
            http.setConnectTimeout(1000);
            Map<String,String> arguments = new HashMap<>();
            arguments.put("api", "someApiKey");
            arguments.put("nameOfTable", tableName);
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
            return result.toString();
        } catch(Exception ignored){
            return "ERROR";
        }
     }
}
