package com.hirmiproject.hirmi;

public class upload_image_model {

    private  String mname ;
    private  String murl;

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }

    public upload_image_model(String mname, String murl) {
        this.mname = mname;
        this.murl = murl;
    }

    public upload_image_model() {
    }
}
