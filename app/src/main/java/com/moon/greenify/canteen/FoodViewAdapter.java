package com.moon.greenify.canteen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moon.greenify.R;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

public class FoodViewAdapter extends RecyclerView.Adapter<FoodViewAdapter.ViewHolder> {
    Context context;
    ArrayList<FoodModel> foodModelsList;
    public static Hashtable<String, Integer> foodItemsCount;
    Button checkOutButton;
    HashMap<String, Float> priceMap;
    TextView checkoutText;
    FoodViewAdapter(Context context, ArrayList<FoodModel> foodModelsList, Button checkOutButton, TextView checkoutText, HashMap<String, Float> foodID_PriceMap){
        this.context = context;
        this.foodModelsList = foodModelsList;
        this.checkOutButton = checkOutButton;
        foodItemsCount = new Hashtable<>();
        this.priceMap = foodID_PriceMap;
        this.checkoutText = checkoutText;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_view_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodModel foodModel = foodModelsList.get(position);
        String foodID = foodModel.foodID;
        int stock = Integer.parseInt(foodModel.stock);
        Glide.with(context).load(foodModel.foodImageUrl).into(holder.imageOfFood);
        holder.nameOfFood.setText(foodModel.foodName);
        holder.priceOfFood.setText(foodModel.foodPrice);

        if(foodItemsCount.contains(foodID)){
            holder.addButton.setVisibility(View.GONE);
            holder.foodCounterView.setVisibility(View.VISIBLE);
            holder.foodCounterText.setText("1");
            foodItemsCount.put(foodID, foodItemsCount.get(foodID));
            setCheckOutButton();
        } else {
        holder.addButton.setOnClickListener(e->{
            holder.addButton.setVisibility(View.GONE);
            holder.foodCounterView.setVisibility(View.VISIBLE);
            holder.foodCounterText.setText("1");
            foodItemsCount.put(foodID, 1);
            setCheckOutButton();
        });}
        // For multi-order of same food

        holder.incrementButton.setOnClickListener(e->{
            if(foodItemsCount.get(foodID) >= stock) {
                Toast.makeText(context, "Order limit for current item reached!", Toast.LENGTH_SHORT).show();
                setCheckOutButton();
            } else {
            foodItemsCount.put(foodID,(foodItemsCount.get(foodID)+1));}
            holder.foodCounterText.setText(foodItemsCount.get(foodID).toString());
            setCheckOutButton();
        });

        holder.decrementButton.setOnClickListener(e->{
            if(foodItemsCount.get(foodID) == 1){
                holder.foodCounterView.setVisibility(View.GONE);
                holder.addButton.setVisibility(View.VISIBLE);
                holder.foodCounterText.setText("0");
                foodItemsCount.remove(foodID);
                setCheckOutButton();
            } else {
            foodItemsCount.put(foodID,(foodItemsCount.get(foodID)-1));
            holder.foodCounterText.setText(foodItemsCount.get(foodID).toString());
            setCheckOutButton();
            }
        });
    }

    private void setCheckOutButton(){
        if(!foodItemsCount.isEmpty()){
            Animation slideUp = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
            if(checkOutButton.getVisibility() == View.GONE){
                checkOutButton.setAnimation(slideUp);
                checkoutText.setAnimation(slideUp);
            }
            checkOutButton.setVisibility(View.VISIBLE);
            checkoutText.setVisibility(View.VISIBLE);
            float total = calculateTotal();
            String a = String.format(Locale.ENGLISH, "%.2f",total);
            a = "Total: " + "₹" + a;
            checkOutButton.setText(a);
        } else {
            Animation slideDown = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
            if(checkOutButton.getVisibility() == View.VISIBLE){
                checkoutText.setAnimation(slideDown);
                checkOutButton.setAnimation(slideDown);
            }
            checkOutButton.setVisibility(View.GONE);
            checkoutText.setVisibility(View.GONE);
        }
    }

    private float calculateTotal(){
        float total = 0;
        Enumeration<String> e = foodItemsCount.keys();
        while (e.hasMoreElements()) {
            String id = e.nextElement();
            if(priceMap.containsKey(id)){
                float price = priceMap.get(id) * foodItemsCount.get(id);
                total += price;
            }
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return foodModelsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageOfFood;
        TextView nameOfFood;
        TextView priceOfFood;
        Button addButton;

        CardView foodCounterView;
        Button incrementButton;
        Button decrementButton;
        TextView foodCounterText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageOfFood = itemView.findViewById(R.id.foodImage);
            nameOfFood = itemView.findViewById(R.id.nameOfFood);
            priceOfFood = itemView.findViewById(R.id.foodPrice);
            addButton = itemView.findViewById(R.id.addButton);

            foodCounterView = itemView.findViewById(R.id.foodCounterView);
            incrementButton = itemView.findViewById(R.id.plusButton);
            decrementButton = itemView.findViewById(R.id.minusButton);
            foodCounterText = itemView.findViewById(R.id.foodCounterText);
        }
    }
}
