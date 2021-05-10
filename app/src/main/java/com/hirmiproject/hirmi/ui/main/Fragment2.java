package com.hirmiproject.hirmi.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.R;
import com.hirmiproject.hirmi.model_history_inspector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fragment2 extends Fragment {
    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment2_inspector_layout,container,false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
        final DatabaseReference items = database.getReference("item");
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final ListView listView = view.findViewById(R.id.list_id);
        final ArrayList<String> arrayList = new ArrayList<String>();

        String name ;
        DatabaseReference ref = database.getReference("inspector");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<model_history_inspector> objects = new ArrayList<model_history_inspector>();
                final String name ;
                String n = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
                    n = n.replace(".",",");
                     name = snapshot.child(n).child("name").getValue().toString();

                items.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                            if(name.equals(dataSnapshot.child("inspector_name").getValue().toString()) && !dataSnapshot.child("status").getValue().toString().equals("TO BE CALL") ){

                                objects.add(new model_history_inspector(dataSnapshot.child("drawing_no").getValue().toString()
                                        ,dataSnapshot.child("date").getValue().toString(),
                                        dataSnapshot.child("time").getValue().toString(),dataSnapshot.child("status").getValue().toString(),null));
                                CustomAdapter customAdapter = new CustomAdapter(getActivity(), objects);

                                listView.setAdapter(customAdapter);
                                customAdapter.notifyDataSetChanged();
                            }



                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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
            TextView textView2,textView3,textView4,textView5;

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

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.final_history_list, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.his_draw_id);
                holder.textView2 = (TextView) convertView.findViewById(R.id.status_id);
                holder.textView3 = (TextView) convertView.findViewById(R.id.date_id);
                holder.textView5 = convertView.findViewById(R.id.image_id);
                holder.textView5.setVisibility(View.INVISIBLE);

                holder.textView4= convertView.findViewById(R.id.time_id);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(objects.get(position).getDraw());


            holder.textView2.setText(objects.get(position).getStatus());
            if (objects.get(position).getStatus().equals("ACCEPTED")){
                holder.textView2.setTextColor(Color.GREEN);
            }else if (objects.get(position).getStatus().equals("REJECTED")){
                holder.textView2.setTextColor(Color.RED);

            }else {
                holder.textView2.setTextColor(Color.GRAY);
            }


            holder.textView3.setText(objects.get(position).getDate());

            holder.textView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Toast", Toast.LENGTH_SHORT).show();
                }
            });
            holder.textView4.setText(objects.get(position).getTime());

            return convertView;
        }
    }

    /*public class Mylist extends ArrayAdapter<String>{
        private int layout;
        private List<String> mObjects;
        private Mylist(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();

                viewHolder.drawing = (TextView) convertView.findViewById(R.id.his_draw_id);
                viewHolder.status = (TextView) convertView.findViewById(R.id.status_id);
                viewHolder.date = (TextView) convertView.findViewById(R.id.date_id);
                viewHolder.time = (TextView) convertView.findViewById(R.id.time_id);

                viewHolder.drawing.setText(mObjects.get(position));
                viewHolder.date.setText(mObjects.get(position));
                viewHolder.status.setText(mObjects.get(position));



                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            final ViewHolder viewHolder = mainViewholder;







            return convertView;
        }
    }
    public class ViewHolder {


        TextView drawing,date, time, status;

    }*/
}
