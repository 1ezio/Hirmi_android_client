package com.hirmiproject.hirmi.invertory_options;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.R;
import com.hirmiproject.hirmi.inventory;

import java.util.ArrayList;
import java.util.List;

public class adding_existing extends AppCompatActivity {
    Spinner m_spin, p_spin;
    EditText value ;
    TextView add_new;
    Button save ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_existing);

        m_spin  = findViewById(R.id.material_spin_id);
        p_spin = findViewById(R.id.p_spin_id);
        value = findViewById(R.id.value_id);

        add_new = findViewById(R.id.some_id);

        save = findViewById(R.id.save_id);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("inventory");

        final List<String> materials = new ArrayList<>();
        materials.add("Choose Material");





        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()){
                    materials.add(s.getKey());
                    ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(adding_existing.this, android.R.layout.simple_spinner_item, materials);
                    materialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    m_spin.setAdapter(materialAdapter);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final String[] m = new String[1];
        final List<String> paramter = new ArrayList<>();
        paramter.add("Choose Parameter");

        m_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               m[0] = materials.get(i);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot s : snapshot.child(m[0]).getChildren()){
                            paramter.add(s.getKey());
                            ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(adding_existing.this, android.R.layout.simple_spinner_item, paramter);
                            materialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            p_spin.setAdapter(materialAdapter);



                        }
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

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(adding_existing.this);
                dialog.setContentView(R.layout.new_parameter_dialog);
                if (m[0].equals("Choose Material")){
                    Toast.makeText(adding_existing.this, "Select a Material", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.show();
                    final EditText p_name= dialog.findViewById(R.id.editText4);
                    final EditText p_value= dialog.findViewById(R.id.editText5);
                    TextView add_more = dialog.findViewById(R.id.textView5);
                    Button  save1= dialog.findViewById(R.id.button2);
                    Button done = dialog.findViewById(R.id.done_id);

                    add_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            p_name.setText("");
                            p_value.setText("");
                        }
                    });
                    save1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (p_name.getText().toString().equals("") || p_value.getText().toString().equals("")){
                                Toast.makeText(adding_existing.this, "Enter Parameter or Value..", Toast.LENGTH_SHORT).show();
                            }else{
                                ref.child(m[0]).child(p_name.getText().toString()).setValue(p_value.getText().toString());
                                p_name.setText("");
                                p_value.setText("");

                            }

                        }
                    });
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            startActivity(new Intent(adding_existing.this,adding_existing.class));
                        }
                    });



                }

            }
        });


        final String[] p = new String[1];
        p_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                p[0] = paramter.get(i);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



      /*  .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final
                ndialog.setTitle("MAKE A CHOICE");


                final EditText p_name= ndialog.findViewById(R.id.editText4);
                final EditText p_value= ndialog.findViewById(R.id.editText5);
                TextView add_more = ndialog.findViewById(R.id.textView5);
                Button  save1= ndialog.findViewById(R.id.button2);
                Button done = ndialog.findViewById(R.id.done_id);

                add_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        p_name.setText("");
                        p_value.setText("");
                    }
                });
                save1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (p_name.getText().toString().equals("") || p_value.getText().toString().equals("")){
                            Toast.makeText(adding_existing.this, "Enter Parameter or Value..", Toast.LENGTH_SHORT).show();
                        }else{
                            ref.child(m[0]).child(p_name.getText().toString()).setValue(p_value.getText().toString());

                        }

                    }
                });



            }
        });*/



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (value.getText().toString().equals("")){
                    Toast.makeText(adding_existing.this, "Add Some Value", Toast.LENGTH_SHORT).show();
                }else{
                    ref.child(m[0]).child(p[0]).setValue(value.getText().toString());


                }
            }
        });












    }
}