package com.hirmiproject.hirmi;

import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
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


        configSplash.setAnimLogoSplashDuration(1000);
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);

    }

    @Override
    public void animationsFinished() {
        startActivity(new Intent(splash.this,main_login.class));
    }
}