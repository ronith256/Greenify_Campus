package com.moon.greenify.espM;

import android.app.Dialog;
import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.camera2.internal.annotation.CameraExecutor;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.moon.greenify.MainActivity;
import com.moon.greenify.R;


import java.util.ArrayList;
import java.util.Locale;

import me.itangqi.waveloadingview.WaveLoadingView;

public class WaterRecyclerAdapter extends RecyclerView.Adapter<WaterRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WaterTankModel> waterTankList;
    private View popUpView;
    private GoogleMap gMap;
    Dialog mapDialog;

    public WaterRecyclerAdapter(Context context, ArrayList<WaterTankModel> waterTankList) {
        this.context = context;
        this.waterTankList = waterTankList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.water_tank_view_items, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        MapView map_view =popUpView.findViewById(R.id.mapView);
        mapDialog = new Dialog(holder.itemView.getContext());
        WaterTankModel waterTankModel = waterTankList.get(position);
        double waterPercentage = (waterTankModel.distanceToWater/waterTankModel.capacity)*100;
        String percentage = "At " + String.format(Locale.ENGLISH,"%.2f",waterPercentage) + "%";
        holder.tankName.setText(waterTankModel.name);
        holder.tankCapacity.setText(String.valueOf(waterTankModel.capacity));
        holder.waterPercentage.setText(percentage);
        holder.waterView.setProgressValue((int)waterPercentage);
        holder.waterTankLayout.setOnLongClickListener(e-> {
            showPopup(holder.itemView, waterTankModel.latitude, waterTankModel.longitude, waterTankModel.name);
            return false;
        });
    }
    public void showPopup(View v, double latitude, double longitude, String name){
        mapDialog.setContentView(R.layout.popup_map_view);
        GoogleMap googleMap;
       Toast.makeText(context, latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
        MapView mMapView = (MapView) mapDialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(v.getContext().getApplicationContext());

        mMapView = (MapView) mapDialog.findViewById(R.id.mapView);
        mMapView.onCreate(mapDialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng latlng = new LatLng(latitude, longitude); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(latlng).title(name));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        });
        mapDialog.show();
    }

    @Override
    public int getItemCount() {
        return waterTankList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tankName;
        TextView waterPercentage;
        TextView tankCapacity;
        WaveLoadingView waterView;
        CardView waterTankLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tankName = itemView.findViewById(R.id.nameOfTank);
            waterPercentage = itemView.findViewById(R.id.waterPercentage);
            tankCapacity = itemView.findViewById(R.id.maximumCapacity);
            waterView = itemView.findViewById(R.id.waterLevelView);
            waterTankLayout = itemView.findViewById(R.id.water_tank_layout);
        }
    }
}