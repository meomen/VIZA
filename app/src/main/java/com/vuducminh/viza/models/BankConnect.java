package com.vuducminh.viza.models;

public class BankConnect {
    private String BankName;
    private String codeAccount;
    private String NameOwner;
    private String LogoBank;

    public BankConnect(String bankName, String codeAccount, String nameOwner, String logoBank) {
        BankName = bankName;
        this.codeAccount = codeAccount;
        NameOwner = nameOwner;
        LogoBank = logoBank;
    }

    public BankConnect() {
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getCodeAccount() {
        return codeAccount;
    }

    public void setCodeAccount(String codeAccount) {
        this.codeAccount = codeAccount;
    }

    public String getNameOwner() {
        return NameOwner;
    }

    public void setNameOwner(String nameOwner) {
        NameOwner = nameOwner;
    }

    public String getLogoBank() {
        return LogoBank;
    }

    public void setLogoBank(String logoBank) {
        LogoBank = logoBank;
    }
}
