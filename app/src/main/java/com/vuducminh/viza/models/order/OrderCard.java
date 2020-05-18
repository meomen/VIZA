package com.vuducminh.viza.models.order;

public class OrderCard {
    private Double discount;
    private int price;
    private int moneypay;
    private String issuingHousCard;
    private String codeCard;
    private String seriCard;
    private String phoneOwned;
    private String dateCreate;
    private String dateFormat;

    public OrderCard() {
    }

    public OrderCard(Double discount, int price, int moneypay, String issuingHousCard, String codeCard,
                     String seriCard, String phoneOwned, String dateCreate, String dateFormat) {
        this.discount = discount;
        this.price = price;
        this.moneypay = moneypay;
        this.issuingHousCard = issuingHousCard;
        this.codeCard = codeCard;
        this.seriCard = seriCard;
        this.phoneOwned = phoneOwned;
        this.dateCreate = dateCreate;
        this.dateFormat = dateFormat;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMoneypay() {
        return moneypay;
    }

    public void setMoneypay(int moneypay) {
        this.moneypay = moneypay;
    }

    public String getIssuingHousCard() {
        return issuingHousCard;
    }

    public void setIssuingHousCard(String issuingHousCard) {
        this.issuingHousCard = issuingHousCard;
    }

    public String getCodeCard() {
        return codeCard;
    }

    public void setCodeCard(String codeCard) {
        this.codeCard = codeCard;
    }

    public String getSeriCard() {
        return seriCard;
    }

    public void setSeriCard(String seriCard) {
        this.seriCard = seriCard;
    }

    public String getPhoneOwned() {
        return phoneOwned;
    }

    public void setPhoneOwned(String phoneOwned) {
        this.phoneOwned = phoneOwned;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
