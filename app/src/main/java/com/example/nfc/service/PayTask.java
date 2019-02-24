package com.example.nfc.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;


public class PayTask extends AsyncTask<Void, Void, Boolean> {

    private String mServerUrl;
    private Payment mPay;
    private Boolean result = false;
    private Context mContext;
    private String mPhone;

    public PayTask(Context context, String phone, Payment pay) {
        mServerUrl = "https://diybank.aeb-it.ru/api/product";
        mContext = context;
        mPhone = phone;
        mPay = pay;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        try {



            URL url = new URL("https://diybank.aeb-it.ru/api/payment");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty( "Content-type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer "+Authenticate.getToken(mContext));
            OutputStream out = conn.getOutputStream();
            out.write(("productId="+mPay.getProductId()+"&amount="+mPay.getSum()+"&phone="+mPhone).getBytes());
            out.flush();
            int statusCode = conn.getResponseCode();

            Log.d("POST REQUEST:", " The status code is " + statusCode);

            if (statusCode == 400) {
                result = false;
                Log.i("TEEEEEEEEEEEEEE","----------400");
            }
            if (statusCode == 401) {
                result = false;
                Log.i("TEEEEEEEEEEEEEE","----------401");
            }
            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
                String response = convertStreamToString(inputStream);
                Log.d("POST REQUEST:", "The response is " + response);
                result = true;
                Log.i("TEEEEEEEEEEEEEE","----------200");
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        /*mAuthTask = null;
        showProgress(false);

        if (success) {
            if(token != "" && token != null)
                Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }*/
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

