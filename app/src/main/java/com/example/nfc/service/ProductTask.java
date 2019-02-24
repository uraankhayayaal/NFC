package com.example.nfc.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class ProductTask extends AsyncTask<Void, Void, Collection<Product>> {

    private String mServerUrl;
    private Collection<Product> mProducts;
    private Context mContext;

    public ProductTask(Context context) {
        mServerUrl = "https://diybank.aeb-it.ru/api/product";
        mContext = context;
    }

    @Override
    protected Collection<Product> doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        try {
            URL url = new URL(mServerUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "Bearer "+Authenticate.getToken(mContext));

            int statusCode = httpURLConnection.getResponseCode();
            Log.d("POST REQUEST:", " The status code is " + statusCode);

            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                String response = convertStreamToString(inputStream);
                Log.d("POST REQUEST:", "The response is " + response);

                JSONArray jsonArray = new JSONArray(response);
                Gson gson = new Gson();
                mProducts = gson.fromJson(String.valueOf(jsonArray), new TypeToken<Collection<Product>>(){}.getType());
                //System.out.println(String.valueOf(jsonObject));
                //return true;
            } else {
                return null;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Authenticate.login(getApplicationContext(), token);

        return mProducts;
    }

    @Override
    protected void onPostExecute(final Collection<Product> success) {
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
