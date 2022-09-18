package com.moon.greenify.canteen;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moon.greenify.R;

import java.util.ArrayList;

public class CanteensView extends AppCompatActivity {
    ArrayList<CanteenModel> canteenModels;
    RecyclerView recyclerView;
    CanteenViewAdapter canteenViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_view);
        canteenModels = FoodLoadingView.canteenModels;
        recyclerView = findViewById(R.id.canteenView);
        canteenViewAdapter = new CanteenViewAdapter(getApplicationContext(), canteenModels);
        recyclerView.setAdapter(canteenViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
