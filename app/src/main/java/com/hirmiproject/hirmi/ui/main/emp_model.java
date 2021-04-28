package com.hirmiproject.hirmi.ui.main;

public class emp_model {
    private String mname, memail, mphn;

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMemail() {
        return memail;
    }

    public void setMemail(String memail) {
        this.memail = memail;
    }

    public String getMphn() {
        return mphn;
    }

    public void setMphn(String mphn) {
        this.mphn = mphn;
    }

    public emp_model(String mname, String memail, String mphn) {
        this.mname = mname;
        this.memail = memail;
        this.mphn = mphn;
    }

    public emp_model() {
    }
}
