package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
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
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class splash extends AwesomeSplash {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }*/

    @Override
    public void initSplash(ConfigSplash configSplash) {
        ActionBar actionBar = getSupportActionBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        configSplash.setBackgroundColor(R.color.colorAccent);
        configSplash.setAnimCircularRevealDuration(3000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);


        configSplash.setTitleSplash("\uD835\uDCD3\uD835\uDCF8\uD835\uDCEC\uD835\uDCFE\uD835\uDCF6\uD835\uDCEE\uD835\uDCF7\uD835\uDCFD\uD835\uDCF2\uD835\uDCFF\uD835\uDCEE");

        configSplash.setOriginalHeight(550); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(450); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3);
        configSplash.setLogoSplash(R.drawable.taaju);
        configSplash.setAnimLogoSplashTechnique(Techniques.BounceInUp);
        configSplash.setTitleTextSize(45f);


        configSplash.setAnimLogoSplashDuration(2000);
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);





    }

    @Override
    public void animationsFinished() {


        final CustomProgress progress = new CustomProgress(splash.this);
        progress.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            final String user1 = user.getEmail().toString();
            FirebaseDatabase database ;
            database = FirebaseDatabase.getInstance();

            final DatabaseReference a_reference = database.getReference("admin") ;



            if (user1!=null){
                a_reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            String key = snapshot1.getKey();
                            key = key.replace(",", ".");
                            if (key.equals(user1)){
                                Intent intent = new Intent(splash.this,AdminMainScreenActivity.class);
                                startActivity(intent);
                                progress.dismiss();

                            }else {
                                FirebaseDatabase database ;
                                database = FirebaseDatabase.getInstance();
                                final DatabaseReference c_reference = database.getReference("custodian") ;
                                c_reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot c_snapshot) {
                                        for(DataSnapshot c_snapshot1:c_snapshot.getChildren()) {
                                            String key = c_snapshot1.getKey();
                                            key = key.replace(",", ".");
                                            if (key.equals(user1)) {
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
                                                                String e = user1.replace(".",",");
                                                                c_reference.child(e).child("c_token").setValue(token);
                                                            }
                                                        });




                                                Intent intent = new Intent(splash.this, MainActivityNew.class);
                                                startActivity(intent);
                                                progress.dismiss();

                                            }else{
                                                FirebaseDatabase database ;
                                                database = FirebaseDatabase.getInstance();
                                                final DatabaseReference i_reference = database.getReference("inspector") ;

                                                i_reference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot i_snapshot) {
                                                        for(DataSnapshot i_snapshot1:i_snapshot.getChildren()) {
                                                            String key = i_snapshot1.getKey();
                                                            key = key.replace(",", ".");
                                                            if (key.equals(user1)) {

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
                                                                                String e = user1.replace(".",",");
                                                                                i_reference.child(e).child("i_token").setValue(token);
                                                                            }
                                                                        });




                                                                Intent intent = new Intent(splash.this, Ispector_layout.class);
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






            }

        }
        else{
            startActivity(new Intent(splash.this,main_login.class));
            progress.dismiss();
        }



    }
}