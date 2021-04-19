package com.hirmiproject.hirmi.fragmentsCustodian

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.getInstance
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
        val view:View = inflater.inflate(R.layout.fragment_custodian_settings, container, false)
        val lv = view.findViewById<ListView>(R.id.listview)

        val database:FirebaseDatabase = getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/")
        val items:DatabaseReference = database.getReference("item")
        val arrayList = ArrayList<String>()
        var arrayAdapter: ArrayAdapter<*>
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {
                    val drawings =ds.key
                    arrayList.add(drawings.toString())
                    arrayAdapter = context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1, arrayList) }!!
                    lv.adapter = arrayAdapter


                }
                lv.onItemClickListener=AdapterView.OnItemClickListener{parent, view, position, id ->
                // This is your listview's selected item
                val item = parent.getItemAtPosition(position).toString()


            }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        items.addListenerForSingleValueEvent(valueEventListener)



        // Inflate the layout for this fragment

        return view


    }

    override fun onResume() {
        MainActivityNew.setFragment("Fragment_3")
        super.onResume()
    }

}

