package com.vuducminh.viza.models;

public class MenuObject {
    private int imgRes;
    private String name;

    public MenuObject(int imgRes, String name) {
        this.imgRes = imgRes;
        this.name = name;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
