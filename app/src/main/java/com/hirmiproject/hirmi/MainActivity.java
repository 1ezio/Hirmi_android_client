package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Animation openanim;
    private Animation closeanim;
    private Animation frombottom ;
    private Animation tobottom;
    private FloatingActionButton add_btn ;

    private EditText draw, quantity;
    Button proceed;

    private boolean clicked = false;
    private TextView add_ins;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EditTExt Declarations
        draw = findViewById(R.id.draw_id);

        quantity = findViewById(R.id.q_id);

        final Spinner spinner = findViewById(R.id.spinner_id);
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference r = data.getReference("inspector");

        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> n = new ArrayList<String>();
                n.add("Choose Inspector Name");
                for (DataSnapshot s : snapshot.getChildren()){
                    n.add(s.child("name").getValue().toString());
                    ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, n);
                    addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(addressAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        proceed = findViewById(R.id.proc_id);



        //PROCEED BUTTON
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DATE
                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                final String formattedDate = df.format(c);

                //FIREBASE
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
                final DatabaseReference work = database.getReference("item");


                DatabaseReference ref = database.getReference("inspector");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
                        DatabaseReference work = database.getReference("item");
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            String n =spinner.getSelectedItem().toString();
                            if (n.equals(ds.child("name").getValue().toString())){

                                work.child(draw.getText().toString()).child("drawing_no").setValue(draw.getText().toString());
                                work.child(draw.getText().toString()).child("quantity").setValue(quantity.getText().toString());
                                work.child(draw.getText().toString()).child("inspector_name").setValue(n);
                                work.child(draw.getText().toString()).child("date").setValue(formattedDate);
                                work.child(draw.getText().toString()).child("status").setValue("TO BE CALL");
                                work.child(draw.getText().toString()).child("phone").setValue(ds.child("phn").getValue().toString());
                                work.child(draw.getText().toString()).child("i_token").setValue(ds.child("i_token").getValue().toString());
                                Toast.makeText(MainActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,MainActivity.class));

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });




        //ADD BUTTONS




    }



}