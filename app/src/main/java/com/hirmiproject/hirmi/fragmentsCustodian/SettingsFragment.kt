package com.hirmiproject.hirmi.fragmentsCustodian

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.google.firebase.iid.FirebaseInstanceId
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




        arrayAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, arrayList) }!!

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {
                    val drawings =ds.key
                    arrayList.add(drawings.toString())
                    arrayAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, arrayList) }!!
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
                    val dialogView = inflater.inflate(R.layout.custodian_dialog_box, null)
                    val dialog = AlertDialog.Builder(activity as Context).setView(dialogView)

                    var inspectorId = dialogView.findViewById<TextView>(R.id.inspector_id)
                    var drawingId = dialogView.findViewById<TextView>(R.id.drawing_id)
                    var descriptionId = dialogView.findViewById<EditText>(R.id.description_id)
                    var quantityId = dialogView.findViewById<TextView>(R.id.quantity_id)
                    var reg = dialogView.findViewById<EditText>(R.id.reg_id_id)
                    var quantityForInsId = dialogView.findViewById<EditText>(R.id.quantity_i_id)

                    var descContainer : String
                    var inspectionQuatity : String
                    var inspectionStringQuatity : String
                    var d = dataSnapshot.child(element.toString())
                        val name=d.child("inspector_name").getValue().toString()
                        inspectorId.setText(name)


                        val q=d.child("quantity").getValue().toString()
                        quantityId.setText(q)





                    drawingId.text = element.toString()
                    dialog.setPositiveButton("Inspection Call"){ text, listener ->
                        //Custodian RegID verification
                        val cus_data:DatabaseReference = database.getReference("custodian")
                        val listener = object :ValueEventListener{
                            override fun onDataChange(ss: DataSnapshot) {
                                for (s in ss.children) {
                                    if (s.key==reg.text.toString()) {
                                        val d = reg.text.toString()
                                        val p = s.child("phn").getValue().toString()
                                        items.child(element as String).child("cus_phn").setValue(p)



                                    }
                                }


                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        }
                        cus_data.addListenerForSingleValueEvent(listener)


                        descContainer = descriptionId.text.toString()
                        items.child(element as String).child("basic_desc").setValue(descContainer)
                        FirebaseInstanceId.getInstance().instanceId
                                .addOnCompleteListener(OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        Log.w(ContentValues.TAG, "getInstanceId failed", task.exception)
                                        return@OnCompleteListener
                                    }

                                    // Get new Instance ID token
                                    val token = task.result!!.token
                                    items.child(element as String).child("token").setValue(token)
                                })

                        try{
                            inspectionQuatity = quantityForInsId.text.toString()
                            items.child(element as String).child("quantity_for_inspection").setValue(inspectionQuatity)
                            val num = d.child("phone").getValue().toString()

                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                if ((ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED)){
                                    try {val smsManager = SmsManager.getDefault()
                                        smsManager.sendTextMessage(num, null, "Inspection Call for " +
                                                " : " + element + " " + "is made", null, null)

                                    }catch (e: Exception){
                                        requestPermissions(context as Activity, (arrayOf(android.Manifest.permission.SEND_SMS)), 1)


                                    }
                                }
                            }
                            if  (ifwhatsappinstalled()){
                                var i = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + "91" + num + "&text=" + "Inspection Call for " +
                                        " : " + element + " " + "is made"))

                                startActivity(i)


                                //SMS INTEGRATION




                            }else{
                                Toast.makeText(context, "Whatsapp Not Found", Toast.LENGTH_SHORT).show()
                            }

                        }catch (e: Exception){
                            Toast.makeText(activity as Context, "Enter Inspection Quantity", Toast.LENGTH_LONG).show()
                        }






                    }
                    dialog.setNegativeButton("Cancel"){ text, listener ->}
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

    private fun ifwhatsappinstalled():Boolean {
        val packageManager: PackageManager
        var whatsappinstalled:Boolean

        return try {
            val packageManager = context?.packageManager
            packageManager ?.getPackageInfo("com.whatsapp", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


    override fun onResume() {
        MainActivityNew.setFragment("Fragment_3")
        super.onResume()
    }


}


