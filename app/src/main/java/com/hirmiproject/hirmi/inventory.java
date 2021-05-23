package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.invertory_options.add_material;
import com.hirmiproject.hirmi.invertory_options.adding_existing;
import com.hirmiproject.hirmi.invertory_options.adding_new;
import com.hirmiproject.hirmi.ui.main.emp_model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class inventory extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        final Button report  = findViewById(R.id.report_id);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(inventory.this,report.class));
            }
        });





        final ListView listView = findViewById(R.id.list_id);

        ImageView sign= findViewById(R.id.sign_id);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(inventory.this,main_login.class));
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("inventory");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<inventory_model> model = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()){
                    String ma= snap.getKey();

                    for (DataSnapshot s:snap.getChildren()){
                        String gr = s.getKey();
                        model.add(new inventory_model(ma,gr,s.child("uom").getValue().toString(),
                                s.child("today").getValue().toString(),
                                s.child("mtd").getValue().toString(),
                                s.child("ytd").getValue().toString(),
                                s.child("stock").getValue().toString(),
                                s.child("monthly_target").getValue().toString(),
                                s.child("asking_rate").getValue().toString()
                        ));

                        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),model);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });









        TextView add = findViewById(R.id.add_id);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(inventory.this, add_material.class));

            }
        });



    }


    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<inventory_model> objects;

        private class ViewHolder {
            TextView material, group, today,mtd,ytd,uom,stock,target,asking_rate;

        }

        public CustomAdapter(Context context, ArrayList<inventory_model> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public inventory_model getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            CustomAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new CustomAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.inventory_items, null);
                holder.material = (TextView)convertView.findViewById(R.id.material_id);
                holder.group =(TextView) convertView.findViewById(R.id.group_id);
                holder.uom = (TextView)convertView.findViewById(R.id.uom_id);
                holder.today = (TextView)convertView.findViewById(R.id.today_id);
                holder.mtd = (TextView)convertView.findViewById(R.id.mtd_id);
                holder.ytd = (TextView)convertView.findViewById(R.id.ytd_id);
                holder.stock =(TextView)convertView.findViewById(R.id.stock_id);
                holder.target = (TextView)convertView.findViewById(R.id.target_id);
                holder.asking_rate = (TextView)convertView.findViewById(R.id.asking_rate_id);
                convertView.setTag(holder);
            } else {
                holder = (CustomAdapter.ViewHolder) convertView.getTag();
            }
            holder.material.setText(  objects.get(position).getI_material());
            holder.group.setText(objects.get(position).getI_group());
            holder.uom.setText(objects.get(position).getI_uom());

            holder.today.setText(  objects.get(position).getI_today());
            String y = String.format("%.2f",Float.parseFloat(objects.get(position).getI_mtd()));
            holder.mtd.setText(y);
            String m = String.format("%.2f",Float.parseFloat(objects.get(position).getI_ytd()));
            holder.ytd.setText(m);


            if (objects.get(position).getI_stock().equals("")){
                holder.stock.setText("Not Available");
            }else{
                holder.stock.setText(objects.get(position).getI_stock());
            }

            if (objects.get(position).getI_monthly_target().equals("")){
                holder.target.setText("Not Available");
            }else{
                holder.target.setText(objects.get(position).getI_monthly_target());
            }
            if (objects.get(position).getI_asking_rate().equals("")){
                holder.asking_rate.setText("Not Available");
            }else{

                holder.asking_rate.setText(objects.get(position).getI_asking_rate());
            }
            return convertView;

        }
    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }
}