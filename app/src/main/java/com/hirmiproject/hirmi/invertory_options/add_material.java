package com.hirmiproject.hirmi.invertory_options;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
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

public class add_material extends AppCompatActivity {

    ImageView calen;
    TextView date, new_material ;
    Spinner material;
    EditText group,uom,today,stock,monthly_target,asking_rate;
     String mYear, mMonth, mDay;
     Button submit;
     String material_choosed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("inventory");

        group = findViewById(R.id.group_id);
        uom = findViewById(R.id.uom_id);
        today = findViewById(R.id.today_id);
        stock = findViewById(R.id.stock_id);
        monthly_target = findViewById(R.id.monthly_target);
        asking_rate = findViewById(R.id.asking_rate_id);
        submit = findViewById(R.id.submit_id);

        calen = findViewById(R.id.calen_id);
        date = findViewById(R.id.date_id);
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

                                date.setVisibility(View.VISIBLE);
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, Integer.parseInt(mYear), Integer.parseInt(mMonth), Integer.parseInt(mDay));
                datePickerDialog.show();
            }
        });

        material =  findViewById(R.id.material_spin_id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                final List<String> mat = new ArrayList<String>();
                for (DataSnapshot s : snapshot.getChildren()){
                    String m = s.getKey();
                    mat.add(m);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(add_material.this, android.R.layout.simple_spinner_item, mat);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                material.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        material_choosed = mat.get(i);
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

        new_material = findViewById(R.id.new_material_id);
        new_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(add_material.this);
                dialog.setContentView(R.layout.add_new_material_dialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                final EditText mate = dialog.findViewById(R.id.editText6);
                Button save= dialog.findViewById(R.id.savedialogid);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mate.getText().toString().equals(null)){
                            Toast.makeText(add_material.this, "Add Material Name", Toast.LENGTH_SHORT).show();
                        }else{
                            reference.child(mate.getText().toString()).child("group").setValue("");
                            dialog.dismiss();
                        }

                    }
                });

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }
}