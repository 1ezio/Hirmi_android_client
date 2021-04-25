package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Random;

public class User_regis extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] workers= { "Make Choice","Custodian", "Inspector"};
    Button proceed;
    String cate;
    FirebaseAuth firebaseAuth  ;

    EditText name , phn, reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reg_final);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        //EditText Declaration
        name = findViewById(R.id.name_id);

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

        firebaseAuth = FirebaseAuth.getInstance();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cate.equals("Custodian")){
                    final DatabaseReference creference = database.getReference("custodian");

                    //TYPE GENERATE PASSWORD HERE
                    //AND SEND MAIL 
                    String s =  encodeString(reg.getText().toString());
                    creference.child(s).child("reg_id").setValue(s);
                    creference.child(s).child("name").setValue(name.getText().toString());
                    creference.child(s).child("phn").setValue(phn.getText().toString());

                    firebaseAuth.createUserWithEmailAndPassword(reg.getText().toString(),(phn.getText().toString())).addOnCompleteListener(User_regis.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(User_regis.this, "REGISTERD", Toast.LENGTH_SHORT).show();
                            }
                                                    }
                    });




                }if (cate.equals("Inspector")){
                    final DatabaseReference ireference = database.getReference("inspector");
                    String s = encodeString(reg.getText().toString());
                    ireference.child(s).child("reg_id").setValue(s);
                    ireference.child(s).child("name").setValue(name.getText().toString());
                    ireference.child(s).child("phn").setValue(phn.getText().toString());







                    Random rnd = new Random();

                    int n = 100000 + rnd.nextInt(900000);

                    firebaseAuth.createUserWithEmailAndPassword(reg.getText().toString(), String.valueOf(n)).addOnCompleteListener(User_regis.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(User_regis.this, "REGISTERD", Toast.LENGTH_SHORT).show();
                            }
                            else if (task.isCanceled()){
                                Toast.makeText(User_regis.this, "CANCELLED", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }

                if (cate.equals("Make Choice")){
                    final DatabaseReference ireference = database.getReference("admin");
                    String s = encodeString(reg.getText().toString());
                    ireference.child(s).child("reg_id").setValue(s);
                    ireference.child(s).child("name").setValue(name.getText().toString());
                    ireference.child(s).child("phn").setValue(phn.getText().toString());
                    Random rnd = new Random();



                    firebaseAuth.createUserWithEmailAndPassword(reg.getText().toString(), (phn.getText().toString())).addOnCompleteListener(User_regis.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(User_regis.this, "REGISTERD", Toast.LENGTH_SHORT).show();
                            }
                            else if (task.isCanceled()){
                                Toast.makeText(User_regis.this, "CANCELLED", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }



            }

            private String encodeString(String string) {
                return string.replace(".", ",");
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}