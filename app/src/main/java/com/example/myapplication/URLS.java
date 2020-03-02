package com.example.myapplication;

public class URLS {

    //private static final String URL_ROOT = "http://ec2-54-180-99-222.ap-northeast-2.compute.amazonaws.com:5000";
    private static final String URL_ROOT = "http://ec2-13-124-10-129.ap-northeast-2.compute.amazonaws.com:5000/api";
    public static final String URL_JOIN = URL_ROOT + "/join";
    public static  final String URL_TALK = URL_ROOT+"/talk";
    public static final String URL_CHECK_ID_DUPLICATION = URL_ROOT + "/check_id";
    public static final String URL_CHECK_PHONENUMBER_DUPLICATION = URL_ROOT + "/check_phone_number";
    public static final String URL_AUTH_PHONENUMBER = URL_ROOT + "/auth";
    public static final String URL_FIND_ID = URL_ROOT + "/find_id";
    public static final String URL_CHANGE_PASSWORD_FIRST = URL_ROOT + "/change_password_first";
    public static final String URL_CHANGE_PASSWORD_SECOND = URL_ROOT + "/change_password_second";
    public static final String URL_LOGIN = URL_ROOT + "/login";
}
