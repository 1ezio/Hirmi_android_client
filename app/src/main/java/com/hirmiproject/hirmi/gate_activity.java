package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class gate_activity extends AppCompatActivity {
    EditText total_value, entry_value;
    Spinner category_choosen;
    TextView add_cate, t,g,total_issue, total_entry, goel_total,goel_gate,hap_total,hap_gate,other_total,other_gate;
    Button save ;
    String cate_choosed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate);

        FirebaseAuth user = FirebaseAuth.getInstance();
         String mail = user.getCurrentUser().getEmail();
         mail=mail.replace(".",",");


        category_choosen = findViewById(R.id.cate_id);
        total_value= findViewById(R.id.total_value_id);
        entry_value = findViewById(R.id.entry_value_id);
        add_cate = findViewById(R.id.add_cate_id);

        t = findViewById(R.id.t_id);
        g = findViewById(R.id.g_id);

        goel_total= findViewById(R.id.ms_goel_id);
        goel_gate = findViewById(R.id.ms_gate_id);
        hap_total = findViewById(R.id.hap_total_id);

        hap_gate= findViewById(R.id.hap_gate_id);
        other_total= findViewById(R.id.other_id);
        other_gate = findViewById(R.id.other_gate_id);



        save = findViewById(R.id.save_id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("gate");

        final DatabaseReference mref = database.getReference("power_user");
        final String finalMail = mail;
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snap1 : snapshot.getChildren()){
                    if (snap1.getKey().equals(finalMail)){
                        entry_value.setVisibility(View.VISIBLE);
                        total_value.setVisibility(View.VISIBLE);
                        save.setVisibility(View.VISIBLE);
                        t.setVisibility(View.VISIBLE);
                        g.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        add_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(gate_activity.this);
                dialog.setContentView(R.layout.add_new_material_dialog);
                dialog.show();
                final EditText category = dialog.findViewById(R.id.editText6);
                Button save = dialog.findViewById(R.id.savedialogid);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String c = String.valueOf(category.getText());
                        c = c.replace("/",",");
                        ref.child(c).child("total_pass").setValue(0);
                        ref.child(c).child("gate_entry").setValue(0);
                        dialog.dismiss();
                        startActivity(new Intent(gate_activity.this,gate_activity.class));
                    }
                });


            }
        });

        final Spinner spinner = findViewById(R.id.cate_id);
        final int[] t = {0};
        final int[] e = {0};
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull final DataSnapshot snapshot) {
                final ArrayList<String> list = new ArrayList<>();
                list.add("Choose Category");
                for (DataSnapshot snap:snapshot.getChildren()){
                    String c = String.valueOf(snap.getKey());
                    c = c.replace(",","/");
                    list.add(c);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(gate_activity.this, android.R.layout.simple_list_item_1,list);
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                        cate_choosed =list.get(i);

                        for(DataSnapshot snap1: snapshot.getChildren()){
                            t[0] = t[0] + Integer.parseInt(snap1.child("total_pass").getValue().toString());
                            e[0] = e[0] +Integer.parseInt(snap1.child("gate_entry").getValue().toString());
                        }
                        total_issue= findViewById(R.id.total_issue_id);
                        total_entry = findViewById(R.id.entry_id);
                        total_issue.setText(String.valueOf(t[0]));
                        total_entry.setText(String.valueOf(e[0]));
                        for(DataSnapshot snap1: snapshot.getChildren()){
                           if (snap1.getKey().equals("M,s Goel")){
                               goel_total.setText(snap1.child("total_pass").getValue().toString());
                               goel_gate.setText(snap1.child("gate_entry").getValue().toString());

                           }
                            if (snap1.getKey().equals("M,s HAPBCO")){
                                hap_total.setText(snap1.child("total_pass").getValue().toString());
                                hap_gate.setText(snap1.child("gate_entry").getValue().toString());

                            }
                            if (snap1.getKey().equals("Others")){
                                other_total.setText(snap1.child("total_pass").getValue().toString());
                                other_gate.setText(snap1.child("gate_entry").getValue().toString());

                            }
                        }

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



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cate_choosed.equals("Choose Category")){
                    Toast.makeText(gate_activity.this, "Choose Category", Toast.LENGTH_LONG).show();
                }
                else{
                    String ca = cate_choosed;
                    ca = ca.replace("/",",");

                    ref.child(ca).child("total_pass").setValue(total_value.getText().toString());
                    ref.child(ca).child("gate_entry").setValue(entry_value.getText().toString());
                    Intent intent = new Intent(gate_activity.this,gate_activity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);

                }


            }
        });

    }
}