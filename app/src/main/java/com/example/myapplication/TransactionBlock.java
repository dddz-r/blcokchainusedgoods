package com.example.myapplication;

public class TransactionBlock {
    private String transactionNumber, register_number, seller_id, buyer_id, complete_time;

    public TransactionBlock(String transactionNumber, String register_number, String seller_id, String buyer_id, String complete_time) {
        this.transactionNumber = transactionNumber;
        this.register_number = register_number;
        this.seller_id = seller_id;
        this.buyer_id = buyer_id;
        this.complete_time = complete_time;
    }

    public void setTransactionNumber(String transactionNumber) {this.transactionNumber = transactionNumber;}
    public void setRegister_number(String register_number) {this.register_number = register_number;}
    public void setSeller_id(String seller_id) {this.seller_id = seller_id;}
    public void setBuyer_id(String buyer_id) {this.buyer_id = buyer_id;}
    public void setComplete_time(String complete_time) {this.complete_time = complete_time;}

    public String getTransactionNumber(){return transactionNumber;}
    public String getRegister_number(){return register_number;}
    public String getSeller_id(){return seller_id;}
    public String getBuyer_id(){return buyer_id;}
    public String getComplete_time(){return complete_time;}

}