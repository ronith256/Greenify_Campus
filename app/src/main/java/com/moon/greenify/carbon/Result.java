package com.moon.greenify.carbon;

import android.view.View;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

public class Result {
    ViewPager2 viewPager2;
    Data data;
    View view;
    Result(ViewPager2 viewPager2, Data data, View view){
        this.viewPager2 = viewPager2;
        this.data = data;
        this.view = view;
    }
    Result(ViewPager2 viewPager2, Data data){
        this.viewPager2 = viewPager2;
        this.data = data;
    }

    void checkIfCompleted(){
        if(!data.isQ1Updated()) {run(0, true);}
        else if(!data.isQ2Updated()) {run(1, true);}
        else if(!data.isQ3Updated()) {run(2, true);}
        else if(!data.isQ4Updated()) {run(3, true);}
        else if(!data.isQ5Updated()) {run(4, true);}
        else if(!data.isQ6Updated()) {run(5, true);}
        else if(!data.isQ7Updated()) {run(6, true);}
        else if(!data.isQ8Updated()) {run(7, true);}
        else if(!data.isQ9Updated()) {run(8, true);}
        else if(!data.isQ10Updated()) {run(9, true);}
    }

    double result(){
        double emissions = 0;
        emissions += calcQ1Em()+calcQ2Em()+calcQ4Em()+calcQ5Em();
        emissions = calcQ6Em(emissions);
        emissions += calcQ7Em()+calcQ8Em()+calcQ9Em();
        emissions = calcQ10Em(emissions);
        emissions /= 1000;
        return emissions;
    }

    private int calcQ1Em(){
        if(data.busButtonActive){
            return 50400;
        }
        else return 0;
    }

    private double calcQ2Em(){
        if(data.q2yes){ // If hosteler == yes
            if(data.q3yes){ // If turn off lights == yes
                return ((findElectricityUsage(160, Double.parseDouble(data.q2aValue))*210))*0.92;
            } else {return ((findElectricityUsage(160, 24)*210))*0.92;} // if turn off light == no
        } else return 248.4;
    }

    private double calcQ4Em(){
        if(data.q4yes){return findElectricityUsage(350, 2)*365*0.92;}
        else return findElectricityUsage(100, 2)*365*0.92;
    }

    private double findElectricityUsage(double power, double time){
        return power*time/1000;
    }

    private double calcQ5Em(){
        if(data.q5never){return 0;}
        else if(data.q5once){return 10400;}
        else if(data.q5more){return 72800;}
        return 0;
    }

    private double calcQ6Em(double currentEmissions){
        if(data.q6no){return currentEmissions*1.89;}
        else return currentEmissions;
    }

    private double calcQ7Em(){
        return 0.66*(Double.parseDouble(data.q7value)*2.6);
    }

    private double calcQ8Em(){
        return 100000*(Double.parseDouble(data.q8value));
    }

    private double calcQ9Em(){
        if(data.q9yes) return 208250;
        else return 0;
    }

    private double calcQ10Em(double currentEmissions){
        if(data.q10yes){
            double carbonSaved = Double.parseDouble(data.q10value) * 20000;
            return currentEmissions - carbonSaved;
        } else return currentEmissions;
    }

//    private int calcQ2Em(){
//        if(data.q1)
//    }

    void run(int position, boolean toast){
        viewPager2.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager2.setCurrentItem(position, true);
                if(toast){
                    Toast.makeText(view.getContext(), "Please give input", Toast.LENGTH_SHORT).show();
                }
            }
        }, 10);
    }
}
