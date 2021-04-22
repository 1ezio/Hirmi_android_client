package com.hirmiproject.hirmi.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hirmiproject.hirmi.R;

public class Fragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment2_inspector_layout,container,false);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
        DatabaseReference items = database.getReference("item");




        return view;
    }
}
