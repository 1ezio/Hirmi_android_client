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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.hirmiproject.hirmi.gate_activity;
import com.hirmiproject.hirmi.main_login;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

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
        final View view =  inflater.inflate(R.layout.daily_report_fragment,container,false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("report");
        final DatabaseReference image_ref = database.getReference("item");
        final ListView lv = view.findViewById(R.id.liv_id);
        final Spinner spin= view.findViewById(R.id.category_id);
        final String[] spin_choices = { "Make Choice", "Fitment", "Welding"};
        final ArrayAdapter ad
                = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                spin_choices);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spin.setAdapter(ad);

        Date c = Calendar.getInstance().getTime();
        final String formattedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final DatabaseReference monitor = database.getReference("monitor");
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final ImageView sign = view.findViewById(R.id.sign_id);
        String  mail = auth.getCurrentUser().getEmail();
        mail = mail.replace(".",",");
        final TextView total_draw = view.findViewById(R.id.total_draw_id);
        final TextView total_quant= view.findViewById(R.id.total_quant_id);
        final TextView acc= view.findViewById(R.id.acc_id);
        final Button show_rep = view.findViewById(R.id.show_rep_id);
        final TextView rej= view.findViewById(R.id.reject_id);
        final TextView pending= view.findViewById(R.id.pending_id);


        final String finalMail = mail;
        TextView textView = view.findViewById(R.id.gate_entry_id);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), gate_activity.class));
            }
        });
        monitor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (finalMail.equals(snapshot.getKey())){
                     TextView sign = view.findViewById(R.id.sign_id);
                    sign.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), main_login.class);
                startActivity(intent);
            }
        });
        final int[] count = {0};
        final int[] quant = {0};
        final int[] quant1 = {0};
        final int[] quant2 = {0};
        final int[] quant3 = {0};
        image_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot2) {
                for (final DataSnapshot t : snapshot2.getChildren()){
                    count[0] = count[0] +1;
                    quant1[0] = quant1[0] + Integer.parseInt(t.child("quantity").getValue().toString());
                    if (t.child("status").getValue().toString().equals("TO BE CALL")||t.child("status").equals("PENDING")){
                        quant[0] =quant[0]+ Integer.parseInt(t.child("quantity").getValue().toString());

                    }
                    else if (t.child("status").getValue().toString().equals("ACCEPTED")){
                        quant2[0] =quant2[0]+ Integer.parseInt(t.child("quantity").getValue().toString());

                    }
                     else if (t.child("status").getValue().toString().equals("REJECTED")){
                        quant3[0] =quant3[0]+ Integer.parseInt(t.child("quantity").getValue().toString());

                    }


                }
                total_draw.setText(String.valueOf(count[0]));
                total_quant.setText(String.valueOf((quant1[0])));
                acc.setText(String.valueOf((quant2[0])));
                rej.setText(String.valueOf((quant3[0])));
                pending.setText(String.valueOf((quant[0])));

            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        Calendar cal = Calendar.getInstance();
        final int mnth = cal.get(Calendar.MONTH)+1;

        final int year= cal.get(Calendar.YEAR);
        final String[] cat = {""};
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat[0] =spin_choices[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        show_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cat[0].equals("Make Choice")){
                    Toast.makeText(getContext(), "Choose Category", Toast.LENGTH_SHORT).show();
                }

                else{
                    ref.child(String.valueOf(year)).child(String.valueOf(mnth)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            snapshot =  snapshot.child(cat[0]);
                            final ArrayList<monthly_model> objects = new ArrayList<monthly_model>();
                            for (final DataSnapshot s: snapshot.getChildren()){
                                if (s.child("date").getValue().toString().equals(formattedDate)){
                                    image_ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                            for (DataSnapshot i: snapshot1.getChildren()){

                                                if (i.getKey().equals(s.child("drawing").getValue().toString())){
                                                    String u;
                                                    try {
                                                        u =i.child("image_url").child("murl").getValue().toString();
                                                    }catch (Exception e){
                                                        u = null;
                                                    }

                                                    objects.add(new monthly_model(s.child("drawing").getValue().toString(),s.child("quantity").getValue().toString()+" "+ s.child("status").getValue().toString()
                                                            ,s.child("date").getValue().toString(),s.child("inspector").getValue().toString(),u));

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


                }

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
                    if (objects.get(position).getImage_url() == null){
                        Toast.makeText(getActivity(), "Image Does not Exist", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse(objects.get(position).getImage_url()));

                        startActivity(browser);
                    }

                }
            });
            holder.textView3.setText(objects.get(position).getDate());
            holder.textView4.setText(objects.get(position).getInspector());
            return convertView;

        }
    }
}
