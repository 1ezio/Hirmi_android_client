package com.hirmiproject.hirmi.ui.main;

public class monthly_model {


    public monthly_model() {
    }

    private String drawing, status, date, inspector, image_url,d;

    public monthly_model(String d) {
        this.d = d;
    }

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getStatus() {
        return status;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public monthly_model(String drawing, String status, String date, String inspector, String image_url, String d) {
        this.drawing = drawing;
        this.status = status;
        this.date = date;
        this.inspector = inspector;
        this.image_url = image_url;
        this.d = d;
    }
}
