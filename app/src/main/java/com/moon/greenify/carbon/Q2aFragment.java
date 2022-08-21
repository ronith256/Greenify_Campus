package com.moon.greenify.carbon;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.moon.greenify.R;

public class Q2aFragment extends Fragment {
    EditText hoursValue;
    ImageView imgView;
    TextView txtView;
    Data data;
    View view;
    Q2aFragment(Data data){
        this.data = data;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_2a, container, false);

        hoursValue  = view.findViewById(R.id.q2ahostelHourEdit);
        imgView = view.findViewById(R.id.q2aimageView);
        txtView = view.findViewById(R.id.q2atextView);

        if(data.q2yes){

         txtView.setText(R.string.q2a);
         hoursValue.setHint(R.string.editTextHours);
         imgView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_hostel));
        } else {
            txtView.setText(R.string.qs2ads);
            hoursValue.setHint(R.string.inKm);
            imgView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_house));
        }

        LottieAnimationView checkmark = view.findViewById(R.id.q2aCheck);
        checkmark.setProgress(0);
        checkmark.pauseAnimation();
        hoursValue.addTextChangedListener(new TextWatcher() {
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
                data.setQ2aData(hoursValue.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(data.q2yes){
            txtView.setText(R.string.q2a);
            hoursValue.setHint(R.string.editTextHours);
            imgView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_hostel));
        } else {
            txtView.setText(R.string.qs2ads);
            hoursValue.setHint(R.string.inKm);
            imgView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_house));
        }
    }
}
