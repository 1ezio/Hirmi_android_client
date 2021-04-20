package com.hirmiproject.hirmi.fragmentsCustodian

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.hirmiproject.hirmi.MainActivityNew
import com.hirmiproject.hirmi.R
import com.hirmiproject.hirmi.ui.main.dialog_fragment
import java.lang.Exception
import java.util.zip.Inflater


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

        arrayAdapter = context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1, arrayList) }!!

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {
                    val drawings =ds.key
                    arrayList.add(drawings.toString())
                    arrayAdapter = context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1, arrayList) }!!
                    lv.adapter = arrayAdapter


                }
                /*lv.onItemClickListener=AdapterView.OnItemClickListener { parent, view, position, id ->
                    // This is your listview's selected item
                    val item = parent.getItemAtPosition(position).toString()
                    *//*val transaction = activity!!.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.f_id, dialog_fragment())

                    transaction.commit()*//*
                        dialog_fragment().setupDialog(dia)

                }*/
                lv.setOnItemClickListener { parent, view, position, id ->
                    val element = arrayAdapter.getItem(position) // The item that was clicked
                    val dialogView = inflater.inflate(R.layout.custodian_dialog_box,null)
                    val dialog = AlertDialog.Builder(activity as Context).setView(dialogView)

                    var inspectorId = dialogView.findViewById<TextView>(R.id.inspector_id)
                    var drawingId = dialogView.findViewById<TextView>(R.id.drawing_id)
                    var descriptionId = dialogView.findViewById<EditText>(R.id.description_id)
                    var quantityId = dialogView.findViewById<TextView>(R.id.quantity_id)
                    var quantityForInsId = dialogView.findViewById<EditText>(R.id.quantity_i_id)

                    var descContainer : String
                    var inspectionQuatity : Int?
                    var inspectionStringQuatity : String

                    drawingId.text = element.toString()
                    dialog.setPositiveButton("Inspection Call"){text , listener ->

                        descContainer = descriptionId.text.toString()

                        try{
                            inspectionQuatity = quantityForInsId.text.toString().toInt()
                        }catch(e: Exception){
                            Toast.makeText(activity as Context, "Enter Inspection Quantity" , Toast.LENGTH_LONG).show()
                        }

                        // inspectionQuatity - contains Inspection Quantity
                        // descContainer  ->  conatins All the Description
                        // Code Here :



                    }
                    dialog.setNegativeButton("Cancel"){text , listener ->}
                    dialog.setCancelable(true)
                    dialog.create()
                    dialog.show()

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


