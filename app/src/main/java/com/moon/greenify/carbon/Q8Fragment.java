package com.moon.greenify.carbon;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.moon.greenify.R;

public class Q8Fragment extends Fragment {
    Data data;
    Q8Fragment(Data data){
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
                        R.layout.fragment_8,
                        container, false);

        EditText gadgetsValue = view.findViewById(R.id.q8GadgetsEdit);
        LottieAnimationView checkmark = view.findViewById(R.id.q8animCheck);
        checkmark.setProgress(0);
        checkmark.pauseAnimation();
//        checkmark.setOnClickListener(e-> );
        gadgetsValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1){
                    checkmark.setVisibility(View.VISIBLE);
                    checkmark.playAnimation();
                }
                else if(editable.toString().length() < 1){
                    checkmark.setVisibility(View.GONE);
                }
                data.setQ8Data(gadgetsValue.getText().toString());
            }
        });

        return view;
    }
}
