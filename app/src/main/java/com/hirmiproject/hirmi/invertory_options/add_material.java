package com.hirmiproject.hirmi.invertory_options;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class add_material extends AppCompatActivity {

    ImageView calen;
    TextView date, new_material,add_grp ;
    Spinner material,group;
    EditText uom,today,stock,monthly_target,asking_rate;
     String mYear, mMonth, mDay;
     Button submit ;
     String str_date ;
     int month_of ,year_of;
     String material_choosed, group_choosed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("inventory");

        group = findViewById(R.id.group_id);
        uom = findViewById(R.id.uom_id);
        today = findViewById(R.id.today_id);
        add_grp  = findViewById(R.id.add_grp_id);
        stock = findViewById(R.id.stock_id);
        monthly_target = findViewById(R.id.monthly_target);
        asking_rate = findViewById(R.id.asking_rate_id);
        submit = findViewById(R.id.submit_id);

        date = findViewById(R.id.c);
        calen = findViewById(R.id.calen_id);
        calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = String.valueOf(c.get(Calendar.YEAR));
                mMonth = String.valueOf(c.get(Calendar.MONTH));
                mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));


                DatePickerDialog datePickerDialog = new DatePickerDialog(add_material.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                str_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                month_of = monthOfYear+1;
                                year_of = year;
                                date.setVisibility(View.VISIBLE);
                                date.setText(str_date);
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        final List<String> mat = new ArrayList<String>();
                                        mat.add("Choose Material");
                                        for (DataSnapshot s : snapshot.getChildren()){
                                            String m = s.getKey();
                                            mat.add(m);
                                        }
                                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(add_material.this, android.R.layout.simple_spinner_item, mat);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        material.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                        material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                material_choosed = mat.get(i);
                                                final List<String> grp = new ArrayList<>();
                                                reference.child(material_choosed).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                        grp.add("Choose Group");
                                                        for (DataSnapshot sn : snapshot.getChildren()){
                                                            grp.add(sn.getKey());

                                                        }
                                                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(add_material.this,android.R.layout.simple_spinner_item,grp );
                                                        group.setAdapter(adapter1);

                                                        adapter1.notifyDataSetChanged();
                                                        group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                group_choosed = grp.get(i);
                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                                            }
                                                        });

                                                        add_grp.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                final Dialog dialog = new Dialog(add_material.this);
                                                                dialog.setContentView(R.layout.add_new_material_dialog);
                                                                dialog.setCanceledOnTouchOutside(true);
                                                                dialog.setTitle("Enter Material");
                                                                dialog.show();
                                                                final EditText mate = dialog.findViewById(R.id.editText6);
                                                                Button save= dialog.findViewById(R.id.savedialogid);

                                                                save.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        if (mate.getText().toString().equals(null)){
                                                                            Toast.makeText(add_material.this, "Add Group Name", Toast.LENGTH_SHORT).show();
                                                                        }else{
                                                                            //reference.child(mate.getText().toString()).child("group").setValue("");

                                                                            if (material_choosed.equals("Choose Material")){
                                                                                Toast.makeText(add_material.this, "Choose Material", Toast.LENGTH_SHORT).show();

                                                                            }else{
                                                                                reference.child(material_choosed).child(mate.getText().toString()).child("month").setValue("");
                                                                                reference.child(material_choosed).child(mate.getText().toString()).child("uom").setValue("");
                                                                                reference.child(material_choosed).child(mate.getText().toString()).child("mtd").setValue(0);
                                                                                reference.child(material_choosed).child(mate.getText().toString()).child("ytd").setValue(0);
                                                                                reference.child(material_choosed).child(mate.getText().toString()).child("year").setValue(year_of);
                                                                            }

                                                                            dialog.dismiss();
                                                                        }

                                                                    }
                                                                });

                                                            }
                                                        });



                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }


                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        }, Integer.parseInt(mYear), Integer.parseInt(mMonth), Integer.parseInt(mDay));
                datePickerDialog.show();
            }
        });




        material =  findViewById(R.id.material_spin_id);


        new_material = findViewById(R.id.new_material_id);
        new_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(add_material.this);
                dialog.setContentView(R.layout.add_new_material_dialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setTitle("Enter Material");
                dialog.show();
                final EditText mate = dialog.findViewById(R.id.editText6);
                Button save= dialog.findViewById(R.id.savedialogid);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mate.getText().toString().equals(null)){
                            Toast.makeText(add_material.this, "Add Material Name", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            // reference.child(mate.getText().toString()).child("group").setValue("");
                            Toast.makeText(add_material.this, "Choose Group", Toast.LENGTH_SHORT).show();
                            //reference.child(mate.getText().toString()).setValue(0);
                            reference.child(mate.getText().toString()).child("Received").child("month").setValue(month_of);
                            reference.child(mate.getText().toString()).child("Received").child("mtd").setValue(0);
                            reference.child(mate.getText().toString()).child("Received").child("ytd").setValue(0);
                            reference.child(mate.getText().toString()).child("Received").child("year").setValue(year_of);
                            dialog.dismiss();
                        }

                    }
                });

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (material_choosed.equals("Choose Material")){
                    Toast.makeText(add_material.this, "Choose Material", Toast.LENGTH_SHORT).show();

                }if (group_choosed.equals("Choose Group")){
                    Toast.makeText(add_material.this, "Choose Group", Toast.LENGTH_SHORT).show();
                }
                else{
                    final float val = Float.parseFloat(today.getText().toString());


                    reference.child(material_choosed).child(group_choosed).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            if (snapshot.child("month").getValue().toString().equals(String.valueOf((month_of)))){
                                String mtd = Objects.requireNonNull(snapshot.child("mtd").getValue().toString());

                                float mtd_val = (Float.parseFloat(mtd)+val);
                                reference.child(material_choosed).child(group_choosed).child("mtd").setValue(mtd_val);

                            }else{
                                reference.child(material_choosed).child(group_choosed).child("mtd").setValue(0);

                            }if (snapshot.child("year").getValue().toString().equals(String.valueOf(year_of)) ){
                                String ytd= Objects.requireNonNull(snapshot.child("ytd").getValue().toString());
                                float ytd_val =Float.parseFloat(ytd)+val;
                                reference.child(material_choosed).child(group_choosed).child("ytd").setValue(ytd_val);
                            }else{
                                reference.child(material_choosed).child(group_choosed).child("ytd").setValue(0);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                   // reference.child(mYear).child(mMonth).child(material_choosed).child("group").setValue(group.getText().toString());
                    reference.child(material_choosed).child(group_choosed).child("uom").setValue(uom.getText().toString());

                    reference.child(material_choosed).child(group_choosed).child("today").setValue(val);
                    reference.child(material_choosed).child(group_choosed).child("monthly_target").setValue(monthly_target.getText().toString());
                    reference.child(material_choosed).child(group_choosed).child("asking_rate").setValue(asking_rate.getText().toString());
                    reference.child(material_choosed).child(group_choosed).child("date").setValue(str_date);
                    reference.child(material_choosed).child(group_choosed).child("month").setValue(String.valueOf((Integer.parseInt(mMonth)+1)));
                    reference.child(material_choosed).child(group_choosed).child("year").setValue(mYear);
                    reference.child(material_choosed).child(group_choosed).child("stock").setValue(stock.getText().toString());
                    startActivity(new Intent(add_material.this,add_material.class));


                }




            }
        });




    }
}