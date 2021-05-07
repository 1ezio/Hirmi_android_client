package com.hirmiproject.hirmi.graphs_files;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    public List<String> labels;
    public List<BarEntry> negativeValue;
   
    public List<BarEntry> positiveValue;

    public DataModel() {
        labels = new ArrayList<>();
        negativeValue = new ArrayList<>();
        positiveValue = new ArrayList<>();
    }

    public void addEntry(String date, int positiveCount, int negativeCount) {

        labels.add(date);

        positiveValue.add(new BarEntry(0, positiveCount));
        negativeValue.add(new BarEntry(0, negativeCount));

    }

    public List<String> getLabels() {
        return labels;
    }

    public List<BarEntry> getNegativeValue() {
        return negativeValue;
    }

    public List<BarEntry> getPositiveValue() {
        return positiveValue;
    }
}

