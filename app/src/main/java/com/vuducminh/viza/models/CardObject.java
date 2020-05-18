package com.vuducminh.viza.models;


public class CardObject {
    private String info;
    private float disDPT;
    private int active;
    private float discount;
    private String bankName;
    private String image;

    public CardObject(String info, float disDPT, int active, float discount, String bankName, String image) {
        this.info = info;
        this.disDPT = disDPT;
        this.active = active;
        this.discount = discount;
        this.bankName = bankName;
        this.image = image;
    }

    public CardObject(String info, String bankName, String image) {
        this.info = info;
        this.bankName = bankName;
        this.image = image;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getDisDPT() {
        return disDPT;
    }

    public void setDisDPT(float disDPT) {
        this.disDPT = disDPT;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
