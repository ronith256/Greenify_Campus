package com.moon.greenify.carbon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.moon.greenify.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class QuestionView extends FragmentActivity {
    static Data data = new Data();
    private final Q1Fragment q1Fragment = new Q1Fragment(data);
    private final Q2Fragment q2Fragment = new Q2Fragment(data);
    private final Q3Fragment q3Fragment = new Q3Fragment(data);
    private final Q4Fragment q4Fragment = new Q4Fragment(data);
    private final Q5Fragment q5Fragment = new Q5Fragment(data);
    private final Q6Fragment q6Fragment = new Q6Fragment(data);
    private final Q7Fragment q7Fragment = new Q7Fragment(data);
    private final Q8Fragment q8Fragment = new Q8Fragment(data);
    private final Q9Fragment q9Fragment = new Q9Fragment(data);
    private Result result;
    private long currentTime = 0;
    private long previousTime = 0;
    private int counter = 0;
    Q10Fragment q10Fragment;
    ResultFragment resultFragment;
    Q10aFragment q10aFragment;
    Q2aFragment q2aFragment;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    public static int NUM_PAGES = 13;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.qView);
        q2aFragment = new Q2aFragment(data);
        q10Fragment = new Q10Fragment(data, viewPager);
        q10aFragment = new Q10aFragment(data, viewPager);
        resultFragment = new ResultFragment(data, viewPager);
        result = new Result(viewPager, data);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private static final float thresholdOffset = 0.5f;
            private boolean scrollStarted, checkDirection;
            private boolean scrollFromRight = false;
            private boolean scrollFromRight10 = false;
            private int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                currentPosition = position;
                if (checkDirection) {
                    if ((thresholdOffset > positionOffset)) {
                        if(currentPosition == 3){ scrollFromRight = false;
                            viewPager.setCurrentItem(1);}
                    } else {
                        if(position == 2){
                            scrollFromRight = true;
                            viewPager.setCurrentItem(1);
                        }
                    }
                    checkDirection = false;
                }

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                if(!data.isQ2Updated()){
                if(((data.q2no && !data.q2yes) || !data.isQ2Updated()) && position == 2){
                    if(scrollFromRight){viewPager.setCurrentItem(1);
                        scrollFromRight = false;
                    } else {viewPager.setCurrentItem(3);}
                }}

                if(((data.q10no && !data.q10yes) || !data.isQ10Updated()) && position == 11) {
                    result.run(10, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (!scrollStarted && state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            if(counter == 0) {counter = 1; previousTime = System.currentTimeMillis();
                Toast.makeText(viewPager.getContext(), "Press back again to exit app", Toast.LENGTH_SHORT).show();}
            else if(counter == 1){counter = 0; currentTime = System.currentTimeMillis(); if(currentTime - previousTime < 600){super.onBackPressed();}}
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position){
                case 1:
                    return q2Fragment;
                case 2:
                    return q2aFragment;
                case 3:
                    return q3Fragment;
                case 4:
                    return q4Fragment;
                case 5:
                    return q5Fragment;
                case 6:
                    return q6Fragment;
                case 7:
                    return q7Fragment;
                case 8:
                    return q8Fragment;
                case 9:
                    return q9Fragment;
                case 10:
                   return q10Fragment;
                case 11:
                    return q10aFragment;
                case 12:
                    return resultFragment;
                default:
                    return q1Fragment;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}