package com.hirmiproject.hirmi.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.R;

public class inspector_dialog {
    public void   showDialog(Activity activity, final String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.inspector_dialog);

        final TextView drawing_no, inspector_name, quantity;
        final Button accept, reject;
        drawing_no = dialog.findViewById(R.id.idrawing_id);
        inspector_name = dialog.findViewById(R.id.i_inspector_id);
        quantity = dialog.findViewById(R.id.quantity_i_id);
        accept = dialog.findViewById(R.id.accept_id);
        reject = dialog.findViewById(R.id.reject_id);



        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
        final DatabaseReference items = database.getReference("item");
        items.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot ds : snapshot.getChildren()) {
                    final String drawing = ds.getKey();
                    if (msg.equals(drawing)){
                        drawing_no.setText(drawing);
                        inspector_name.setText(ds.child("inspector_name").getValue().toString());
                        quantity.setText(ds.child("quantity").getValue().toString());
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                items.child(drawing).child("status").setValue("ACCEPTED");
                            }
                        });
                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                items.child(drawing).child("status").setValue("REJECTED");

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    dialog.show();

    }

}
