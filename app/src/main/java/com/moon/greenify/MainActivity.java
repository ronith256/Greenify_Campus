package com.moon.greenify;

import androidx.appcompat.app.AppCompatActivity;

import com.moon.greenify.carbon.QuestionView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_main);

        Button startButton = findViewById(R.id.startButton);
        Intent intent = new Intent(this, QuestionView.class);
        startButton.setOnClickListener(e->this.startActivity(intent));
    }
}