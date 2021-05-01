package com.hirmiproject.hirmi;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hirmiproject.hirmi.ui.main.SectionsPagerAdapter;
import com.hirmiproject.hirmi.ui.main.inspector_dialog;

public class Ispector_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ispector_layout);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        inspector_dialog dialog = new inspector_dialog();


        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabs.getTabAt(1).setIcon(R.drawable.ic_baseline_history_24);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
