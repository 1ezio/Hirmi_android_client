 package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminMainScreenActivity extends AppCompatActivity {

    CardView addNewRoles;
    CardView addDrawings;
    CardView monthlyReport;
    CardView employee;
    TextView admin_name;
    FirebaseDatabase database;
    FirebaseAuth mauth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);
        database = FirebaseDatabase.getInstance();
        mauth = FirebaseAuth.getInstance();
        admin_name = findViewById(R.id.admin_name);
        String user = mauth.getCurrentUser().getEmail();
        user = user.replace(".",",");

        DatabaseReference name = database.getReference("admin");
        final String finalUser = user;
        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (finalUser.equals(dataSnapshot.getKey())){
                        admin_name.setText(dataSnapshot.child("name").getValue().toString());


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addNewRoles = findViewById(R.id.cv_add_new_roles);
        addNewRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainScreenActivity.this,User_regis.class);
                startActivity(intent);


            }
        });

        addDrawings = findViewById(R.id.cv_add_drawings);
        addDrawings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainScreenActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

        monthlyReport = findViewById(R.id.cv_monthly_report);
        monthlyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        employee = findViewById(R.id.cv_employees);
        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}