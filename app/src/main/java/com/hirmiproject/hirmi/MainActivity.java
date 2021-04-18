package com.hirmiproject.hirmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Animation openanim;
    private Animation closeanim;
    private Animation frombottom ;
    private Animation tobottom;
    private FloatingActionButton add_btn ;

    private boolean clicked = false;
    private TextView add_ins;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openanim =  AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        closeanim=AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        frombottom=AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        tobottom= AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);



        add_ins = findViewById(R.id.add_ins);

        add_btn = findViewById(R.id.fab_btn);


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onbuttonclicked();

            }
        });

        add_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,User_regis.class);
                startActivity(i);

            }
        });
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