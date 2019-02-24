package com.example.nfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfc.service.Authenticate;
import com.example.nfc.service.PayTask;
import com.example.nfc.service.Payment;
import com.example.nfc.service.Product;
import com.example.nfc.service.ProductTask;
import com.example.nfc.service.User;
import com.example.nfc.service.UserTask;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.nfc.NdefRecord.createMime;
import static com.example.nfc.LoginActivity.convertStreamToString;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NfcAdapter.CreateNdefMessageCallback {

    private NfcAdapter nfcAdapter;
    private TextView text;
    private EditText sendSumm;
    private String product_id;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Authenticate.isAuth(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getUsername());
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean products = null;
                try {
                    Payment mPay = new Payment("2268927","100");
                    PayTask getProduct = new PayTask(getApplicationContext(), phone, mPay);
                    products = getProduct.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
         * NFC START
         */
        text = (TextView) findViewById(R.id.text);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        sendSumm = (EditText) findViewById(R.id.send_summ);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if( Authenticate.logOut(getApplicationContext()) )
                this.recreate();
//
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            try {
                processIntent(getIntent());
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        String text = ("{sum:"+sendSumm.getText()+", product_id:"+product_id+"}" + System.currentTimeMillis());
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime(
                        "application/vnd.com.example.android.beam", text.getBytes())
                        /**
                         * The Android Application Record (AAR) is commented out. When a device
                         * receives a push with an AAR in it, the application specified in the AAR
                         * is guaranteed to run. The AAR overrides the tag dispatch system.
                         * You can add it back in to guarantee that this
                         * activity starts when receiving a beamed message. For now, this code
                         * uses the tag dispatch system.
                        */
                        //,NdefRecord.createApplicationRecord("com.example.android.beam")
                });
        return msg;
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) throws JSONException {
        text = (TextView) findViewById(R.id.text);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        String jsonString = new String(msg.getRecords()[0].getPayload());
        //text.setText(jsonString);
        Log.d("MESSAGE GETTED", new String(msg.getRecords()[0].getPayload()));

        Payment mPay;
        Boolean products = null;

        JSONObject jsonArray = new JSONObject(jsonString);
        Gson gson = new Gson();
        mPay = gson.fromJson(String.valueOf(jsonArray), new TypeToken<Payment>() {
        }.getType());

        try {

            PayTask getProduct = new PayTask(getApplicationContext(), phone, mPay);

            products = getProduct.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(products) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Вы успешно получили перевод",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Не получилось",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }








    protected void productInfo(View view)
    {
        ProductTask getProduct = new ProductTask(getApplicationContext());
        Collection<Product> products = null;
        try {
            products = getProduct.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(products!=null) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Log.i("PROOOOOODUCT", products.iterator().next().toString());
            Toast toast = Toast.makeText(context, products.iterator().next().toString(), duration);
            toast.show();
        }

    }

    protected String getUsername()
    {
        UserTask getUser = new UserTask(getApplicationContext());
        User user = null;
        try {
            user = getUser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(user!=null) {
            setSumm();
            phone = user.getPhone();
            return user.getSurname() + " " + user.getFirstName();
        }
        return null;
    }

    protected String getSumm()
    {
        ProductTask getProduct = new ProductTask(getApplicationContext());
        Collection<Product> products = null;
        try {
            products = getProduct.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        product_id = products.iterator().next().getId().toString();
        return products.iterator().next().getBalance();
    }

    protected void setSumm()
    {
        TextView balance = (TextView) findViewById(R.id.sum);
        balance.setText(getSumm());
    }
}