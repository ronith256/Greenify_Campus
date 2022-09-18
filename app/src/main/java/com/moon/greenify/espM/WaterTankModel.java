package com.moon.greenify.espM;

public class WaterTankModel {
    String name;
    float distanceToWater;
    double latitude, longitude;
    int capacity, ID;

    public WaterTankModel(String ID, String name, String distanceToWater, String capacity, String latitude, String longitude) {
        this.name = name;
        this.distanceToWater = Float.parseFloat(distanceToWater);
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
        this.capacity = Integer.parseInt(capacity);
        this.ID = Integer.parseInt(ID);
    }
}
