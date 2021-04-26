package com.hirmiproject.hirmi.ui.main;

public class monthly_model {
    public monthly_model() {
    }

    private String drawing, status, date, inspector;

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public monthly_model(String drawing, String status, String date, String inspector) {
        this.drawing = drawing;
        this.status = status;
        this.date = date;
        this.inspector = inspector;
    }
}
