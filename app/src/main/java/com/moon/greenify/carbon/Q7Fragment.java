package com.moon.greenify.carbon;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.moon.greenify.R;

public class Q7Fragment extends Fragment {
    Data data;
    Q7Fragment(Data data){
        this.data = data;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater
                .inflate(
                        R.layout.fragment_7,
                        container, false);

        EditText trashValue = view.findViewById(R.id.q7TrashEdit);
        LottieAnimationView checkmark = view.findViewById(R.id.lav_thumbUp);
        checkmark.setProgress(0);
        checkmark.pauseAnimation();
        trashValue.addTextChangedListener(new TextWatcher() {
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
                data.setQ7Data(trashValue.getText().toString());
            }
        });

        return view;
    }
}
