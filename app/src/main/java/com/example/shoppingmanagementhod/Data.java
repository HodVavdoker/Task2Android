package com.example.shoppingmanagementhod;

public class Data {
    public  String email;
    public  String password;
    public  String phone;

    public Data(){

    }
    public Data(String email, String password, String phone) {
        this.email = email;
        this.password = password;
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "Data{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
