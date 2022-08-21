package com.moon.greenify.carbon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.moon.greenify.R;

public class Q10Fragment extends Fragment {
    Data data;
    Result result;
    ViewPager2 viewPager2;
    Q10Fragment(Data data, ViewPager2 viewPager2){
        this.data = data; this.viewPager2 = viewPager2;
    }

    public Q10Fragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater
                .inflate(
                        R.layout.fragment_10,
                        container, false);

        Button yesButton = view.findViewById(R.id.q10YesButton);
        Button noButton = view.findViewById(R.id.q10NoButton);
        Button resultButton = view.findViewById(R.id.q10ResultButton);

        if(data.isQ10Updated()){
            if(!data.q10yes){yesButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));}
            else{noButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));}
        }

        result = new Result(viewPager2, data, view);
        resultButton.setOnClickListener(e->{result.checkIfCompleted(); data.setResultButtonPressed(); result.run(12, false);});
        yesButton.setOnClickListener(e-> {
            if(!data.q10yes){
                yesButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                noButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                resultButton.setVisibility(View.GONE);
                data.setQ10Data(true, false);
            }
            else {
                yesButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                resultButton.setVisibility(View.VISIBLE);
                data.setQ10Data(false, false);
            }
        });

        noButton.setOnClickListener(e-> {
            if(!data.q10no){
                noButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                yesButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                resultButton.setVisibility(View.VISIBLE);
                data.setQ10Data(false, true);
            } else {
                noButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ10Data(false, false);
                resultButton.setVisibility(View.GONE);
            }
        });
        return view;
    }
}
