package com.moon.greenify.carbon;

import android.os.Bundle;
import com.moon.greenify.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Q1Fragment extends Fragment{
    Data data;

    Q1Fragment(Data data){
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        boolean b1selected = false;
        boolean b2selected = false;
        Button busButton = view.findViewById(R.id.q1BusButton);
        Button walkButton = view.findViewById(R.id.q1WalkButton);

        if(data.isQ1Updated()){
            if(!data.walkButtonActive){walkButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));}
            else{busButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));}
        }

        walkButton.setOnClickListener(view1 -> {
            if(!data.walkButtonActive){
                walkButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                busButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ1Data(false, true);
            } else {
                walkButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ1Data(false, false);
            }
        });

        busButton.setOnClickListener(e-> {
            if(!data.busButtonActive){
                busButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                walkButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ1Data(true, false);
            } else {
                busButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ1Data(false, false);
            }
        });
        return view;
    }

}
