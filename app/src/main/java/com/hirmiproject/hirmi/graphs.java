package com.hirmiproject.hirmi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.util.Hex;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class graphs extends Fragment {

    private String[] mMonth = new String[] {
            "Jan", "Feb" , "Mar", "Apr", "May", "Jun",
            "Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.activity_graphs, container, false);

        Button btn = v.findViewById(R.id.btn_id);

        final ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 2020; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item    , years);

        Spinner spinYear = v.findViewById(R.id.year_id);



        spinYear.setAdapter(adapter);

        final ArrayList<String> mnths = new ArrayList<String>();
        mnths.add("Select Month");
        mnths.add("January");
        mnths.add("February");mnths.add("March");mnths.add("April");mnths.add("May");mnths.add("June");mnths.add("July");mnths.add("August");mnths.add("September");mnths.add("October");
        mnths.add("November");mnths.add("December");
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,mnths);
        Spinner spnmnth = v.findViewById(R.id.spinner_mnth_id);
        spnmnth.setAdapter(ad);

        final String[] final_mnth = new String[1];
        final String[] mnth_name = new String[1];

        spnmnth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                final_mnth[0] = String.valueOf(i);
                mnth_name[0] = String.valueOf(mnths.get(i));
                Toast.makeText(getActivity(), final_mnth[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final String[] final_year = new String[1];


        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final_year[0] = years.get(i);
                Toast.makeText(getActivity(), final_year[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                graph(final_mnth[0],final_year[0],mnth_name[0]);

            }
        });











        return  v ;

    }
    private void graph(String s, String s1, final String s2){



        //  final String [] dates = new String[]{};
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("report");
        final XYSeries accepted= new XYSeries("ACCEPTED");
        final List<String> dates   = new ArrayList<String>();

        final Map<String, Integer> map = new HashMap<String, Integer>();
        final Map<String, Integer> map2 = new HashMap<String, Integer>();

        final XYSeries rejected= new XYSeries("REJECTED");
        ref.child(String.valueOf(s1)).child(String.valueOf(s)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){

                   // int currentValue = map.get(ds.child("date").getValue().toString());
                    if (ds.child("status").getValue().toString().equals("ACCEPTED")){
                        String s = ds.child("date").getValue().toString();
                        s =s.substring(0,2);
                        map.put(s, 0);
                    }
                    if (ds.child("status").getValue().toString().equals("REJECTED")){
                        String s = ds.child("date").getValue().toString();
                        s =s.substring(0,2);
                        map2.put(s, 0);
                    }


                }
                for (DataSnapshot s: snapshot.getChildren()){
                    if (s.child("status").getValue().toString().equals("REJECTED")){

                        String d = s.child("date").getValue().toString();
                        d = d.substring(0,2);
                        int current = map2.get(d);
                        map2.put(d,current+1);

                        int x1 = 0;
                        for (Map.Entry<String, Integer> entry1:map2.entrySet()){
                            rejected.add(x1,entry1.getValue());
                            x1+=1;
                        }
                    }
                }


                for (DataSnapshot ds:snapshot.getChildren()){
                    String s = ds.child("date").getValue().toString();
                    s =s.substring(0,2);
                    if (map.size()!=0 ){
                        int currentValue = map.get(s);
                        if (ds.child("status").getValue().toString().equals("ACCEPTED")){
                            map.put(s, currentValue+1);

                            int x=0;
                            for (Map.Entry<String, Integer> entry:map.entrySet()){
                                accepted.add(x,entry.getValue());
                                x+=1;
                            }








                            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                            dataset.addSeries(accepted);
                            dataset.addSeries(rejected);

                            XYSeriesRenderer accpetRendere = new XYSeriesRenderer();
                            accpetRendere.setColor(Color.GREEN);

                            accpetRendere.setPointStyle(PointStyle.CIRCLE);
                            accpetRendere.setFillPoints(true);

                            accpetRendere.setDisplayChartValues(true);

                            XYSeriesRenderer rejectedseries = new XYSeriesRenderer();
                            rejectedseries.setColor(Color.RED);
                            rejectedseries.setPointStyle(PointStyle.CIRCLE);
                            rejectedseries.setFillPoints(true);
                            rejectedseries.setDisplayChartValues(true);


                            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
                            multiRenderer.setXLabels(0);
                            multiRenderer.setChartTitle("ACCEPTED CHART");
                            multiRenderer.setXTitle(s2);
                            multiRenderer.setBarSpacing(0.5);
                            multiRenderer.setBackgroundColor(Color.WHITE);
                            multiRenderer.setYTitle("COUNT");


                            multiRenderer.setLabelsTextSize(20f);

                            multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);
                            multiRenderer.setZoomButtonsVisible(true);

                            int y=0;
                            for (Map.Entry<String, Integer> entry:map.entrySet()){
                                multiRenderer.addXTextLabel(y,entry.getKey());
                                y+=1;
                            }

                            View chart ;

                            multiRenderer.addSeriesRenderer(accpetRendere);
                            multiRenderer.addSeriesRenderer(rejectedseries);
                            LinearLayout chartlayout = getActivity().findViewById(R.id.linear_id);

                            chart= ChartFactory.getBarChartView(getContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);

                            // Start Activity
                            chartlayout.addView(chart);


                        }
                    }



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }






    private void openchhart() {
        int[] x = { 1,2,3,4,5,6,7,8 };
        int[] income = { 2000,2500,2700,3000,2800,3500,3700,3800};
        int[] expense = {2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400 };

        // Creating an  XYSeries for Income
        XYSeries incomeSeries = new XYSeries("Income");
        // Creating an  XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("Expense");
        // Adding data to Income and Expense Series
        for(int i=0;i<x.length;i++){
            incomeSeries.add(x[i], income[i]);
            expenseSeries.add(x[i],expense[i]);
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(incomeSeries);
        // Adding Expense Series to dataset
        dataset.addSeries(expenseSeries);

        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.WHITE);
        incomeRenderer.setPointStyle(PointStyle.CIRCLE);
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(true);
        incomeRenderer.setFillBelowLine(true);

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.YELLOW);
        expenseRenderer.setPointStyle(PointStyle.CIRCLE);
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(2);
        expenseRenderer.setDisplayChartValues(true);
        expenseRenderer.setFillBelowLine(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Income vs Expense Chart");
        multiRenderer.setXTitle("Year 2012");
        multiRenderer.setYTitle("Amount in Dollars");
        multiRenderer.setZoomButtonsVisible(true);
        for(int i=0;i<x.length;i++){
            multiRenderer.addXTextLabel(i+1, mMonth[i]);
        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(incomeRenderer);
        multiRenderer.addSeriesRenderer(expenseRenderer);

        // Creating an intent to plot line chart using dataset and multipleRenderer
        Intent intent = ChartFactory.getLineChartIntent(getContext(), dataset, multiRenderer);

        // Start Activity
        startActivity(intent);
    }


}