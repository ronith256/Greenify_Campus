package com.moon.greenify.canteen;

public class FoodModel {
    String foodID;
    String foodName;
    String foodPrice;
    String foodImageUrl;
    String stock;

    public FoodModel(String foodID, String stock, String foodName, String foodPrice, String foodImageUrl) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImageUrl = foodImageUrl;
        this.stock = stock;
    }
}
