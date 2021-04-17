package com.hirmiproject.hirmi.fragmentsCustodian

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hirmiproject.hirmi.MainActivityNew
import com.hirmiproject.hirmi.R

class HomeFragment : Fragment() {

    lateinit var btnInspectionCall : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_custodian_home, container, false)
        MainActivityNew.setFragment("Fragment_1")

        btnInspectionCall = view.findViewById(R.id.btnInspectionCall)
        
        return view
    }

    override fun onResume() {
        MainActivityNew.setFragment("Fragment_1")
        super.onResume()
    }


}