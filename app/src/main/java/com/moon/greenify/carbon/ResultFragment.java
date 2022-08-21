package com.moon.greenify.carbon;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.moon.greenify.R;

import java.io.IOException;
import java.io.InputStream;

public class ResultFragment extends Fragment {
    Data data;
    Result result;
    ViewPager2 viewPager2;
    View view;
    TextView text1, text2;
    LottieAnimationView stars;
    ResultFragment(Data data, ViewPager2 viewPager2){
        this.data = data; this.viewPager2 = viewPager2;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        view = inflater
                .inflate(
                        R.layout.result_fragment,
                        container, false);
        result = new Result(viewPager2, data, view);
        text1 = view.findViewById(R.id.result_a3);
        text2 = view.findViewById(R.id.result_a4);
        stars = view.findViewById(R.id.resultAnimCheck);
        result.checkIfCompleted();
        method();
        return view;
    }

    private void method(){

        double emissions = result.result();

        text1.setText(getString(R.string.a3_text,(emissions/1000)));
        text2.setText(getString(R.string.a4_text,(emissions/(614))));

        if(emissions < 744){
            stars.setAnimation(R.raw.stars_3);
        }
        else if(emissions < 868){
            stars.setAnimation(R.raw.stars_2);
        } else if(emissions < 992){
            stars.setAnimation(R.raw.stars_1);
        }

        stars.setMinAndMaxProgress(0.0f, 0.47f);
        stars.playAnimation();

    }

    @Override
    public void onResume() {
        super.onResume();
        method();
    }
}
