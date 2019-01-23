package com.txs.mobilesafe.domain;

public class BlacknumInfo {
    public String mode;
    public String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
        switch (mode){
            case "1":
                this.mode = "拦截短信";
                break;
            case "2":
                this.mode = "拦截电话";
                break;
            case "3":
                this.mode = "拦截所有";
                break;
        }
    }
}
