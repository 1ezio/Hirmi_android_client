package com.hirmiproject.hirmi.ui.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.MainActivity;
import com.hirmiproject.hirmi.R;
import com.hirmiproject.hirmi.fragmentsCustodian.history_fragment2;
import com.hirmiproject.hirmi.model_history_inspector;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.itextpdf.text.Annotation.FILE;

public class monthly_report_fragment extends Fragment {

    private String[] mMonth = new String[] {
            "Jan", "Feb" , "Mar", "Apr", "May", "Jun",
            "Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View  view  = inflater.inflate(R.layout.monthly_report_fragment, container, false);

        final LinearLayout linearLayout = view.findViewById(R.id.expandable_id);
        ImageView image = view.findViewById(R.id.arrow_id);
        final CardView card = view.findViewById(R.id.card_id);
        final TextView total_draw = view.findViewById(R.id.total_draw_id);
        final TextView total_quant= view.findViewById(R.id.total_quant_id);
        final TextView acc= view.findViewById(R.id.acc_id);
        final TextView rej= view.findViewById(R.id.reject_id);
        final TextView pending= view.findViewById(R.id.pending_id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("report");
        final DatabaseReference image_ref = database.getReference("item");

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearLayout.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(card,new AutoTransition());
                    linearLayout.setVisibility(View.VISIBLE);
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


                }else{
                    TransitionManager.beginDelayedTransition(card,new AutoTransition());
                    linearLayout.setVisibility(View.GONE);

                }
            }
        });

        final ListView lv = view.findViewById(R.id.lv_id);

        Spinner mnth_spin = view.findViewById(R.id.mnth_spin_id);
        Spinner year_spin = view.findViewById(R.id.year_spin_id);
        Spinner cate = view.findViewById(R.id.spin_id);
        final String[] spin_choices = { "Make Choice", "Fitment", "Welding"};
        final ArrayAdapter aa
                = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                spin_choices);
        aa.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        cate.setAdapter(aa);
        Button show = view.findViewById(R.id.done_id);

        final ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 2020; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item    , years);
        year_spin.setAdapter(adapter);

        final ArrayList<String> mnths = new ArrayList<String>();
        mnths.add("Month");
        mnths.add("January");
        mnths.add("February");mnths.add("March");mnths.add("April");mnths.add("May");mnths.add("June");mnths.add("July");mnths.add("August");mnths.add("September");mnths.add("October");
        mnths.add("November");mnths.add("December");
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,mnths);
        mnth_spin.setAdapter(ad);
        final String[] final_mnth = new String[1];
        final String[] mnth_name = new String[1];

        mnth_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                final_mnth[0] = String.valueOf(i);
                mnth_name[0] = String.valueOf(mnths.get(i));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final String[] final_year = new String[1];


        year_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final_year[0] = years.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final String[] choice = new String[1];


        cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choice[0] = spin_choices[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot =  snapshot.child(final_year[0]).child(String.valueOf(final_mnth[0])).child(choice[0]);
                        final ArrayList<monthly_model> objects = new ArrayList<monthly_model>();
                        for (final DataSnapshot s: snapshot.getChildren()){

                            image_ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {

                                    for (DataSnapshot i:snapshot1.getChildren()){
                                        if (i.getKey().equals(s.child("drawing").getValue().toString())){
                                            String u,d=null;
                                            d = i.child("d").getValue().toString();
                                            try {
                                                u = i.child("image_url").child("murl").getValue().toString();

                                            }catch (Exception e){
                                                u = null;
                                            }
                                            objects.add(new monthly_model(s.child("drawing").getValue().toString(),s.child("quantity").getValue().toString()+" "+ s.child("status").getValue().toString()
                                                    ,s.child("date").getValue().toString(),s.child("inspector").getValue().toString(),u,d));

                                            CustomAdapter customAdapter = new CustomAdapter(getContext(),objects);
                                            lv.setAdapter(customAdapter);
                                            customAdapter.notifyDataSetChanged();

                                        }
                                        //.child("image_url").child("murl").getValue().toString();

                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });
            }
        });






        //final DatePickerDialog.OnDateSetListener finalDateSetListener = dateSetListener;
        return view;
    }









    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<monthly_model> objects;

        private class ViewHolder {
            TextView textView1;
            TextView textView2, textView3, textView4,textView5,textView6;

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
                holder.textView6 = convertView.findViewById(R.id.d_id);

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
            holder.textView6.setText(objects.get(position).getD());
            return convertView;

        }
    }
}
