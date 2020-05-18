package com.vuducminh.viza.models.order;

public class OrderDeposit {
    private Double discount;
    private int price;
    private String issuingHousCard;
    private String nameBank;
    private String codeCard;
    private String seriCard;
    private String bankAccountNumber;
    private String dateCreate;
    private String dateFormat;


    public OrderDeposit(Double discount, int price, String issuingHousCard, String nameBank, String codeCard, String seriCard, String bankAccountNumber, String dateCreate, String dateFormat) {
        this.discount = discount;
        this.price = price;
        this.issuingHousCard = issuingHousCard;
        this.nameBank = nameBank;
        this.codeCard = codeCard;
        this.seriCard = seriCard;
        this.bankAccountNumber = bankAccountNumber;
        this.dateCreate = dateCreate;
        this.dateFormat = dateFormat;
    }

    public OrderDeposit() {
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

    public String getIssuingHousCard() {
        return issuingHousCard;
    }

    public void setIssuingHousCard(String issuingHousCard) {
        this.issuingHousCard = issuingHousCard;
    }

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
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

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
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
