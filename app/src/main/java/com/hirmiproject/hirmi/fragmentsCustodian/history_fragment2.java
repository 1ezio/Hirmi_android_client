package com.hirmiproject.hirmi.fragmentsCustodian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.R;
import com.hirmiproject.hirmi.fragmentsCustodian.adapter.compare_class;
import com.hirmiproject.hirmi.model_history_inspector;
import com.hirmiproject.hirmi.ui.main.Fragment2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class    history_fragment2 extends Fragment {
    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment2_inspector_layout, container, false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
        final DatabaseReference items = database.getReference("item");
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final ListView listView = view.findViewById(R.id.list_id);



        items.addValueEventListener(new ValueEventListener() {
            final ArrayList<model_history_inspector> objects = new ArrayList<model_history_inspector>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> arrayList = new ArrayList<String>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String u;
                    try {
                    u = dataSnapshot.child("image_url").child("murl").getValue().toString();
                    }catch (Exception e){
                    u = null;
                    }


                if (dataSnapshot.child("status").getValue().toString().equals("ACCEPTED") || dataSnapshot.child("status").getValue().toString().equals("REJECTED")){
                    objects.add(new model_history_inspector(dataSnapshot.child("drawing_no").getValue().toString()
                            , dataSnapshot.child("date").getValue().toString(),
                            dataSnapshot.child("time").getValue().toString(), dataSnapshot.child("status").getValue().toString(),u));

                    CustomAdapter customAdapter = new CustomAdapter(getContext(), objects);

                    listView.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();
                }





                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }


    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<model_history_inspector> objects;

        private class ViewHolder {
            TextView textView1;
            TextView textView2, textView3, textView4,textView5;

        }

        public CustomAdapter(Context context, ArrayList<model_history_inspector> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public model_history_inspector getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            CustomAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new CustomAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.final_history_list, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.his_draw_id);
                holder.textView2 = (TextView) convertView.findViewById(R.id.status_id);
                holder.textView3 = (TextView) convertView.findViewById(R.id.date_id);
                holder.textView4 = (TextView) convertView.findViewById(R.id.time_id);
                holder.textView5 = convertView.findViewById(R.id.image_id);
                holder.textView5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (objects.get(position).getImage_url() == null){
                            Toast.makeText(getActivity(), "Image Does not Exist", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse(objects.get(position).getImage_url()));

                            startActivity(browser);
                        }

                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (CustomAdapter.ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(objects.get(position).getDraw());


            holder.textView2.setText(objects.get(position).getStatus());
            if (objects.get(position).getStatus().equals("ACCEPTED")) {
                holder.textView2.setTextColor(Color.GREEN);
            } else if (objects.get(position).getStatus().equals("REJECTED")) {
                holder.textView2.setTextColor(Color.RED);

            } else {
                holder.textView2.setTextColor(Color.GRAY);
            }


            holder.textView3.setText(objects.get(position).getDate());
            holder.textView4.setText(objects.get(position).getTime());
            return convertView;
        }
    }
}