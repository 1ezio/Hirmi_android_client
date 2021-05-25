package com.hirmiproject.hirmi;

public class drawing_model {
    private  String mdrawing, memail, mdate, mtime;

    public drawing_model() {
    }

    public String getMdrawing() {
        return mdrawing;
    }

    public void setMdrawing(String mdrawing) {
        this.mdrawing = mdrawing;
    }

    public String getMemail() {
        return memail;
    }

    public void setMemail(String memail) {
        this.memail = memail;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public drawing_model(String mdrawing, String memail, String mdate, String mtime) {
        this.mdrawing = mdrawing;
        this.memail = memail;
        this.mdate = mdate;
        this.mtime = mtime;
    }
}
