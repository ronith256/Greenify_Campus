package com.moon.greenify.espM;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorSpace;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.moon.greenify.R;

import java.util.ArrayList;
import java.util.Locale;

public class GarbageRecyclerAdapter extends RecyclerView.Adapter<GarbageRecyclerAdapter.ViewHolder> {

    Dialog mapDialog;
    public GarbageRecyclerAdapter(Context context, ArrayList<GarbageDataModel> garbageCanList) {
        this.context = context;
        this.garbageCanList = garbageCanList;
    }

    private Context context;
    private ArrayList<GarbageDataModel> garbageCanList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.garbage_can_view_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mapDialog = new Dialog(holder.itemView.getContext());
        GarbageDataModel dataModel = garbageCanList.get(position);
        double garbagePercentage = (dataModel.distanceToGarbage/dataModel.capacity)*100;
        String percentage = "At " + String.format(Locale.ENGLISH,"%.2f",garbagePercentage) + "%";
        holder.canName.setText(dataModel.name);
        holder.usedSpace.setText(percentage);
        holder.canCapacity.setText(String.valueOf(dataModel.capacity));
        if(garbagePercentage >= 75){holder.animView.setAnimation(R.raw.green);}
        else if(garbagePercentage < 75 && garbagePercentage >= 50){holder.animView.setAnimation(R.raw.yellow);}
        else {holder.animView.setAnimation(R.raw.red);}
        holder.animView.playAnimation();
        holder.cardView.setOnLongClickListener(e->{
            showPopup(holder.itemView, dataModel.latitude, dataModel.longitude, dataModel.name);
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
        return garbageCanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView canName;
        TextView usedSpace;
        TextView canCapacity;
        LottieAnimationView animView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            canName = itemView.findViewById(R.id.nameOfCan);
            usedSpace = itemView.findViewById(R.id.garbagePercentage);
            canCapacity = itemView.findViewById(R.id.garbageCapacity);
            animView = itemView.findViewById(R.id.garbageCanAnim);
            cardView = itemView.findViewById(R.id.garbage_can_view);
        }
    }
}
