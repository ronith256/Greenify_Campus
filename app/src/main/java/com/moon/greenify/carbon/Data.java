package com.moon.greenify.carbon;

public class Data{
    // Q1 data
    boolean busButtonActive = false;
    boolean walkButtonActive = false;
    
    void setQ1Data(boolean busButtonActive, boolean walkButtonActive){
        this.walkButtonActive = walkButtonActive;
        this.busButtonActive = busButtonActive;
    }

    boolean isQ1Updated(){
       return this.walkButtonActive || this.busButtonActive;
    }

    // Q2 data
    boolean q2yes = false;
    boolean q2no = false;
    
    void setQ2Data(boolean q2yes, boolean q2no){
        this.q2yes = q2yes;
        this.q2no = q2no;
    }

    boolean isQ2Updated(){
        return this.q2yes || this.q2no;
    }

    String q2aValue = null;

    void setQ2aData(String q2aValue){
        this.q2aValue = q2aValue;
    }

    boolean isQ2aUpdated(){
        return q2aValue != null;
    }
    
    // Q3 data
    boolean q3yes = false;
    boolean q3no = false;

    void setQ3Data(boolean q3yes, boolean q3no){
        this.q3yes = q3yes;
        this.q3no = q3no;
    }

    boolean isQ3Updated(){
        return this.q3yes || this.q3no;
    }
    
    // Q4 data
    boolean q4yes = false;
    boolean q4no = false;
    
    void setQ4Data(boolean q4yes, boolean q4no){
        this.q4yes = q4yes;
        this.q4no = q4no;
    }

    boolean isQ4Updated(){
        return this.q4yes || this.q4no;
    }

    // Q5 data
    boolean q5never = false;
    boolean q5once = false;
    boolean q5more = false;

    void setQ5Data(boolean q5never, boolean q5once, boolean q5more){
        this.q5never = q5never;
        this.q5once = q5once;
        this.q5more = q5more;
    }

    boolean isQ5Updated(){
        return this.q5never || this.q5once || this.q5more;
    }

    // Q6 data
    boolean q6yes = false;
    boolean q6no = false;

    void setQ6Data(boolean q6yes, boolean q6no){
        this.q6yes = q6yes;
        this.q6no = q6no;
    }

    boolean isQ6Updated(){
        return this.q6yes || this.q6no;
    }

    // Q7 data
    String q7value = null;

    void setQ7Data(String q7value){
        this.q7value = q7value;
    }

    boolean isQ7Updated(){
        return q7value != null;
    }
    // Q8 data
    String q8value = null;

    void setQ8Data(String q8value){
        this.q8value = q8value;
    }

    boolean isQ8Updated(){
        return q8value != null;
    }

    // Q9 data
    boolean q9yes = false;
    boolean q9no = false;

    void setQ9Data(boolean q9yes, boolean q9no){
        this.q9yes = q9yes;
        this.q9no = q9no;
    }

    boolean isQ9Updated(){
        return this.q9yes || this.q9no;
    }

    // Q10 data
    boolean q10yes = false;
    boolean q10no = false;

    void setQ10Data(boolean q10yes, boolean q10no){
        this.q10yes = q10yes;
        this.q10no = q10no;
    }

    boolean isQ10Updated(){
        return this.q10yes || this.q10no;
    }

    // Q10a
    String q10value = null;

    void setQ10aData(String q10value){
        this.q10value = q10value;
    }

    boolean isQ10aUpdated(){
        return q10value != null;
    }

    // Result data
    boolean resultButtonPressed = false;
    void setResultButtonPressed(){
        this.resultButtonPressed = true;
    }

    int numberOfStars = 1;

}
