package com.moon.greenify.espM;

public class GarbageDataModel {
    String name;
    float distanceToGarbage;
    double latitude, longitude;
    int capacity, ID;

    public GarbageDataModel(String ID, String name, String distanceToGarbage, String capacity, String latitude, String longitude) {
        this.name = name;
        this.distanceToGarbage = Float.parseFloat(distanceToGarbage);
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
        this.capacity = Integer.parseInt(capacity);
        this.ID = Integer.parseInt(ID);
    }
}
