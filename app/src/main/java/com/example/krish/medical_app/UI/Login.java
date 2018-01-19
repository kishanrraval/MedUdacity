package com.example.krish.medical_app.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krish.medical_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;

import static java.security.AccessController.getContext;

public class Login extends AppCompatActivity {

    protected EditText username;
    protected EditText password;
    protected TextView signup;
    protected TextView login;
    protected TextView forgot_password;
    protected String doctor_username;
    protected DatabaseReference Login ;
    protected String usernm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login);

        String folderPath = Environment.getExternalStorageDirectory() + "/Dentogram/";
        File file = new File(folderPath);
        if(!(file.exists()))
        {
            if(file.mkdirs());

        }

        SharedPreferences sharedPref = getSharedPreferences("doctor_username", MODE_PRIVATE);
        doctor_username = sharedPref.getString("doctor_username", null);
        if (doctor_username != null)
        {
            launch_my_patients(doctor_username);
        }

        Login = FirebaseDatabase.getInstance().getReference();

        username = (EditText) findViewById(R.id.editText_login_email);
        password = (EditText) findViewById(R.id.editText_login_password);
        signup = (TextView) findViewById(R.id.textView_login_signup_btn);
        login = (TextView) findViewById(R.id.textView_login_login_btn);
        forgot_password = (TextView) findViewById(R.id.textView_login_forgot);

        forgot_password.setVisibility(View.INVISIBLE);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_signup();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        usernm = username.getText().toString();

                            if ((dataSnapshot.child(username.getText().toString()).exists()))
                            {
                                if (!(usernm.equals(""))) {

                                    if (dataSnapshot.child(usernm).child("password").getValue().toString().equals(password.getText().toString())) {
                                        SharedPreferences sharedPref = getSharedPreferences("doctor_username", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("doctor_username", usernm);
                                        editor.commit();
                                        launch_my_patients(usernm);
                                    }
                                    else
                                    {
                                        password.setText("");
                                        password.setHint("Incorrect Password");
                                        username.setHintTextColor(Color.WHITE);

                                    }

                                }
                                else
                                {
                                    username.setText("");
                                    username.setHint("Username Required");
                                    password.setText("");

                                }

                            }
                            else
                            {
                                password.setText("");
                                password.setHint("Password");
                                username.setText("");
                                username.setHint("Username does not exist");
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    public void launch_signup() {
        startActivity(new Intent(this, Signup.class));
    }


    public void launch_my_patients(String doc_username) {

        Intent i =new Intent(this, My_patients.class);
        i.putExtra("username",doc_username);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver  , new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        password.setText("");
        password.setHint("Password");
        username.setText("");
        username.setHint("Username");
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
                sb = Snackbar.make(findViewById(R.id.login_ui), "No Internet Connection",Snackbar.LENGTH_INDEFINITE);
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
