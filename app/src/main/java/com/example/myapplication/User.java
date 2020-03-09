package com.example.myapplication;

public class User {

    private String user_id, user_name, user_phone_number, user_address, user_account;


    public User(String user_id, String user_name, String user_phone_number, String user_address, String user_account){

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone_number = user_phone_number;
        this.user_address = user_address;
        this.user_account = user_account;
    //    this.user_token = user_token;
    }

    public String getUser_id() { return user_id; }
    public String getUser_name() { return user_name; }
    public String getUser_phone_number() { return user_phone_number; }
    public String getUser_address() { return user_address; }
    public String getUser_account() { return user_account; }
    //public String getUser_token() { return user_token; }
}
