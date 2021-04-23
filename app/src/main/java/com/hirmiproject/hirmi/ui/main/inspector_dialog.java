package com.hirmiproject.hirmi.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.MainActivity;
import com.hirmiproject.hirmi.R;

public class inspector_dialog {
    public void   showDialog(final Activity activity, final String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.inspector_dialog);

        final TextView drawing_no, inspector_name, quantity;
        final Button accept, reject;
        drawing_no = dialog.findViewById(R.id.idrawing_id);
        inspector_name = dialog.findViewById(R.id.i_inspector_id);
        quantity = dialog.findViewById(R.id.quantity_i_id);
        accept = dialog.findViewById(R.id.accept_id);
        reject = dialog.findViewById(R.id.reject_id);
        final Fragment3 context = new Fragment3();


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
        final DatabaseReference i_items = database.getReference("item");
        i_items.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (final DataSnapshot s : datasnapshot.getChildren()) {
                    final String drawing = s.getKey();
                    if (msg.equals(drawing)){
                        drawing_no.setText(drawing);
                        inspector_name.setText(s.child("inspector_name").getValue().toString());
                        quantity.setText(s.child("quantity").getValue().toString());
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                i_items.child(drawing).child("status").setValue("ACCEPTED");
                                if (s.getKey().equals(drawing)){
                                    String d = s.child("cus_phn").getValue().toString();
                                    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

                                            try {
                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendTextMessage(d,null,"Inspection Call for " +
                                                        " : "+ drawing+ " "+ "is ACCEPTED",null,null);

                                            }catch (Exception e ){
                                                Toast.makeText(activity, "Permission not granted to Send SMS", Toast.LENGTH_SHORT).show();

                                            }

                                    }


                                    if  (whatsappinstalled()){
                                        Intent i =new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+"91"+d+"&text="+"Inspection Call for " +
                                                " : "+ drawing+ " "+ "is Rejected "));

                                        activity.startActivity(i);


                                        //SMS INTEGRATION




                                    }else{
                                        Toast.makeText(activity, "Whatsapp Not Found", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }

                            private boolean whatsappinstalled() {

                                Boolean n ;

                                try {
                                    PackageManager packageManager;
                                    packageManager =activity.getPackageManager();
                                    packageManager.getPackageInfo("com.whstsapp.com",0);
                                    n =true;




                                } catch (PackageManager.NameNotFoundException e) {
                                    n =true;
                                }


                                return n;


                            }
                        });
                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                i_items.child(drawing).child("status").setValue("REJECTED");

                                if (s.getKey().equals(drawing)){
                                    String d = s.child("cus_phn").getValue().toString();
                                    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

                                        try {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage(d,null,"Inspection Call for " +
                                                    " : "+ drawing+ " "+ "is ACCEPTED",null,null);

                                        }catch (Exception e ){
                                            Toast.makeText(activity, "Permission not granted to Send SMS", Toast.LENGTH_SHORT).show();

                                        }

                                    }



                                    if  (whatsappinstalled()){
                                        Intent i =new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+"91"+d+"&text="+"Inspection Call for " +
                                                " : "+ drawing+ " "+ "is REJECTED "));


                                        context.startActivity(i);


                                        //SMS INTEGRATION




                                    }else{
                                        Toast.makeText(activity, "Whatsapp Not Found", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }

                            private boolean whatsappinstalled() {PackageManager packageManager;
                                boolean n ;

                                try {
                                    packageManager =activity.getPackageManager();
                                    packageManager.getPackageInfo("com.whstsapp.com",0);
                                    n =true;




                                } catch (PackageManager.NameNotFoundException e) {
                                    n =true;
                                }


                                return n;


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
