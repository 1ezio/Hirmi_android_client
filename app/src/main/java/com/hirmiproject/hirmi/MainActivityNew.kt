package com.hirmiproject.hirmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hirmiproject.hirmi.fragmentsCustodian.HistoryFragment
import com.hirmiproject.hirmi.fragmentsCustodian.HomeFragment
import com.hirmiproject.hirmi.fragmentsCustodian.SettingsFragment
import com.hirmiproject.hirmi.fragmentsCustodian.adapter.ViewPageAdapterCustodian

class MainActivityNew : AppCompatActivity() {
    lateinit var tabsCustodian: TabLayout
    lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)


        viewPager = findViewById(R.id.viewPager)
        tabsCustodian = findViewById(R.id.tabsCustodian)

        setUpTabs()

    }

    private fun setUpTabs(){
        val adapter = ViewPageAdapterCustodian(supportFragmentManager)
        adapter.addFragment(HomeFragment(),"Home")
        adapter.addFragment(HistoryFragment(),"History")
        adapter.addFragment(SettingsFragment(),"Settings")

        viewPager.adapter = adapter
        tabsCustodian.setupWithViewPager(viewPager)

        tabsCustodian.getTabAt(0)?.setIcon(R.drawable.ic_baseline_home_24)
        tabsCustodian.getTabAt(1)?.setIcon(R.drawable.ic_baseline_history_24)
        tabsCustodian.getTabAt(2)?.setIcon(R.drawable.ic_baseline_settings_24)
    }

}