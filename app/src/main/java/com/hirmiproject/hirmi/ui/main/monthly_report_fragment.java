package com.hirmiproject.hirmi.ui.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.MainActivity;
import com.hirmiproject.hirmi.R;
import com.hirmiproject.hirmi.fragmentsCustodian.history_fragment2;
import com.hirmiproject.hirmi.model_history_inspector;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.itextpdf.text.Annotation.FILE;

public class monthly_report_fragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View  view  = inflater.inflate(R.layout.monthly_report_fragment, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("report");
        final ListView lv = view.findViewById(R.id.lv_id);

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, 1);
        Button date_picker = view.findViewById(R.id.date_id);








        final DatePickerDialog.OnDateSetListener dateSetListener ;
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, final int year, int month, int day) {
                month= month+1;
                String date = month+"-"+year;

                final int finalMonth = month;
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       snapshot =  snapshot.child(String.valueOf(year)).child(String.valueOf(finalMonth));
                        final ArrayList<monthly_model> objects = new ArrayList<monthly_model>();
                        for (DataSnapshot s: snapshot.getChildren()){
                            objects.add(new monthly_model(s.child("drawing").getValue().toString(),s.child("quantity").getValue().toString()+" "+ s.child("status").getValue().toString()
                                    ,s.child("date").getValue().toString(),s.child("inspector").getValue().toString()));

                            CustomAdapter customAdapter = new CustomAdapter(getContext(),objects);
                            lv.setAdapter(customAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });






            }
        };


        //final DatePickerDialog.OnDateSetListener finalDateSetListener = dateSetListener;
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int mnth = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener,year,mnth,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        return view;
    }

    private static void addImage(Document document,byte[] byteArray)
    {
        Image image = null;
        try
        {
            image = Image.getInstance(byteArray);
        }
        catch (BadElementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try
        {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }







    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }









    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<monthly_model> objects;

        private class ViewHolder {
            TextView textView1;
            TextView textView2, textView3, textView4;

        }

        public CustomAdapter(Context context, ArrayList<monthly_model> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public monthly_model getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            CustomAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new CustomAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.monthly_list_items, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.m_draw_id);
                holder.textView2 = (TextView) convertView.findViewById(R.id.m_status_id);
                holder.textView3 = (TextView) convertView.findViewById(R.id.m_date_time_id);
                holder.textView4 = convertView.findViewById(R.id.m_ins_id);
                convertView.setTag(holder);
            } else {
                holder = (CustomAdapter.ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(objects.get(position).getDrawing());


            holder.textView2.setText(objects.get(position).getStatus());
            if (objects.get(position).getStatus().contains("ACCEPTED")) {
                holder.textView2.setTextColor(Color.GREEN);
            } else if (objects.get(position).getStatus().contains("REJECTED")) {
                holder.textView2.setTextColor(Color.RED);

            } else {
                holder.textView2.setTextColor(Color.GRAY);
            }


            holder.textView3.setText(objects.get(position).getDate());
            holder.textView4.setText(objects.get(position).getInspector());
            return convertView;

        }
    }
}
