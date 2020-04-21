package com.example.myapplication;

public class URLS {
  

    private static final String URL_ROOT = "http://ec2-52-79-133-153.ap-northeast-2.compute.amazonaws.com:5000/api";
    public static final String URL_JOIN = URL_ROOT + "/join";
    public static  final String URL_TALK_SEND = URL_ROOT+"/talk_send";
    public static  final String URL_TALK_RECEIVE = URL_ROOT+"/talk_receive";
    public static final String URL_TALK_LIST = URL_ROOT+"/talk_list";
    public static final String URL_REVIEW_WRITE = URL_ROOT+"/review_write";
    public static final String URL_REVIEW_READ = URL_ROOT+"/review_read";
    public static final String URL_CHECK_ID_DUPLICATION = URL_ROOT + "/check_id";
    public static final String URL_CHECK_PHONENUMBER_DUPLICATION = URL_ROOT + "/check_phone_number";
    public static final String URL_AUTH_PHONENUMBER = URL_ROOT + "/auth";
    public static final String URL_FIND_ID = URL_ROOT + "/find_id";
    public static final String URL_CHANGE_PASSWORD_FIRST = URL_ROOT + "/change_password_first";
    public static final String URL_CHANGE_PASSWORD_SECOND = URL_ROOT + "/change_password_second";
    public static final String URL_LOGIN = URL_ROOT + "/login";
    public static final String URL_SENDING_AUTH_MASSEGE = URL_ROOT + "/send";
    public static final String URL_INSERT_OBJECT = URL_ROOT + "/insert_object";
    public static final String URL_INSERT_TRANSACTION = URL_ROOT + "/insert_transaction";
    public static final String URL_GET_OBJECT_DB = URL_ROOT + "/get_object_database";
    public static final String URL_GET_TRANSACTION_DB = URL_ROOT + "/get_transaction_database";
    public static final String URL_GET_OBJECT_BLOCK  = URL_ROOT + "/get_object_block";
    public static final String URL_GET_TRANSACTION_BLOCK  = URL_ROOT + "/get_transaction_block";
    public static final String URL_GET_OBJECT_GRIDVIEW  = URL_ROOT + "/get_object_gridview";
    public static final String URL_GET_TRANSACTION_SELL_LIST  = URL_ROOT + "/get_transaction_sell_list";
    public static final String URL_GET_TRANSACTION_BUY_LIST  = URL_ROOT + "/get_transaction_buy_list";
    public static final String  URL_UPDATE_OBJECT_STATE = URL_ROOT + "/update_object_state";
    public static final String  URL_UPDATE_BUY_REQ_ID = URL_ROOT + "/update_object_buy_req_id";
    public static final String  URL_GET_OBJECT_REGISTER_NUMBER = URL_ROOT + "/get_object_register_number";
    public static final String URL_GET_ON_DEAL_LIST = URL_ROOT + "/get_object_on_deal_list";
    public static final String URL_STORE_IMAGE = URL_ROOT + "/upload";
    public static final String URL_UPLOAD = "http://ec2-52-79-133-153.ap-northeast-2.compute.amazonaws.com:5000";

}
