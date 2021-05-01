    package com.hirmiproject.hirmi.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hirmiproject.hirmi.FcmNotificationsSender;
import com.hirmiproject.hirmi.Ispector_layout;
import com.hirmiproject.hirmi.R;
import com.hirmiproject.hirmi.inspector_activity;
import com.hirmiproject.hirmi.main_login;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Fragment3 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment3_inspector_layout, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
        final DatabaseReference items = database.getReference("item");
        final DatabaseReference n = database.getReference("inspector");
       TextView  signout = view.findViewById(R.id.sign_id);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), main_login.class);
                startActivity(intent);
            }
        });

        items.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> arrayList = new ArrayList<String>();

                for (final DataSnapshot childs : snapshot.getChildren()) {



                    n.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dsnapshot) {


                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String mail = user.getEmail().toString();
                            String nmail = mail.replace(".",",");
                            String drawing = childs.getKey();
                            String name ;
                            name = dsnapshot.child(nmail).child("name").getValue().toString();
                            if (childs.child("status").getValue().toString().equals("PENDING") && childs.child("inspector_name").getValue().toString().equals(name)) {
                                arrayList.add(drawing);
                                // MyListAdapter adapter= new MyListAdapter(getContext(), drawing, "Approve");
                                ListView lv = (ListView) view.findViewById(R.id.list_id);
                                //arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,arrayList);
                                MyListAdaper adapter = new MyListAdaper(getContext(), R.layout.inspector_drawings, arrayList);

                                lv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();



                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        //inspector_dialog dialog = new inspector_dialog();

                                        //dialog.showDialog(getActivity(), arrayList.get(i));
                                        Intent intent = new Intent(getActivity(), inspector_activity.class);
                                        intent.putExtra("key",arrayList.get(i));
                                        startActivity(intent);
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(Fragment3.this).commit();

                                    }
                                });


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        return view;
    }
    public class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            FirebaseMessaging.getInstance().subscribeToTopic("all");

            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();

                viewHolder.title = (TextView) convertView.findViewById(R.id.drawing_id);
                viewHolder.button = (Button) convertView.findViewById(R.id.ok_id);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            final ViewHolder finalMainViewholder = mainViewholder;
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    finalMainViewholder.button.setVisibility(View.INVISIBLE);

                    //Toast.makeText(getContext(), "Acknowledge Sent", Toast.LENGTH_LONG).show();
                    final FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
                    DatabaseReference items = database.getReference("item");
                    final Context context = null;
                    items.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                               if (dataSnapshot.getKey().equals(getItem(position)) && dataSnapshot.child("status").getValue().toString().equals("PENDING")) {

                                   String d = dataSnapshot.child("cus_phn").getValue().toString();
                                   String token = dataSnapshot.child("token").getValue().toString();
                                   FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token, "Acknowledge"
                                           , "Acknowledge by Inspector",context ,getActivity());
                                   notificationsSender.SendNotifications();

                                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                       if ((ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)) {
                                           try {
                                               SmsManager smsManager = SmsManager.getDefault();
                                               smsManager.sendTextMessage(d, null, "Inspection Call for " +
                                                       " : " + getItem(position) + " " + "is received to Inspector ", null, null);

                                           } catch (Exception e) {
                                               Toast.makeText(getContext(), "Permission not granted to Send SMS", Toast.LENGTH_SHORT).show();

                                           }
                                       }
                                   }


                                   try {
                                       PackageManager packageManager = getActivity().getPackageManager();
                                       Intent i = new Intent(Intent.ACTION_VIEW);
                                       String url = "https://api.whatsapp.com/send?phone=" + "91"+d + "&text=" + URLEncoder.encode("Inspection Call for " +
                                               " : " + getItem(position) + " " + "is received to Inspector ", "UTF-8");
                                       i.setPackage("com.whatsapp");
                                       i.setData(Uri.parse(url));
                                       if (i.resolveActivity(packageManager) != null) {
                                           startActivity(i);

                                       } else {
                                           Toast.makeText(getContext(), "Whatsapp NOT Found", Toast.LENGTH_SHORT).show();
                                       }
                                   } catch (Exception e) {
                                       Log.e("ERROR WHATSAPP", e.toString());
                                       Toast.makeText(getContext(), "Whatsapp NOT Found", Toast.LENGTH_SHORT).show();
                                   }


                                  /* if  (ifwhatsappinstalled()){
                                       Intent i =new Intent(String.valueOf(getContext()), Uri.parse("https://api.whatsapp.com/send?phone="+"91"+d+"&text="+"Inspection Call for " +
                                               " : "+ getItem(position)+ " "+ "is received to Inspector "));

                                        startActivity(i);


                                       //SMS INTEGRATION




                                   }else{
                                       Toast.makeText(getContext(), "Whatsapp Not Found", Toast.LENGTH_SHORT).show();
                                   }*/
                               }
                               }
                            }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });







                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }

    private boolean ifwhatsappinstalled() {
        PackageManager packageManager;
        Boolean whatsappinstalled ;

        try {
            packageManager = getActivity().getPackageManager();
            packageManager.getPackageInfo("com.whstsapp.com",0);
            whatsappinstalled =true;




        } catch (PackageManager.NameNotFoundException e) {
            whatsappinstalled =true;
        }


        return whatsappinstalled;
    }

    public class ViewHolder {


        TextView title;
        Button button;
    }

}
