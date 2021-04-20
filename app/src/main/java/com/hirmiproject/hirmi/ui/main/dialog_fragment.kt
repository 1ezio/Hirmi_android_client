package com.hirmiproject.hirmi.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hirmiproject.hirmi.MainActivityNew
import com.hirmiproject.hirmi.R

class dialog_fragment : DialogFragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        MainActivityNew.setFragment("Fragment_4")
        val view: View = inflater.inflate(R.layout.custodian_dialog_box, container, false)
        return view
    }
}