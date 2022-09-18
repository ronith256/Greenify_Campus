package com.moon.greenify.canteen;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.RenderNode;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moon.greenify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import is.arontibo.library.ElasticDownloadView;

public class CanteenViewAdapter extends RecyclerView.Adapter<CanteenViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CanteenModel> canteenModelsList;
    private ArrayList<FoodModel> foodModelArrayList;
    CanteenViewAdapter(Context context, ArrayList<CanteenModel> canteenModelsList){
        this.context = context;
        this.canteenModelsList = canteenModelsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.canteen_view_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CanteenModel canteenModel = canteenModelsList.get(position);
        Glide.with(context).load(canteenModel.imageURL).into(holder.imageOfCanteen);
        holder.nameOfCanteen.setText(canteenModel.nameOfCanteen);
        holder.canteenStatus.setText(canteenModel.canteenStatus);
        Intent intent = new Intent(holder.context, FoodsView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        holder.canteenButton.setOnClickListener(e->{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    foodModelArrayList = new ArrayList<>();
                    DBHelper dbHelper = new DBHelper();
                    String foodJSON = dbHelper.getData("foodTable");
                    addToFoodModel(foodJSON);
                    FoodLoadingView.foodModelArrayList = foodModelArrayList;
                }
            }).start();
            holder.context.startActivity(intent);});
//        holder.canteenButton.setOnClickListener(e->{
//            intent.putExtra("buttonName", canteenModel.nameOfCanteen);
//            holder.elasticDownloadView.startIntro();
//            holder.relativeLayout.setVisibility(View.VISIBLE);
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    DBHelper dbHelper = new DBHelper();
//                    String foodData = dbHelper.getData(canteenModel.nameOfCanteen.replaceAll("\\s", "")+"table");
//                    addToFoodModel(foodData);
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            holder.elasticDownloadView.success();
//                        }
//                    });
//
//                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            holder.relativeLayout.setVisibility(View.GONE);
//                            holder.context.startActivity(intent);
//                        }
//                    }, 100);
//                }
//            });
//            t.start();
//        });
    }

    private void addToFoodModel(String foodJson){
        try{
            JSONArray jsonArray = new JSONArray(foodJson);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                foodModelArrayList.add(new FoodModel(jsonObject.getString("foodID"), jsonObject.getString("stock"), jsonObject.getString("foodName"), jsonObject.getString("foodPrice"), jsonObject.getString("imageURL")));
            }
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Error Fetching From Database", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return canteenModelsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageOfCanteen;
        TextView nameOfCanteen;
        TextView canteenStatus;
        CardView canteenButton;
        Context context;
//        RelativeLayout relativeLayout;
//        ElasticDownloadView elasticDownloadView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageOfCanteen = itemView.findViewById(R.id.imageOfCanteen);
            nameOfCanteen = itemView.findViewById(R.id.nameOfCanteen);
            canteenStatus = itemView.findViewById(R.id.canteenStatus);
            canteenButton = itemView.findViewById(R.id.canteenButton);
//            relativeLayout = itemView.findViewById(R.id.canteenRelativeLayout);
//            elasticDownloadView = itemView.findViewById(R.id.elastic_download_view);
        }
    }
}
