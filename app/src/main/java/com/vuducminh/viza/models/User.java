package com.vuducminh.viza.models;

public class User {
    private String address;
    private int type;
    private String company;
    private String email;
    private int balance;
    private int isActive;
    private String mobile;
    private String fullname;
    private String username;
    private String receive_otp;
    private String identityNumber;
    private String addressCompany;
    private String megavnnUsername;
    private String sex;
    private String provice;
    private String city;
    private String token = "";
    private int gioitinh;
    private int userID;
    private String dateBirth;

    public User(String address, int type, String company, String email, int balance, int isActive,
                String mobile, String fullname, String username, String receive_otp, String identityNumber,
                String addressCompany, String megavnnUsername, String sex, String provice, String city, String token,
                int gioitinh, int userID, String dateBirth) {
        this.address = address;
        this.type = type;
        this.company = company;
        this.email = email;
        this.balance = balance;
        this.isActive = isActive;
        this.mobile = mobile;
        this.fullname = fullname;
        this.username = username;
        this.receive_otp = receive_otp;
        this.identityNumber = identityNumber;
        this.addressCompany = addressCompany;
        this.megavnnUsername = megavnnUsername;
        this.sex = sex;
        this.provice = provice;
        this.city = city;
        this.token = token;
        this.gioitinh = gioitinh;
        this.userID = userID;
        this.dateBirth = dateBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReceive_otp() {
        return receive_otp;
    }

    public void setReceive_otp(String receive_otp) {
        this.receive_otp = receive_otp;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getAddressCompany() {
        return addressCompany;
    }

    public void setAddressCompany(String addressCompany) {
        this.addressCompany = addressCompany;
    }

    public String getMegavnnUsername() {
        return megavnnUsername;
    }

    public void setMegavnnUsername(String megavnnUsername) {
        this.megavnnUsername = megavnnUsername;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(int gioitinh) {
        this.gioitinh = gioitinh;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }
}
