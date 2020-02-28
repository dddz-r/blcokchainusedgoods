package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class PrefManager {

    private static final String SHARED_PREF_NAME = "pref";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_PHONE_NUMBER = "user_phone_number";
    private static final String KEY_ADDRESS = "user_address";
    private static final String KEY_ACCOUNT = "user_account";
    private static final String KEY_IS_LOGGED_IN = "is_logged-in";
    private static PrefManager instance;
    private static Context ctx;

    //constructor
    private PrefManager(Context context){

        ctx = context;
    }

    public static synchronized  PrefManager getInstance(Context context){

        if(instance ==null){
            instance = new PrefManager(context);
        }

        return instance;
    }

    public void setUserLogin(User user){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getUser_id());
        editor.putString(KEY_NAME, user.getUser_name());
        editor.putString(KEY_PHONE_NUMBER, user.getUser_phone_number());
        editor.putString(KEY_ADDRESS, user.getUser_address());
        editor.putString(KEY_ACCOUNT, user.getUser_account());
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn(){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public User getUser(){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_PHONE_NUMBER, null),
                sharedPreferences.getString(KEY_ADDRESS, null),
                sharedPreferences.getString(KEY_ACCOUNT, null)
        );
    }

    public void logout() {

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));

    }
}
