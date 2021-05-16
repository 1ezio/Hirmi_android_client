package com.hirmiproject.hirmi.invertory_options;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hirmiproject.hirmi.AdminMainScreenActivity;
import com.hirmiproject.hirmi.R;
import com.hirmiproject.hirmi.inventory;

public class adding_new extends AppCompatActivity {

    EditText material, parameter,value;
    TextView textView;
    Button done, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_new);

        material = findViewById(R.id.editText);
        parameter = findViewById(R.id.editText2);
        value = findViewById(R.id.editText3);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("inventory");

        save = findViewById(R.id.save_id);

        textView = findViewById(R.id.textView3);
        done = findViewById(R.id.finish_id);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (parameter.getText().toString().equals("") || value.getText().toString().equals("")){
                    Toast.makeText(adding_new.this, "Add Parameter OR Value", Toast.LENGTH_LONG).show();
                }
                else{
                    ref.child(material.getText().toString()).child(parameter.getText().toString()).setValue(value.getText().toString());
                    parameter.setText(null);
                    value.setText(null);
                    Toast.makeText(adding_new.this, "DATA UPLOADED", Toast.LENGTH_LONG).show();
                }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parameter.setText(null);
                value.setText(null);

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adding_new.this, inventory.class));
            }
        });

    }
}