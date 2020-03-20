package com.example.myapplication;

public class ObjectBlock {
    private String register_number, object_number, object_name, object_cost, object_owner, register_time, object_information, object_state, object_category;

    public ObjectBlock(String register_number, String object_number, String object_name, String object_information, String object_cost, String object_owner, String register_time, String object_state, String object_category) {
        this.register_number = register_number;
        this.object_number = object_number;
        this.object_name = object_name;
        this.object_information = object_information;
        this.object_cost = object_cost;
        this.object_owner = object_owner;
        this.register_time = register_time;
        this.object_state = object_state;
        this.object_category = object_category;
    }


    public void setRegister_number(String register_number) {this.register_number = register_number;}
    public void setObject_number(String object_number) {this.object_number = object_number;}
    public void setObject_name(String object_name) {this.object_name = object_name;}
    public void setObject_information(String object_information) {this.object_information = object_information;}
    public void setObject_cost(String object_cost) {this.object_cost = object_cost;}
    public void setObject_owner(String object_owner) {this.object_owner = object_owner;}
    public void setRegister_time(String register_time) {this.register_time = register_time;}
    public void setObject_state(String object_state) {this.object_state = object_state;}
    public void setObject_category(String object_category) {this.object_category = object_category;}

    public String getRegister_number(){return register_number;}
    public String getObject_number(){return object_number;}
    public String getObject_name(){return object_name;}
    public String getObject_information(){return object_information;}
    public String getObject_cost(){return object_cost;}
    public String getObject_owner(){return object_owner;}
    public String getRegister_time(){return register_time;}
    public String getObject_state() {return object_state;}
    public String getObject_category() {return object_category;}

}