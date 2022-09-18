package com.moon.greenify.espM;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import is.arontibo.library.ElasticDownloadView;
import is.arontibo.library.ProgressDownloadView;

import com.moon.greenify.R;

import java.util.ArrayList;

public class WaterLoadingScreen extends AppCompatActivity {
    ElasticDownloadView elasticDownloadView;
    public static ArrayList<WaterTankModel> waterTankList;
    public static ArrayList<GarbageDataModel> garbageCansList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_animation);
        elasticDownloadView = findViewById(R.id.elastic_download_view);
//        WaveLoadingView mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        elasticDownloadView.startIntro();
        Intent myIntent = new Intent(this, ESPView.class);
        DBManager dbManager = new DBManager(getApplicationContext(), this, elasticDownloadView);
        waterTankList = new ArrayList<>();
        garbageCansList = new ArrayList<>();
        new Handler().postDelayed(() -> new Thread(() -> {
            dbManager.start("http://192.168.99.178:80/espDemo/waterData.db", "waterData");
            DBHelper dbHelper = new DBHelper(getApplicationContext(), "waterData.db", 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor waterCursor = db.rawQuery("SELECT * FROM waterData", null);
            new Thread(() -> {
                Cursor garbageCursor = db.rawQuery("SELECT * FROM garbageData", null);
                if(garbageCursor.moveToFirst()){
                    do{
                        garbageCansList.add(new GarbageDataModel(garbageCursor.getString(0), garbageCursor.getString(1), garbageCursor.getString(2), garbageCursor.getString(3), garbageCursor.getString(4), garbageCursor.getString(5)));
                    } while(garbageCursor.moveToNext());
                }
                garbageCursor.close();
                db.close();
            }).start();

            if(waterCursor.moveToFirst()){
                do{
                    waterTankList.add(new WaterTankModel(waterCursor.getString(0), waterCursor.getString(1), waterCursor.getString(2), waterCursor.getString(3), waterCursor.getString(4), waterCursor.getString(5)));
                } while(waterCursor.moveToNext());
            }
            waterCursor.close();

        }).start(),ProgressDownloadView.ANIMATION_DURATION_BASE - 200);

        new Handler().postDelayed(() -> {
            WaterLoadingScreen.this.startActivity(myIntent);
            finish();
        }, 3*ProgressDownloadView.ANIMATION_DURATION_BASE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
