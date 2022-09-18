package com.moon.greenify.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moon.greenify.InternetErrorActivity;
import com.moon.greenify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import is.arontibo.library.ElasticDownloadView;

public class FoodLoadingView extends AppCompatActivity {
    ElasticDownloadView elasticDownloadView;
    public static ArrayList<FoodModel> foodModelArrayList;
    public static ArrayList<CanteenModel> canteenModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_animation);
        elasticDownloadView = findViewById(R.id.elastic_download_view);
        elasticDownloadView.startIntro();
        canteenModels = new ArrayList<>();
        foodModelArrayList = new ArrayList<>();
        Intent toCanteensView = new Intent(this, CanteensView.class);
        DBHelper dbHelper = new DBHelper();
        new Thread(() -> {
            String canteens = dbHelper.getData("canteenTable");
            if(canteens.equals("ERROR")){
                startActivity(new Intent(this, InternetErrorActivity.class));
                finish();
                return;
            }
            addToCanteenModel(canteens);
            FoodLoadingView.this.startActivity(toCanteensView);
            finish();
        }).start();
        new Thread(() -> {
            String foodJson = dbHelper.getData("foodTable");
            addToFoodModel(foodJson);
        }).start();
    }


    private void addToCanteenModel(String canteens){
        try{
        JSONArray jsonArray = new JSONArray(canteens);
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            canteenModels.add(new CanteenModel(jsonObject.getString("imageURL"), jsonObject.getString("name"), jsonObject.getString("canteenStatus")));
        }} catch (Exception e){
//            System.out.println("you fucked up");
            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Error Fetching From Database", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToFoodModel(String foodJson){
        try{
            JSONArray jsonArray = new JSONArray(foodJson);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                foodModelArrayList.add(new FoodModel(jsonObject.getString("foodID"), jsonObject.getString("stock"), jsonObject.getString("foodName"), jsonObject.getString("foodPrice"), jsonObject.getString("imageURL")));
            }
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Error Fetching From Database", Toast.LENGTH_SHORT).show();
        }
    }
}
