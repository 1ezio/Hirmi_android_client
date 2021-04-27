package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class main_login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth ;
    private FirebaseDatabase database ;
    private EditText email_text;
    private EditText pass_text;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




        final CustomProgress progress = new CustomProgress(main_login.this);


        TextView forgot_password ;
        email_text = findViewById(R.id.email_id);
        pass_text = findViewById(R.id.pass_id);
        login = findViewById(R.id.login_id);
        forgot_password = findViewById(R.id.forgot_id);

        email_text.addTextChangedListener(loginTextWatcher);
        pass_text.addTextChangedListener(loginTextWatcher);



        database = FirebaseDatabase.getInstance();
        final DatabaseReference a_reference = database.getReference("admin") ;
        final DatabaseReference c_reference = database.getReference("custodian") ;
        final DatabaseReference i_reference = database.getReference("inspector") ;






        firebaseAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                final String email = email_text.getText().toString().trim();
                String password = pass_text.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(main_login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                   a_reference.addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                                           for(DataSnapshot snapshot1:snapshot.getChildren()){
                                               String key = snapshot1.getKey();
                                               key = key.replace(",", ".");
                                               if (key.equals(email)){
                                                   Intent intent = new Intent(main_login.this,AdminMainScreenActivity.class);
                                                   startActivity(intent);
                                                   progress.dismiss();
                                               }else{
                                                   c_reference.addValueEventListener(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot c_snapshot) {
                                                           for(DataSnapshot c_snapshot1:c_snapshot.getChildren()) {
                                                               String key = c_snapshot1.getKey();
                                                               key = key.replace(",", ".");
                                                               if (key.equals(email)) {
                                                                   final String finalKey = key;
                                                                   FirebaseInstanceId.getInstance().getInstanceId()
                                                                           .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                                               @Override
                                                                               public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                                   if (!task.isSuccessful()) {

                                                                                       return;
                                                                                   }

                                                                                   // Get new Instance ID token
                                                                                   String token = task.getResult().getToken();
                                                                                   String e = email.replace(".",",");
                                                                                   c_reference.child(e).child("c_token").setValue(token);
                                                                               }
                                                                           });




                                                                   Intent intent = new Intent(main_login.this, MainActivityNew.class);
                                                                   startActivity(intent);
                                                                   progress.dismiss();
                                                               }else{

                                                                   i_reference.addValueEventListener(new ValueEventListener() {
                                                                       @Override
                                                                       public void onDataChange(@NonNull DataSnapshot i_snapshot) {
                                                                           for(DataSnapshot i_snapshot1:i_snapshot.getChildren()) {
                                                                               String key = i_snapshot1.getKey();
                                                                               key = key.replace(",", ".");
                                                                               if (key.equals(email)) {

                                                                                   final String finalKey = key;
                                                                                   FirebaseInstanceId.getInstance().getInstanceId()
                                                                                           .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                                                               @Override
                                                                                               public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                                                   if (!task.isSuccessful()) {

                                                                                                       return;
                                                                                                   }

                                                                                                   // Get new Instance ID token
                                                                                                   String token = task.getResult().getToken();
                                                                                                   String e = email.replace(".",",");
                                                                                                   i_reference.child(e).child("i_token").setValue(token);
                                                                                               }
                                                                                           });




                                                                                   Intent intent = new Intent(main_login.this, Ispector_layout.class);
                                                                                   startActivity(intent);
                                                                                   progress.dismiss();
                                                                               }
                                                                           }
                                                                       }

                                                                       @Override
                                                                       public void onCancelled(@NonNull DatabaseError error) {

                                                                       }
                                                                   });

                                                               }
                                                           }
                                                       }

                                                       @Override
                                                       public void onCancelled(@NonNull DatabaseError error) {

                                                       }
                                                   });
                                               }
                                           }

                                       }


                                           @Override
                                       public void onCancelled(@NonNull DatabaseError error) {

                                       }
                                   });






                                } else {
                                    Toast.makeText(main_login.this, "FAILED", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
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
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String email = email_text.getText().toString().trim();
            String password = pass_text.getText().toString().trim();

            login.setEnabled(!email.isEmpty() && !password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(main_login.this,main_login.class);
        startActivity(intent);
        super.onBackPressed();
    }
}