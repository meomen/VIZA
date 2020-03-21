package com.vuducminh.viza.models;


public class NewsObject {
    private String imageurl;
    private String title;
    private String abstract_;
    private String url;

    public NewsObject(String imageurl, String title, String abstract_, String url) {
        this.imageurl = imageurl;
        this.title = title;
        this.abstract_ = abstract_;
        this.url = url;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract_() {
        return abstract_;
    }

    public void setAbstract_(String abstract_) {
        this.abstract_ = abstract_;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
