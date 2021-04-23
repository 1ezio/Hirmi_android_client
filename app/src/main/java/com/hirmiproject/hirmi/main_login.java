package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class main_login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        final EditText email_text, pass_text;
        Button login;
        TextView forgot_password ;
        email_text = (EditText) findViewById(R.id.email_id);
        pass_text = (EditText) findViewById(R.id.pass_id);
        login = findViewById(R.id.login_id);
        forgot_password = findViewById(R.id.forgot_id);

        firebaseAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_text.getText().toString().trim();
                String password = pass_text.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(main_login.this, "Please Enter E-MAIL", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(main_login.this, "Please Enter PASSWORD", Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(main_login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(main_login.this, "LOGIN", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(main_login.this, "FAILED", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText sendmail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter E-mail");
                passwordResetDialog.setView(sendmail);

                passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = sendmail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(main_login.this, "LINK SENT ON E-MAIL. PLEASE CHECK", Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(main_login.this, "ERROR !"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });


    }
}