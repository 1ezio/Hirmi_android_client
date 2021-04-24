package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Animation openanim;
    private Animation closeanim;
    private Animation frombottom ;
    private Animation tobottom;
    private FloatingActionButton add_btn ;

    private EditText draw, i_name, quantity;
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
        i_name = findViewById(R.id.ins_id);
        quantity = findViewById(R.id.q_id);


        proceed = findViewById(R.id.proc_id);



        //ANIMATIONS
        openanim =  AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        closeanim=AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        frombottom=AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        tobottom= AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);


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
                            String n =i_name.getText().toString();
                            if (n.equals(ds.child("name").getValue().toString())){

                                work.child(draw.getText().toString()).child("drawing_no").setValue(draw.getText().toString());
                                work.child(draw.getText().toString()).child("quantity").setValue(quantity.getText().toString());
                                work.child(draw.getText().toString()).child("inspector_name").setValue(i_name.getText().toString());
                                work.child(draw.getText().toString()).child("date").setValue(formattedDate);
                                work.child(draw.getText().toString()).child("status").setValue("pending");
                                work.child(draw.getText().toString()).child("phone").setValue(ds.child("phn").getValue().toString());


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


    private void onbuttonclicked() {
        setvisibility(clicked);
        setanim(clicked);
        if(!clicked){
            clicked = true;
        }else{
            clicked= false;
        }

                
    }

    private void setvisibility(Boolean clicked) {
        if(!clicked){
            add_ins.setVisibility(View.VISIBLE);

        }else{
            add_ins.setVisibility(View.INVISIBLE);

        }


    }

    private void setanim(Boolean clicked) {
        if(!clicked){
            add_ins.startAnimation(frombottom);

            add_btn.startAnimation(openanim);

        }else{add_ins.startAnimation(tobottom);

            add_btn.startAnimation(closeanim);

        }
     }
}