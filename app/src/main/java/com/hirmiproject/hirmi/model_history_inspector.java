package com.hirmiproject.hirmi;

public class model_history_inspector {
    private String draw;
    private String date;
    private String time ;

    public model_history_inspector(String draw, String date, String time, String status) {
        this.draw = draw;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getDraw() {
        return draw;
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
