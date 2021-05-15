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
    CardView employee, inven;
    TextView admin_name;
    FirebaseDatabase database;
    TextView signout ;
    FirebaseAuth mauth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);
        database = FirebaseDatabase.getInstance();
        FirebaseUser user1=FirebaseAuth.getInstance().getCurrentUser();
        mauth = FirebaseAuth.getInstance();
        admin_name = findViewById(R.id.admin_name);
        String user = mauth.getCurrentUser().getEmail();
        user = user.replace(".",",");
        signout = findViewById(R.id.signout_id);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminMainScreenActivity.this,main_login.class);
                startActivity(intent);

            }
        });

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
                Intent intent = new Intent(AdminMainScreenActivity.this, report.class);
                startActivity(intent);


            }
        });

        employee = findViewById(R.id.cv_employees);
        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainScreenActivity.this, employee_layout.class);
                startActivity(intent);

            }
        });
        inven = findViewById(R.id.inventory_id);
        inven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainScreenActivity.this, inventory.class);
                startActivity(intent);

            }
        });

    }
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    public void add_new_role(View view) {
        startActivity(new Intent(AdminMainScreenActivity.this,User_regis.class));
    }

    public void ad_drawring(View view) {
        Intent intent = new Intent(AdminMainScreenActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void monthly_report(View view) {
        Intent intent = new Intent(AdminMainScreenActivity.this, report.class);
        startActivity(intent);


    }

    public void emp(View view) {
        Intent intent = new Intent(AdminMainScreenActivity.this, employee_layout.class);
        startActivity(intent);

    }

    public void iknventory(View view) {
        Intent intent = new Intent(AdminMainScreenActivity.this, inventory.class);
        startActivity(intent);
    }
}