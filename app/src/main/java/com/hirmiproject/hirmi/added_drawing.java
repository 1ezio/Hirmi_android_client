package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.ui.main.emp_model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class added_drawing extends AppCompatActivity {

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_drawing);
        list = findViewById(R.id.list_id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("item");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<drawing_model> objects = new ArrayList<drawing_model>();
                for(DataSnapshot snap : snapshot.getChildren()){

                    objects.add(new drawing_model(snap.getKey()+" "+snap.child("d").getValue().toString()
                            ,snap.child("admin_email").getValue().toString(),
                            snap.child("date_added").getValue().toString(),
                            snap.child("time_added").getValue().toString()
                            ));

                }
                CustomAdapter adapter = new CustomAdapter(getApplicationContext(),objects);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<drawing_model> objects;

        private class ViewHolder {
            TextView textView1;
            TextView textView2, textView3,textview4;

        }

        public CustomAdapter(Context context, ArrayList<drawing_model> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public drawing_model getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            CustomAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new CustomAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.added_drawing_items, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.draw_id);
                holder.textView2 = (TextView) convertView.findViewById(R.id.added_id);
                holder.textView3 = (TextView) convertView.findViewById(R.id.date_id);
                holder.textview4 = (TextView) convertView.findViewById(R.id.time_id);

                convertView.setTag(holder);
            } else {
                holder = (CustomAdapter.ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(objects.get(position).getMdrawing());


            holder.textView2.setText(objects.get(position).getMemail());


            holder.textView3.setText(objects.get(position).getMdate());
            holder.textview4.setText(objects.get(position).getMtime());
            return convertView;

        }
    }

    @Override
    public void onBackPressed() {
        CustomProgress progress = new CustomProgress(added_drawing.this);
        progress.show();
        Intent intent= new Intent();

        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
        progress.dismiss();
        super.onBackPressed();
    }
}