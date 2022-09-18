package com.moon.greenify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.moon.greenify.birdsAI.BirdMain;
import com.moon.greenify.canteen.CanteensView;
import com.moon.greenify.canteen.FoodLoadingView;
import com.moon.greenify.carbon.QuestionView;
import com.moon.greenify.espM.ESPView;
import com.moon.greenify.espM.WaterLoadingScreen;
import com.moon.greenify.treesAI.TreeMain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText search = findViewById(R.id.search_edit_text);
        Button carbonButton = findViewById(R.id.carbon_button);
        Intent carbonIntent = new Intent(this, QuestionView.class);
        Button waterGarbageButton = findViewById(R.id.water_garbage_button);
        Intent waterGarbageIntent = new Intent(this, WaterLoadingScreen.class);
        Button treeIdentifier = findViewById(R.id.trees_ai_button);
        Intent treeIdentifierIntent = new Intent(this, TreeMain.class);
        Button birdIdentifier = findViewById(R.id.bird_ai_button);
        Intent birdIdentifierIntent = new Intent(this, BirdMain.class);
        Button canteenAccess = findViewById(R.id.canteen_button);
        Intent canteenAccessIntent = new Intent(this, FoodLoadingView.class);


        carbonButton.setOnClickListener(e->startActivity(carbonIntent));
        waterGarbageButton.setOnClickListener(e->startActivity(waterGarbageIntent));
        treeIdentifier.setOnClickListener(e->startActivity(treeIdentifierIntent));
        birdIdentifier.setOnClickListener(e->startActivity(birdIdentifierIntent));
        canteenAccess.setOnClickListener(e->startActivity(canteenAccessIntent));
    }

//    private boolean checkLocationPermission(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                == PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 9987);
//        }
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED;
//    }
}