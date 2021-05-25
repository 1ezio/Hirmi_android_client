package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
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

import org.jetbrains.annotations.NotNull;

public class splash extends AwesomeSplash {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }*/
private int request_code = 11;
    @Override
    public void initSplash(ConfigSplash configSplash) {
        ActionBar actionBar = getSupportActionBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        configSplash.setBackgroundColor(R.color.darkorange);
        configSplash.setAnimCircularRevealDuration(3000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);


        configSplash.setTitleSplash("");

        configSplash.setOriginalHeight(550); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(450); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3);
        configSplash.setLogoSplash(R.drawable.logo_edited);
        configSplash.setAnimLogoSplashTechnique(Techniques.BounceInUp);
        configSplash.setTitleTextSize(45f);


        configSplash.setAnimLogoSplashDuration(2000);
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);





    }

    @Override
    public void animationsFinished() {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(splash.this);
        final com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE &&
                result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                    try {
                        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.FLEXIBLE,splash.this,request_code);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                }
            }
        });





        final Boolean[] Flag = {false};
        final CustomProgress progress = new CustomProgress(splash.this);
        progress.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!isNetworkAvailable()){
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
        else{

        }
        if (user!=null){
            final String user1 = user.getEmail().toString();
            final FirebaseDatabase[] database = new FirebaseDatabase[1];
            database[0] = FirebaseDatabase.getInstance();

            final DatabaseReference a_reference = database[0].getReference("admin") ;



             if (user1!=null){
                a_reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            String key = snapshot1.getKey();
                            key = key.replace(",", ".");
                            if (key.equals(user1)){
                                Intent intent = new Intent(splash.this,AdminMainScreenActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                progress.dismiss();
                                Flag[0] = true;
                                finish();
                                break;


                            }
                            else {

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
                                                Flag[0]=true;
                                                break;


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
                                                                Flag[0] = true;
                                                                break;
                                                            }else {
                                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                final DatabaseReference p_ref = database.getReference("power_user");
                                                                p_ref.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snap) {
                                                                        for (DataSnapshot snaps:snap.getChildren() ){
                                                                            String e = snaps.getKey();
                                                                            e= e.replace(",",".");
                                                                            if (e.equals(user1)){
                                                                                Intent intent = new Intent(splash.this,inventory.class);
                                                                                startActivity(intent);
                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                                                startActivity(intent);
                                                                                finish();
                                                                                Flag[0] = true;
                                                                                break;
                                                                            }else if (Flag[0]) {
                                                                                break;
                                                                            }else {
                                                                                startActivity(new Intent(splash.this, report.class));
                                                                                progress.dismiss();


                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== request_code){
            Toast.makeText(this, "Start Download", Toast.LENGTH_SHORT).show();

        }

    }
}