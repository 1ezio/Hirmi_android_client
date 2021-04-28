package com.hirmiproject.hirmi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.ui.main.emp_model;

import java.util.ArrayList;

public class employee_initiator extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_inspector,container,false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("custodian");
        final ListView list = view.findViewById(R.id.list_id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<emp_model> objects = new ArrayList<emp_model>();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("reg_id").getValue().toString();
                    email = email.replace(".",",");
                    String phn = dataSnapshot.child("phn").getValue().toString();

                    objects.add(new emp_model(name,email,phn));

                    CustomAdapter customAdapter = new CustomAdapter(getContext(),objects);
                    list.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();





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
        private ArrayList<emp_model> objects;

        private class ViewHolder {
            TextView textView1;
            TextView textView2, textView3;

        }

        public CustomAdapter(Context context, ArrayList<emp_model> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public emp_model getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            CustomAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new CustomAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.emp_list_items, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.name_id);
                holder.textView2 = (TextView) convertView.findViewById(R.id.email_id);
                holder.textView3 = (TextView) convertView.findViewById(R.id.phn_id);

                convertView.setTag(holder);
            } else {
                holder = (CustomAdapter.ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(objects.get(position).getMname());


            holder.textView2.setText(objects.get(position).getMemail());





            holder.textView3.setText(objects.get(position).getMphn());

            return convertView;

        }
    }


}
