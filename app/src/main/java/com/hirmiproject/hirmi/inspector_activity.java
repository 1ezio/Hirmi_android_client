package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hirmiproject.hirmi.ui.main.Fragment3;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class inspector_activity extends AppCompatActivity {

    public static Uri mimageuri;

    private TextView drawing_no, inspector_name, quantity;
    private Button accept, reject;
    final int PICK_IMAGE_REQUEST = 1;

    private EditText remarks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspector_dialog);
        drawing_no = findViewById(R.id.idrawing_id);
        inspector_name =findViewById(R.id.i_inspector_id);
        quantity = findViewById(R.id.quantity_i_id);
        accept = findViewById(R.id.accept_id);
        reject = findViewById(R.id.reject_id);

        Intent i = getIntent();
        final String msg = i.getStringExtra("key");

        final ProgressBar mprogress = findViewById(R.id.prog_id);
        final StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference("images/");



        remarks = findViewById(R.id.remark_id);

        final TextView attach ;
        attach = findViewById(R.id.attch_id);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
        final DatabaseReference i_items = database.getReference("item");

        final DatabaseReference report_ref = database.getReference("report");
        i_items.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (final DataSnapshot s : datasnapshot.getChildren()) {
                    final String drawing = s.getKey();
                    if (msg.equals(drawing)){
                        drawing_no.setText(drawing);
                        inspector_name.setText(s.child("inspector_name").getValue().toString());
                        quantity.setText(s.child("quantity_for_inspection").getValue().toString());
                        attach.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openfilechooser();

                            }
                        });
                        accept.setOnClickListener(new View.OnClickListener() {

                            @SuppressLint("ResourceType")
                            @Override
                            public void onClick(View view) {

                                uploadfile();

                                Calendar c= Calendar.getInstance();
                                int year = c.get(Calendar.YEAR);
                                int mnth = c.get(Calendar.MONTH);
                                mnth+=1;
                                Long tsLong = System.currentTimeMillis()/1000;
                                String stamp = tsLong.toString();

                                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("status").setValue("ACCEPTED");
                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("inspector").setValue(s.child("inspector_name").getValue().toString());
                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("date").setValue(date);
                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("quantity").setValue(s.child("quantity_for_inspection").getValue().toString());
                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("drawing").setValue(s.child("drawing_no").getValue().toString());



                                i_items.child(drawing).child("status").setValue("ACCEPTED");
                                String currentTime  =new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                                i_items.child(drawing).child("i_time").setValue(currentTime);
                                i_items.child(drawing).child("date").setValue(date);
                                i_items.child(drawing).child("remark").setValue(remarks.getText().toString());
                                if (s.getKey().equals(drawing)){

                                    String d = s.child("cus_phn").getValue().toString();
                                    String token = s.child("token").getValue().toString();

                                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,"Acknowledge"
                                            ,"Inspection Call for " +
                                            " : "+ drawing+ " "+ "is ACCEPTED",getApplicationContext(),inspector_activity.this);
                                    notificationsSender.SendNotifications();

                                    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

                                        try {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage(d,null,"Inspection Call for " +
                                                    " : "+ drawing+ " "+ "is ACCEPTED",null,null);

                                        }catch (Exception e ){
                                            Toast.makeText(inspector_activity.this, "Permission not granted to Send SMS", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                    if  (whatsappinstalled()){
                                        Intent i =new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+"91"+d+"&text="+"Inspection Call for " +
                                                " : "+ drawing+ " "+ "is ACCEPTED "));


                                        startActivityForResult(i,100);



                                        //SMS INTEGRATION


                                    }else{
                                        Toast.makeText(inspector_activity.this, "Whatsapp Not Found", Toast.LENGTH_SHORT).show();
                                    }

                                }


                            }

                            private boolean whatsappinstalled() {

                                Boolean n ;

                                try {
                                    PackageManager packageManager;
                                    packageManager =getPackageManager();
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


                                uploadfile();
                                Calendar c= Calendar.getInstance();
                                int year = c.get(Calendar.YEAR);
                                int mnth = c.get(Calendar.MONTH);
                                mnth+=1;
                                Long tsLong = System.currentTimeMillis()/1000;
                                String stamp = tsLong.toString();
                                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("status").setValue("REJECTED");
                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("inspector").setValue(s.child("inspector_name").getValue().toString());
                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("date").setValue(date);
                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("quantity").setValue(s.child("quantity_for_inspection").getValue().toString());
                                report_ref.child(String.valueOf(year)).child(String.valueOf(mnth)).child(stamp).child("drawing").setValue(s.child("drawing_no").getValue().toString());


                                i_items.child(drawing).child("status").setValue("REJECTED");
                                String currentTime  =new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                                i_items.child(drawing).child("i_time").setValue(currentTime);
                                i_items.child(drawing).child("date").setValue(date);

                                if (s.getKey().equals(drawing)) {
                                    String d = s.child("cus_phn").getValue().toString();
                                    if (s.getKey().equals(drawing)) {

                                        String token = s.child("token").getValue().toString();
                                        Context context1 = null;
                                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token, "Acknowledge"
                                                , "Drawing no. : " + drawing + " is REJECTED", getApplicationContext(), inspector_activity.this);
                                        notificationsSender.SendNotifications();

                                        i_items.child(drawing).child("remark").setValue(remarks.getText().toString());
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                            try {
                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendTextMessage(d, null, "Inspection Call for " +
                                                        " : " + drawing + " " + "is REJECTED", null, null);

                                            } catch (Exception e) {
                                                Toast.makeText(inspector_activity.this, "Permission not granted to Send SMS", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    }


                                    if (whatsappinstalled()) {
                                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + "91" + d + "&text=" + "Inspection Call for " +
                                                " : " + drawing + " " + "is REJECTED "));


                                        startActivityForResult(i,100);

                                        Intent intent = new Intent(inspector_activity.this,Ispector_layout.class);
                                        startActivity(intent);

                                        //SMS INTEGRATION


                                    } else {
                                        Toast.makeText(inspector_activity.this, "Whatsapp Not Found", Toast.LENGTH_SHORT).show();

                                    }

                                }


                            }

                            private boolean whatsappinstalled() {PackageManager packageManager;
                                boolean n ;

                                try {
                                    packageManager =getPackageManager();
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

            private String getfileExtension(URI uri){
                ContentResolver cr = getContentResolver();
                MimeTypeMap mime =MimeTypeMap.getSingleton();
                return mime.getExtensionFromMimeType(cr.getType(mimageuri));

            }
            private void uploadfile() {

                if(mimageuri!=null){


                    Bitmap bitImage= null;
                    try {
                        bitImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(mimageuri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitImage.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                    byte[] fileInBytes = baos.toByteArray();



                    final StorageReference fileref = storageReference.child(msg+"."+"jpg");
                    fileref.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(inspector_activity.this, "Success", Toast.LENGTH_SHORT).show();
                                    upload_image_model model = new upload_image_model(msg+"."+"jpg", uri.toString());
                                    i_items.child(msg).child("image_url").setValue(model);
                                }
                            });


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mprogress.setProgress(0);
                                }
                            },5000);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(inspector_activity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            int progress = (int) (100*((snapshot.getBytesTransferred())/(snapshot.getTotalByteCount())));
                            mprogress.setProgress((int) progress);
                        }
                    });


                }else{
                    Toast.makeText(inspector_activity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }

            private void openfilechooser() {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);



            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mimageuri = data.getData();
        if (requestCode == 100 && resultCode == RESULT_OK){
            Intent intent = new Intent(inspector_activity.this,Ispector_layout.class);
            startActivity(intent);
        }

        super.onActivityResult(requestCode, resultCode, data);



        }
    }

}