package com.hirmiproject.hirmi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.hirmiproject.hirmi.fragmentsCustodian.SettingsFragment
import com.hirmiproject.hirmi.fragmentsCustodian.adapter.ViewPageAdapterCustodian
import com.hirmiproject.hirmi.fragmentsCustodian.history_fragment2


class MainActivityNew : AppCompatActivity() {
    var user = FirebaseAuth.getInstance().currentUser
    companion object{
        var currentFragment: String? =null

        fun setFragment(s: String){
            currentFragment = s
        }
        fun getFragment() : String {
            return currentFragment.toString()
        }

    }


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
        adapter.addFragment(SettingsFragment(), "Drawing No.")
        adapter.addFragment(history_fragment2(), "History")
        


        viewPager.adapter = adapter
        tabsCustodian.setupWithViewPager(viewPager)


        tabsCustodian.getTabAt(1)?.setIcon(R.drawable.ic_baseline_history_24)
        tabsCustodian.getTabAt(0)?.setIcon(R.drawable.ic_baseline_settings_24)
    }

    var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            if (doubleBackToExitPressedOnce) {
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
            }
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Refreshed ! Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }


}