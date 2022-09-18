package com.moon.greenify.espM;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moon.greenify.R;

import java.util.ArrayList;

public class GarbageCansView extends Fragment {
    ArrayList<GarbageDataModel> garbageList;
    RecyclerView recyclerView;
    GarbageRecyclerAdapter garbageRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_garbage_can_view, container, false);
//        setContentView(R.layout.activity_water_tank_view);
        garbageList = WaterLoadingScreen.garbageCansList;
        System.out.println(garbageList.get(0).capacity);
        recyclerView = view.findViewById(R.id.garbageCanView);
        garbageRecyclerAdapter = new GarbageRecyclerAdapter(view.getContext(), garbageList);
        recyclerView.setAdapter(garbageRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
}
