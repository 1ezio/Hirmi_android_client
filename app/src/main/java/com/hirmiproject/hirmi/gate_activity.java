package com.hirmiproject.hirmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class gate_activity extends AppCompatActivity {
    EditText total_value, entry_value;
    Spinner category_choosen;
    TextView add_cate, total_issue, total_entry;
    Button save ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate);

        category_choosen = findViewById(R.id.cate_id);
        total_value= findViewById(R.id.total_value_id);
        entry_value = findViewById(R.id.entry_value_id);
        add_cate = findViewById(R.id.add_cate_id);
        total_issue= findViewById(R.id.total_issue_id);
        total_entry = findViewById(R.id.entry_id);
        save = findViewById(R.id.save_id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();

        add_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(gate_activity.this);
                dialog.setContentView(R.layout.add_new_material_dialog);
                EditText category = dialog.findViewById(R.id.editText6);
                Button save = dialog.findViewById(R.id.savedialogid);
                ref.child(String.valueOf(category.getText())).child("total_pass").setValue(0);
                

            }
        });

    }
}