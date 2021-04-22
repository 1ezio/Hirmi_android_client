package com.hirmiproject.hirmi.fragmentsCustodian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.hirmiproject.hirmi.MainActivityNew
import com.hirmiproject.hirmi.R
import com.hirmiproject.hirmi.ui.main.Fragment3
import com.hirmiproject.hirmi.ui.main.Fragment3.MyListAdaper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        MainActivityNew.setFragment("Fragment_2")
        val view: View =inflater.inflate(R.layout.fragment_custodian_history, container, false)

        val lv = view.findViewById<ListView>(R.id.history_id)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/")
        val items: DatabaseReference = database.getReference("item")
        val arrayList = ArrayList<String>()
        var arrayAdapter: ArrayAdapter<*>


        //DATE
        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("MMM-yyyy", Locale.getDefault())
        val formattedDate = df.format(c).toString()


        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    var drawings =ds.child("date").toString()
                    var status = ds.child("status").toString()
                    if (drawings.contains(formattedDate, ignoreCase = true)){
                        arrayList.add("Drawing No. : " + ds.child("drawing_no").value.toString() + "                          Status : " + ds.child("status").value.toString()+"                             Date : " + ds.child("date").value.toString())

                        arrayAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, arrayList) }!!
                        lv.adapter = arrayAdapter


                    }


                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        items.addListenerForSingleValueEvent(valueEventListener)

        return view
    }

    override fun onResume() {
        MainActivityNew.setFragment("Fragment_2")
        super.onResume()
    }

}