package com.hirmiproject.hirmi.ui.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.hirmiproject.hirmi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class daily_report_fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.daily_report_fragment,container,false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("report");
        final DatabaseReference image_ref = database.getReference("item");
        final ListView lv = view.findViewById(R.id.liv_id);
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());




        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Calendar cal = Calendar.getInstance();
                int mnth = cal.get(Calendar.MONTH);
                mnth+=1;
                 int year= cal.get(Calendar.YEAR);

                snapshot =  snapshot.child(String.valueOf(year)).child(String.valueOf(mnth));
                final ArrayList<monthly_model> objects = new ArrayList<monthly_model>();
                for (final DataSnapshot s: snapshot.getChildren()){
                    if (s.child("date").getValue().toString().equals(formattedDate)){
                        image_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot i: snapshot.getChildren()){
                                    if (i.getKey().equals(s.child("drawing").getValue().toString())){
                                        String url = i.child("image_url").child("murl").getValue().toString();
                                        objects.add(new monthly_model(s.child("drawing").getValue().toString(),s.child("quantity").getValue().toString()+" "+ s.child("status").getValue().toString()
                                                ,s.child("date").getValue().toString(),s.child("inspector").getValue().toString(),url));

                                        CustomAdapter customAdapter = new CustomAdapter(getContext(),objects);
                                        lv.setAdapter(customAdapter);
                                        customAdapter.notifyDataSetChanged();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


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
        private ArrayList<monthly_model> objects;

        private class ViewHolder {
            TextView textView1;
            TextView textView2, textView3, textView4,textView5;

        }

        public CustomAdapter(Context context, ArrayList<monthly_model> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public monthly_model getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            CustomAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new CustomAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.monthly_list_items, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.m_draw_id);
                holder.textView2 = (TextView) convertView.findViewById(R.id.m_status_id);
                holder.textView3 = (TextView) convertView.findViewById(R.id.m_date_time_id);
                holder.textView4 = convertView.findViewById(R.id.m_ins_id);
                holder.textView5 = convertView.findViewById(R.id.image_id);

                convertView.setTag(holder);
            } else {
                holder = (CustomAdapter.ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(objects.get(position).getDrawing());


            holder.textView2.setText(objects.get(position).getStatus());
            if (objects.get(position).getStatus().contains("ACCEPTED")) {
                holder.textView2.setTextColor(Color.GREEN);
            } else if (objects.get(position).getStatus().contains("REJECTED")) {
                holder.textView2.setTextColor(Color.RED);

            } else {
                holder.textView2.setTextColor(Color.GRAY);
            }

            holder.textView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse(objects.get(position).getImage_url()));

                    startActivity(browser);

                }
            });
            holder.textView3.setText(objects.get(position).getDate());
            holder.textView4.setText(objects.get(position).getInspector());
            return convertView;

        }
    }
}
