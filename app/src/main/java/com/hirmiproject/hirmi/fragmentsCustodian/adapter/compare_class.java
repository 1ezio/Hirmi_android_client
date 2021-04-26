package com.hirmiproject.hirmi.fragmentsCustodian.adapter;

public class compare_class {
    String Timestamp;

    public compare_class(String timestamp) {
        Timestamp = timestamp;
    }

    public boolean getTimestamp() {
        return Boolean.parseBoolean(Timestamp);
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public compare_class() {
    }
}
