package com.example.nfc.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.nfc.LoginActivity;


/**
 * Created by uraan on 04.12.2016.
 */

public class Authenticate {

    private static final String APP_TOKEN_KEY = "com.example.nfc.token";
    private static final String APP_TOKEN_EMPTY_VALUE = "";

    public static void login(Context contex, String token){
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(contex);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(APP_TOKEN_KEY, token);
        editor.commit();
        Log.i("Setting token ------- ",token);
    }

    public static String getToken(Context contex){
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(contex);
        String auth_token_string = settings.getString(APP_TOKEN_KEY, APP_TOKEN_EMPTY_VALUE);
        Log.i("Using token ------- ",auth_token_string);
        return auth_token_string;
    }

    public static void isAuth(Context contex){
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(contex);
        String auth_token_string = settings.getString(APP_TOKEN_KEY, APP_TOKEN_EMPTY_VALUE);
        Log.i("Getting token ------- ",auth_token_string);
        if(auth_token_string == APP_TOKEN_EMPTY_VALUE){
            Intent intent = new Intent(contex, LoginActivity.class);
            contex.startActivity(intent);
        }
    }

    public static boolean logOut(Context contex){
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(contex);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(APP_TOKEN_KEY);
        if(editor.commit())
            return true;
        else
            return false;
    }
}
