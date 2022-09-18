package com.moon.greenify.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moon.greenify.InternetErrorActivity;
import com.moon.greenify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import is.arontibo.library.ProgressDownloadView;

public class FoodsView extends AppCompatActivity {
    ArrayList<FoodModel> foodModelArrayList;
    RecyclerView recyclerView;
    FoodViewAdapter foodViewAdapter;
    Button checkOutButton;
    static HashMap<String, Float> foodID_PriceMap;
    private static String lastUpdatedTime = "";
    private static boolean firstTime = true;
    private ArrayList<FoodModel> tempFoodModelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);
        foodModelArrayList = FoodLoadingView.foodModelArrayList;
        foodID_PriceMap = new HashMap<>();
        new Thread(() -> {
            for(int i = 0; i < foodModelArrayList.size(); i++){
                foodID_PriceMap.put(foodModelArrayList.get(i).foodID, Float.parseFloat(foodModelArrayList.get(i).foodPrice));
            }
        }).start();
        new Thread(this::reload).start();
        recyclerView = findViewById(R.id.foodView);
        checkOutButton = findViewById(R.id.checkoutButton);
        TextView checkoutText = findViewById(R.id.checkoutText);
        foodViewAdapter = new FoodViewAdapter(getApplicationContext(), foodModelArrayList, checkOutButton, checkoutText, foodID_PriceMap);
        recyclerView.setAdapter(foodViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Intent newIntent = new Intent(this, PaymentPage.class);
        checkOutButton.setOnClickListener(e->{
            newIntent.putExtra("total", checkOutButton.getText().toString().substring(8));
            startActivity(newIntent);
            finish();
        });
    }

    private void reload(){
        int retryCounter = 0;
        while(true){
//            Log.d("reloadThread", "threadRunning");
            tempFoodModelList = new ArrayList<>();
            try {
                if(isDBUpdated()){
                    retryCounter = 0;
                    System.out.println("db updated");
                    DBHelper dbHelper = new DBHelper();
                    String foodJson = dbHelper.getData("foodTable");
                    addToFoodModel(foodJson);
                    FoodLoadingView.foodModelArrayList = tempFoodModelList;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
        //                    finish();
        //                    overridePendingTransition(0, 0);
        //                    startActivity(getIntent());
        //                    overridePendingTransition(0, 0);
                            recreate();
                        }
                    });
                    retryCounter = 0;
                }
            } catch (SocketTimeoutException e) {
                retryCounter++;
                if(retryCounter > 10){
                    startActivity(new Intent(this, InternetErrorActivity.class));
                    finish();
                    break;
                }
                e.printStackTrace();
            } catch (IOException e) {
                retryCounter++;
                e.printStackTrace();
            }
        }
    }

    private void addToFoodModel(String foodJson){
        try{
            JSONArray jsonArray = new JSONArray(foodJson);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                tempFoodModelList.add(new FoodModel(jsonObject.getString("foodID"), jsonObject.getString("stock"), jsonObject.getString("foodName"), jsonObject.getString("foodPrice"), jsonObject.getString("imageURL")));
            }
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Error Fetching From Database", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isDBUpdated() throws java.net.SocketTimeoutException, IOException {
        URL url = new URL("http://192.168.99.178:80/espDemo/getLastUpdateFood.php");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);
        http.setConnectTimeout(1000);
        Map<String,String> arguments = new HashMap<>();
        arguments.put("action", "1");
        arguments.put("api", "8~B~~s?%Z$");
        arguments.put("tableName", "foodTable");
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
        if(firstTime){
            lastUpdatedTime = result.toString();
            firstTime = false;
        } else {
            String a = lastUpdatedTime;
            lastUpdatedTime = result.toString();
//               if(a.equals(result.toString())){
//                }
            return !result.toString().equals(a);
        }
        return false;
    }
}
