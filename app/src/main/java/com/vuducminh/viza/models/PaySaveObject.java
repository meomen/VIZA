package com.vuducminh.viza.models;



public class PaySaveObject {
    private String cardnumber;
    private String placeIssue;
    private String fullname;
    private int active;
    private String dateIssue;
    private String contact;
    private String bankname;
    private String branch;

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getPlaceIssue() {
        return placeIssue;
    }

    public void setPlaceIssue(String placeIssue) {
        this.placeIssue = placeIssue;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(String dateIssue) {
        this.dateIssue = dateIssue;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
