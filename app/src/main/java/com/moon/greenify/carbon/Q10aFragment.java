package com.moon.greenify.carbon;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.moon.greenify.R;

public class Q10aFragment extends Fragment {
    Data data;
    ViewPager2 viewPager2;
    Result result;
    Q10aFragment(Data data, ViewPager2 viewPager2){
        this.data = data;
        this.viewPager2 = viewPager2;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_10a, container, false);
        result = new Result(viewPager2, data, view);
        if(data.q10no && data.isQ10Updated()){
            result.run(12, false);
        }
        EditText treesValue = view.findViewById(R.id.q10aTreeEdit);
        LottieAnimationView checkmark = view.findViewById(R.id.q10aCheck);
        checkmark.setProgress(0);
        checkmark.pauseAnimation();
        treesValue.addTextChangedListener(new TextWatcher() {
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
                data.setQ10aData(treesValue.getText().toString());
            }
        });
        return view;
    }
}
