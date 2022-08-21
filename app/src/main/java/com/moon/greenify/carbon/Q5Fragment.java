package com.moon.greenify.carbon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.moon.greenify.R;

public class Q5Fragment extends Fragment {
    Data data;
    Q5Fragment(Data data){
        this.data = data;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater
                .inflate(
                        R.layout.fragment_5,
                        container, false);
        Button neverButton = view.findViewById(R.id.q5NeverButton);
        Button onceButton = view.findViewById(R.id.q5OnceButton);
        Button moreButton = view.findViewById(R.id.q5MoreButton);

        if(data.isQ5Updated()){
            if(!data.q5never){neverButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));}
            else if(!data.q5once){onceButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));}
            else{moreButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));}
        }

        neverButton.setOnClickListener(e->{
            if(!data.q5never){
                neverButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                onceButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                moreButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ5Data(true, false, false);
            } else {
                onceButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ5Data(false, false, false);
            }
        });

        onceButton.setOnClickListener(e->{
            if (!data.q5once){
                onceButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                neverButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                moreButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ5Data(false, true, false);
            } else {
                neverButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                data.setQ5Data(false, false, false);
            }
        });

        moreButton.setOnClickListener(e->{
            if (!data.q5more){
                moreButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                neverButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                onceButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ5Data(false, false, true);
            } else {
                moreButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                data.setQ5Data(false, false, false);
            }
        });
        return view;
    }
}
