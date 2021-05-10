package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
    String[] workers= { "Make Choice","Initiator", "Inspector", "Power User"};
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

        //validation
       name.addTextChangedListener(loginTextWatcher);
        phn.addTextChangedListener(loginTextWatcher);
        reg.addTextChangedListener(loginTextWatcher);




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

                if (TextUtils.isEmpty(reg.getText().toString())){
                    Toast.makeText(User_regis.this, "Enter E-mail", Toast.LENGTH_SHORT).show();
                }if (TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(User_regis.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(phn.getText().toString()) && (phn.getText().toString().length()<10 || phn.getText().toString().length()>10)){
                    Toast.makeText(User_regis.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else {

                    if (cate.equals("Initiator")) {
                        final DatabaseReference creference = database.getReference("custodian");

                        //TYPE GENERATE PASSWORD HERE
                        //AND SEND MAIL
                        String s = encodeString(reg.getText().toString());
                        creference.child(s).child("reg_id").setValue(s);
                        creference.child(s).child("name").setValue(name.getText().toString());
                        creference.child(s).child("phn").setValue(phn.getText().toString());

                        Random rnd = new Random();

                        int n = 100000 + rnd.nextInt(900000);

                        firebaseAuth.createUserWithEmailAndPassword(reg.getText().toString(), String.valueOf(n)).addOnCompleteListener(User_regis.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(User_regis.this, "REGISTERED", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(User_regis.this, User_regis.class);

                                    startActivity(intent);
                                } else if (task.isCanceled()) {
                                    Toast.makeText(User_regis.this, "CANCELLED", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                    if (cate.equals("Inspector")) {
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
                                if (task.isSuccessful()) {
                                    Toast.makeText(User_regis.this, "REGISTERED", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(User_regis.this, User_regis.class);

                                    startActivity(intent);
                                } else if (task.isCanceled()) {
                                    Toast.makeText(User_regis.this, "CANCELLED", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




                    }

                    if (cate.equals("Make Choice")) {
                        final DatabaseReference ireference = database.getReference("monitor");
                        String s = encodeString(reg.getText().toString());
                        ireference.child(s).child("reg_id").setValue(s);
                        ireference.child(s).child("name").setValue(name.getText().toString());
                        ireference.child(s).child("phn").setValue(phn.getText().toString());


                        Random rnd = new Random();

                        int n = 100000 + rnd.nextInt(900000);

                        firebaseAuth.createUserWithEmailAndPassword(reg.getText().toString(), String.valueOf(n)).addOnCompleteListener(User_regis.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(User_regis.this, "REGISTERED", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(User_regis.this, User_regis.class);

                                    startActivity(intent);
                                } else if (task.isCanceled()) {
                                    Toast.makeText(User_regis.this, "CANCELLED", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                    if (cate.equals("Power User")){

                            final DatabaseReference ireference = database.getReference("power_user");
                            String s = encodeString(reg.getText().toString());
                            ireference.child(s).child("reg_id").setValue(s);
                            ireference.child(s).child("name").setValue(name.getText().toString());
                            ireference.child(s).child("phn").setValue(phn.getText().toString());


                            Random rnd = new Random();

                            int n = 100000 + rnd.nextInt(900000);

                            firebaseAuth.createUserWithEmailAndPassword(reg.getText().toString(), String.valueOf(n)).addOnCompleteListener(User_regis.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(User_regis.this, "REGISTERED", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(User_regis.this, User_regis.class);

                                        startActivity(intent);
                                    } else if (task.isCanceled()) {
                                        Toast.makeText(User_regis.this, "CANCELLED", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                    }
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
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String email = name.getText().toString().trim();
            String password = phn.getText().toString().trim();
            String registration = reg.getText().toString().trim();

            proceed.setEnabled(!email.isEmpty() && !password.isEmpty() && !registration.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(User_regis.this,AdminMainScreenActivity.class));
        super.onBackPressed();
    }
}