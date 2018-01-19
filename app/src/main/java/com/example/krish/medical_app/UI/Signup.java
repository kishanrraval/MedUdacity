package com.example.krish.medical_app.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krish.medical_app.Java_classes.Doctor;
import com.example.krish.medical_app.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

/**
 * Created by parini on 07-06-2017.
 */

public class Signup extends AppCompatActivity
{
    protected EditText email;
    protected EditText username;
    protected EditText password;
    protected EditText confirm_password;
    protected TextView login;
    protected TextView signup;
    protected DatabaseReference check_existing;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.signup);

        check_existing = FirebaseDatabase.getInstance().getReference();

        email = (EditText)findViewById(R.id.editText_signup_email);
        username = (EditText)findViewById(R.id.editText_signup_username);
        password = (EditText)findViewById(R.id.editText_signup_password);
        confirm_password = (EditText) findViewById(R.id.editText_signup_repassword);
        login = (TextView) findViewById(R.id.textView_signup_login_btn);
        signup = (TextView) findViewById(R.id.textView_signup_signup_btn);


        String folderPath = Environment.getExternalStorageDirectory() + "/Dentogram/";
        File file = new File(folderPath);
        if(!(file.exists()))
        {
            if(file.mkdirs());

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_login();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username_s = username.getText().toString();
                final String email_s = email.getText().toString();
                final String password_s = password.getText().toString();

                if (username_s.equals("")) {
                    username.setHint("Enter valid Username");
                } else if (email_s.equals("")) {
                    email.setHint("Enter Email");
                } else {
                    check_existing.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(username.getText().toString()).exists()) {
                                username.setText("");
                                password.setText("");
                                confirm_password.setText("");
                                email.setText("");
                                username.setHint("Username already exists!");
                            } else {
                                if (password.getText().toString().equals(confirm_password.getText().toString())) {
                                    launch_doctor_profile(username_s, password_s, email_s);
                                } else {
                                    password.setText("");
                                    confirm_password.setText("");
                                    confirm_password.setHint("Password didn't match!");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

    }

    public void launch_login()
    {
        startActivity(new Intent(this, Login.class));
    }
    public void launch_doctor_profile(String username,String password,String email)
    {
        Intent i = new Intent(this,Doctor_profile.class);
        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("email",email);
        i.putExtra("ui_status","true");
        startActivity(i);

    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver  , new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    Snackbar sb = null;
    private BroadcastReceiver networkStateReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            boolean isConnected = ni != null &&
                    ni.isConnectedOrConnecting();


            if (isConnected) {
                try{
                    sb.dismiss();
                }
                catch (Exception ex)
                {
                    Log.e("Exception", ex.getStackTrace().toString());
                }
            } else {
                sb = Snackbar.make(findViewById(R.id.signup_ui), "No Internet Connection",Snackbar.LENGTH_INDEFINITE);
                sb.setAction("Start Wifi", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(true);
                    }
                }).setActionTextColor(getResources().getColor(R.color.holo_blue_light));
                sb.show();
            }
        }
    };
}

