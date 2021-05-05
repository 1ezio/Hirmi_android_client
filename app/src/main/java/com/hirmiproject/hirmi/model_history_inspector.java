package com.hirmiproject.hirmi;

public class model_history_inspector {
    private String draw;
    private String date;

    private String time ;
    private String image_url;

    public model_history_inspector(String draw, String date, String time, String status,String image_url) {
        this.draw = draw;
        this.date = date;
        this.time = time;
        this.status = status;
        this.image_url = image_url;
    }

    public String getDraw() {
        return draw;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public model_history_inspector() {
    }

    private  String status;

}
