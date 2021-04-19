package com.hirmiproject.hirmi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_regis extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] workers= { "Make Choice","Custodian", "Inspector"};
    Button proceed;
    String cate;
    EditText name , email , phn, reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_regis);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        //EditText Declaration
        name = findViewById(R.id.name_id);
        email = findViewById(R.id.e_id);
        phn = findViewById(R.id.mob_id);
        reg = findViewById(R.id.reg_id);


        proceed = findViewById(R.id.r_id);



        //FIREBASE IMPLEMENTATION

        final FirebaseDatabase database =FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");



        //SPINNER

        ArrayAdapter arrayAdapter  = new ArrayAdapter(this, android.R.layout.simple_spinner_item,workers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cate= workers[i].toString();
        final FirebaseDatabase database =FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");



        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cate.equals("Custodian")){
                    final DatabaseReference creference = database.getReference("custodian");

                    //TYPE GENERATE PASSWORD HERE
                    //AND SEND MAIL 

                    creference.child(reg.getText().toString()).child("reg_id").setValue(reg.getText().toString());
                    creference.child(reg.getText().toString()).child("name").setValue(name.getText().toString());
                    creference.child(reg.getText().toString()).child("phn").setValue(phn.getText().toString());
                    creference.child(reg.getText().toString()).child("email").setValue(email.getText().toString());

                }if (cate.equals("Inspector")){
                    final DatabaseReference ireference = database.getReference("inspector");
                    ireference.child(reg.getText().toString()).child("reg_id").setValue(reg.getText().toString());
                    ireference.child(reg.getText().toString()).child("name").setValue(name.getText().toString());
                    ireference.child(reg.getText().toString()).child("phn").setValue(phn.getText().toString());
                    ireference.child(reg.getText().toString()).child("email").setValue(email.getText().toString());

                }else{
                    Toast.makeText(User_regis.this, "Make Correct Choice", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}