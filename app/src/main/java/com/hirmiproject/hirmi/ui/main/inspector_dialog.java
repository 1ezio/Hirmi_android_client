package com.hirmiproject.hirmi.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hirmiproject.hirmi.CustomProgress;
import com.hirmiproject.hirmi.FcmNotificationsSender;
import com.hirmiproject.hirmi.MainActivity;
import com.hirmiproject.hirmi.R;
import com.hirmiproject.hirmi.upload_image_model;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class inspector_dialog  {
    Context context;
    //Constructor
    public static Uri mimageuri;

     private TextView drawing_no, inspector_name, quantity;
     private Button accept, reject;
     private EditText  remarks;

    public void   showDialog(final Activity activity, final String msg){
         final int PICK_IMAGE_REQUEST = 1;
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.inspector_dialog);



        drawing_no = dialog.findViewById(R.id.idrawing_id);
        inspector_name = dialog.findViewById(R.id.i_inspector_id);
        quantity = dialog.findViewById(R.id.quantity_i_id);
        accept = dialog.findViewById(R.id.accept_id);
        reject = dialog.findViewById(R.id.reject_id);

        final ProgressBar mprogress = dialog.findViewById(R.id.prog_id);
        final StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference("images");



        remarks = dialog.findViewById(R.id.remark_id);
        final Fragment3 context = new Fragment3();
        final TextView attach ;
        attach = dialog.findViewById(R.id.attch_id);


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
                        quantity.setText(s.child("quantity").getValue().toString());
                        attach.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openfilechooser();

                            }
                        });
                        accept.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                try {
                                    uploadfile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

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
                                    Context context1 = null;
                                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,"Acknowledge"
                                            ,"Inspection Call for " +
                                            " : "+ drawing+ " "+ "is ACCEPTED",activity);
                                    notificationsSender.SendNotifications();

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
                                                " : "+ drawing+ " "+ "is ACCEPTED "));


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


                                try {
                                    uploadfile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                                                , "Drawing no. : " + drawing + " is REJECTED", activity);
                                        notificationsSender.SendNotifications();

                                        i_items.child(drawing).child("remark").setValue(remarks.getText().toString());
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                            try {
                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendTextMessage(d, null, "Inspection Call for " +
                                                        " : " + drawing + " " + "is REJECTED", null, null);

                                            } catch (Exception e) {
                                                Toast.makeText(activity, "Permission not granted to Send SMS", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    }


                                    if (whatsappinstalled()) {
                                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + "91" + d + "&text=" + "Inspection Call for " +
                                                " : " + drawing + " " + "is REJECTED "));


                                        activity.startActivity(i);



                                        //SMS INTEGRATION


                                    } else {
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


            private void uploadfile() throws IOException {

                if(mimageuri!=null){

                  /*  File file = new File(SiliCompressor.with(activity).compress(FileUtils.getPath(activity,mimageuri),new File(activity.getCacheDir(),"temp")));
                    Uri uri = Uri.fromFile(file);
                    AnstronCoreHelper coreHelper;
                    file.delete();


                    try {


                         bmp (activity.getContentResolver(),mimageuri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    //here you can choose quality factor in third parameter(ex. i choosen 25)
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] fileInBytes = baos.toByteArray();

*/                  Bitmap bitImage=BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(mimageuri));
                    bitImage=compressImage(bitImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitImage.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                    byte[] fileInBytes = baos.toByteArray();
                    StorageReference fileref = storageReference.child(System.currentTimeMillis()+"."+"jpg");
                    fileref.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mprogress.setProgress(0);
                                }
                            },5000);
                            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                            upload_image_model model = new upload_image_model(msg,storageReference.getDownloadUrl().toString());
                            i_items.child(msg).child("image_url").setValue(model);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            int progress = (int) (100*((snapshot.getBytesTransferred())/(snapshot.getTotalByteCount())));
                            mprogress.setProgress((int) progress);
                        }
                    });


                }else{
                    Toast.makeText(activity, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }

            private void openfilechooser() {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(intent,1);



            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    dialog.show();

    }
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Compression quality, here 100 means no compression, the storage of compressed data to baos
        int options = 90;
        while (baos.toByteArray().length / 1024 > 400) {  //Loop if compressed picture is greater than 400kb, than to compression
            baos.reset();//Reset baos is empty baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//The compression options%, storing the compressed data to the baos
            options -= 10;//Every time reduced by 10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//The storage of compressed data in the baos to ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//The ByteArrayInputStream data generation
        return bitmap;
    }
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK  && data!=null && data.getData()!=null){
            mimageuri = data.getData();


        }
    }





}
