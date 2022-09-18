package com.moon.greenify.canteen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moon.greenify.R;

import org.json.JSONObject;

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

public class PaymentPage extends AppCompatActivity  {
    TextView orderID;
    final String UPI_ID = "yourupiid@vpa";
    JSONObject jsonObject;
    String orderAmount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        Intent intent = getIntent();
        orderAmount = intent.getStringExtra("total");
        orderID = findViewById(R.id.orderID);
        new Thread(this::generateOrderID).start();
        TextView orderTotal = findViewById(R.id.orderTotal);
        RadioButton walletRadioButton = findViewById(R.id.walletRadioButton);
        RadioButton upiRadioButton = findViewById(R.id.upiRadioButton);
        Button payButton = findViewById(R.id.payButton);
        orderTotal.setText("₹" + orderAmount);
        payButton.setOnClickListener(e->{
            jsonObject = new JSONObject(FoodViewAdapter.foodItemsCount);
            if(walletRadioButton.isChecked()){
                Toast.makeText(this, "Amrita Wallet not supported yet!", Toast.LENGTH_SHORT).show();
            }
            startUPI(orderAmount);
        });
    }

    private void generateOrderID(){
        try{
            URL url = new URL("http://192.168.99.178:80/espDemo/generateOrderID.php");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);
            http.setConnectTimeout(1000);
            Map<String,String> arguments = new HashMap<>();
            arguments.put("api", "3432");
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
            if(result.toString().length() == 8){
            runOnUiThread(()->orderID.setText(result.toString()));}
            else{
                runOnUiThread(()->orderID.setText("Order Failed"));
            }
        } catch(Exception ignored){}
    }

    private void sendJson(String status){
        try{
            URL url = new URL("http://192.168.99.178:80/espDemo/putOrder.php");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setConnectTimeout(1000);
            Map<String,String> arguments = new HashMap<>();
            arguments.put("api", "3434");
            arguments.put("orderinf", jsonObject.toString(0));
            arguments.put("id", orderID.getText().toString());
            arguments.put("status", status);
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
        } catch(Exception ignored){}
    }

    void startUPI(String amount) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", UPI_ID)
                .appendQueryParameter("pn", UPI_ID)
                .appendQueryParameter("mc", "5499")
                .appendQueryParameter("tr", orderID.getText().toString())
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            upiActivityLauncher.launch(chooser);
        } else {
            Toast.makeText(this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }


    ActivityResultLauncher<Intent> upiActivityLauncher = registerForActivityResult(
       new ActivityResultContracts.StartActivityForResult(),
       result -> {
        Intent data = result.getData();
        if(data != null) {
            String status = data.getStringExtra("Status");
            if(status.contains("success")){
                sendJson("success");
            } else {
                sendJson("failed");
            }
        } else {
            sendJson("failed");
        }
       }
    );
}

