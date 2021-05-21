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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.invertory_options.adding_existing;
import com.hirmiproject.hirmi.invertory_options.adding_new;

import java.util.ArrayList;
import java.util.List;

public class inventory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        final Button report  = findViewById(R.id.report_id);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(inventory.this,report.class));
            }
        });

        final ListView listView = findViewById(R.id.list_id);

        ImageView sign= findViewById(R.id.sign_id);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(inventory.this,main_login.class));
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("inventory");

        final Spinner spinner = findViewById(R.id.spinner_id);

        Button add = findViewById(R.id.add_id);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(inventory.this);
                dialog.setTitle("MAKE A CHOICE");
                dialog.setContentView(R.layout.inventory_dialog);
                final Button add_new = dialog.findViewById(R.id.new_id);

                final Button add_existing = dialog.findViewById(R.id.existing_id);

                dialog.show();
                add_new.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(inventory.this, adding_new.class));
                    }
                });

                add_existing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(inventory.this, adding_existing.class));
                        dialog.dismiss();
                    }
                });
            }
        });



        final List<String> materials = new ArrayList<String>();
        materials.add("Choose Material");




        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()){
                    materials.add(s.getKey());
                    ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(inventory.this, android.R.layout.simple_spinner_item, materials);
                    addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(addressAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String material = materials.get(i);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot = snapshot.child(material);
                        List<String> parameter = new ArrayList<String>();
                        for (DataSnapshot d : snapshot.getChildren()){
                            String key = d.getKey();
                            parameter.add(key + " : "+d.getValue().toString() );

                        }
                        ArrayAdapter adapter = new ArrayAdapter(inventory.this, android.R.layout.simple_list_item_1,parameter);
                        listView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }
}