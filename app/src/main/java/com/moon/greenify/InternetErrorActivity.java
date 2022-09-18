package com.moon.greenify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.moon.greenify.canteen.FoodLoadingView;

public class InternetErrorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet_error);
        Button retryButton = findViewById(R.id.retryButton);
        LottieAnimationView lottieAnimationView = findViewById(R.id.no_internet_anim);
        lottieAnimationView.playAnimation();
        retryButton.setOnClickListener(e->{startActivity(new Intent(this, FoodLoadingView.class)); finish();});
    }
}
