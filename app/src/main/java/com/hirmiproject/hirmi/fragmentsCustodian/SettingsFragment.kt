package com.hirmiproject.hirmi.fragmentsCustodian

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hirmiproject.hirmi.MainActivityNew
import com.hirmiproject.hirmi.R


class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
     inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        MainActivityNew.setFragment("Fragment_3")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custodian_settings, container, false)


    }

    override fun onResume() {
        MainActivityNew.setFragment("Fragment_3")
        super.onResume()
    }

}