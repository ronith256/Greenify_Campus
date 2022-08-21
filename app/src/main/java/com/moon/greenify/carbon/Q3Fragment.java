package com.moon.greenify.carbon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.moon.greenify.R;

public class Q3Fragment extends Fragment {
    Data data;
    Q3Fragment(Data data){
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
                        R.layout.fragment_3,
                        container, false);

        Button yesButton = view.findViewById(R.id.q3YesButton);
        Button noButton = view.findViewById(R.id.q3NoButton);

        if(data.isQ3Updated()){
            if(!data.q3yes){yesButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));}
            else{noButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));}
        }

        yesButton.setOnClickListener(e-> {
            if(!data.q3yes){
                yesButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                noButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ3Data(true, false);
            }
            else {
                yesButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ3Data(false, false);
            }
        });

        noButton.setOnClickListener(e-> {
            if(!data.q3no){
                noButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button_selected));
                yesButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ3Data(false, true);
            } else {
                noButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.custom_button));
                data.setQ3Data(false, false);
            }
        });

        return view;
    }
}
