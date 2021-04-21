package com.hirmiproject.hirmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

public class AdminMainScreenActivity extends AppCompatActivity {

    CardView addNewRoles;
    CardView addDrawings;
    CardView monthlyReport;
    CardView employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);

        addNewRoles = findViewById(R.id.cv_add_new_roles);
        addNewRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addDrawings = findViewById(R.id.cv_add_drawings);
        addDrawings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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