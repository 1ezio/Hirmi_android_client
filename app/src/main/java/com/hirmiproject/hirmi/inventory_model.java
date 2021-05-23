package com.hirmiproject.hirmi;

public class inventory_model {
    String i_material, i_group, i_uom,i_today,i_mtd,i_ytd,i_stock,i_monthly_target,i_asking_rate;

    public inventory_model() {
    }

    public String getI_material() {
        return i_material;
    }

    public void setI_material(String i_material) {
        this.i_material = i_material;
    }

    public String getI_group() {
        return i_group;
    }

    public void setI_group(String i_group) {
        this.i_group = i_group;
    }

    public String getI_uom() {
        return i_uom;
    }

    public void setI_uom(String i_uom) {
        this.i_uom = i_uom;
    }

    public String getI_today() {
        return i_today;
    }

    public void setI_today(String i_today) {
        this.i_today = i_today;
    }

    public String getI_mtd() {
        return i_mtd;
    }

    public void setI_mtd(String i_mtd) {
        this.i_mtd = i_mtd;
    }

    public String getI_ytd() {
        return i_ytd;
    }

    public void setI_ytd(String i_ytd) {
        this.i_ytd = i_ytd;
    }

    public String getI_stock() {
        return i_stock;
    }

    public void setI_stock(String i_stock) {
        this.i_stock = i_stock;
    }

    public String getI_monthly_target() {
        return i_monthly_target;
    }

    public void setI_monthly_target(String i_monthly_target) {
        this.i_monthly_target = i_monthly_target;
    }

    public String getI_asking_rate() {
        return i_asking_rate;
    }

    public void setI_asking_rate(String i_asking_rate) {
        this.i_asking_rate = i_asking_rate;
    }

    public inventory_model(String i_material, String i_group, String i_uom, String i_today, String i_mtd, String i_ytd, String i_stock, String i_monthly_target, String i_asking_rate) {
        this.i_material = i_material;
        this.i_group = i_group;
        this.i_uom = i_uom;
        this.i_today = i_today;
        this.i_mtd = i_mtd;
        this.i_ytd = i_ytd;
        this.i_stock = i_stock;
        this.i_monthly_target = i_monthly_target;
        this.i_asking_rate = i_asking_rate;
    }
}
