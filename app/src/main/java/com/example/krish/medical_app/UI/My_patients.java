package com.example.krish.medical_app.UI;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.text.TextWatcher;


import com.example.krish.medical_app.Adapters.PatientAdapter;
import com.example.krish.medical_app.Java_classes.Patient;
import com.example.krish.medical_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.krish.medical_app.R.drawable.s;

/**
 * Created by KRISH on 13-06-2017.
 */

public class My_patients extends AppCompatActivity {

    protected ImageButton options;
    protected ImageButton search_imageView;
    protected EditText search;
    protected ImageButton add_patient;
    protected ListView listView;
    protected String doc_username;
    protected PatientAdapter patientadapter;
    protected DatabaseReference existing_patients;
    protected Patient patient;
    protected ListView patient_list;
    protected String patient_id;
    protected DrawerLayout drawerLayout;
    protected TextView logout_btn;
    protected TextView profile_btn;
    protected TextView doc_name;
    protected TextView delete_acc;
    protected TextView my_patients;
    protected AVLoadingIndicatorView avi;

    ArrayList<String> patient_name;
    ArrayList<Patient> patient_array;
    //ArrayList<String> patient_diagnosis;

    ArrayList<String> patients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.my_patients);


        Bundle bundle = getIntent().getExtras();
        doc_username = bundle.getString("username");

        SharedPreferences sharedPref = getSharedPreferences("doctor_username", MODE_PRIVATE);
        doc_username = sharedPref.getString("doctor_username", null);

        existing_patients = FirebaseDatabase.getInstance().getReference();

        patient_array = new ArrayList<>();
        //patient_diagnosis = new ArrayList<>();
        patient_name = new ArrayList<String>();

        patients = new ArrayList<String>();

        search_imageView = (ImageButton) findViewById(R.id.imageView_search);
        search = (EditText) findViewById(R.id.filter_search);
        options = (ImageButton) findViewById(R.id.imageButton_my_patients_options);
        add_patient = (ImageButton) findViewById(R.id.imageButton_my_patients_add_patient);
        listView = (ListView) findViewById(R.id.listView_my_patients);
        logout_btn = (TextView) findViewById(R.id.logout_btn);
        profile_btn = (TextView) findViewById(R.id.pro_btn);
        doc_name = (TextView) findViewById(R.id.textView_navigation_fullname);
        my_patients = (TextView) findViewById(R.id.textView_my_patients_patients);
        delete_acc = (TextView) findViewById(R.id.delete_account_btn);
        avi =  (AVLoadingIndicatorView) findViewById(R.id.avi);

        avi.show();

        search.setVisibility(View.GONE);

        //Adding listener to searchView\
       /* new search.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

        });
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Text searched",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
*/

        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);

        add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                patient_id = existing_patients.child(doc_username).child("patients:").push().getKey();
                launch_new_patient_info(doc_username, patient_id);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                existing_patients.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       String password= dataSnapshot.child(doc_username).child("password").getValue().toString();
                       String email= dataSnapshot.child(doc_username).child("email").getValue().toString();
                        launch_doc_profile(doc_username, password, email);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        delete_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailogopner_delete_acc();
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launch_login();
            }
        });


        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.openDrawer(Gravity.START);

                    existing_patients.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(doc_username).child("name").exists())
                            {
                                doc_name.setText(dataSnapshot.child(doc_username).child("name").getValue().toString());
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        search_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_patients.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
            }
        });

        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do some thing now
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Change the TextView background color
                Log.i("-----------=====-----" , search.getText().toString());


               Log.i("kpsdjhkajsdhf------   " , search.getText().toString());
                search(search.getText().toString());
                // Get the EditText text and display it on TextView

            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.i("hereeeeeeee","bla");

                search.setVisibility(View.GONE);
                my_patients.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        patientadapter = new PatientAdapter(getApplicationContext(), patient_array);
        patient_list = (ListView) findViewById(R.id.listView_my_patients);
        patient_list.setAdapter(patientadapter);

        patient_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Patient p = patient_array.get(i);
                Intent in = new Intent(My_patients.this, View_patient.class);
                in.putExtra("patient_id", p.getPatient_id());
                in.putExtra("username", doc_username);
                startActivity(in);
            }
        });


        existing_patients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                patientadapter.clear();

                if(doc_username!=null)
                {
                    for (DataSnapshot postSnapshot : dataSnapshot.child(doc_username).child("patients").getChildren()) {
                        String name = postSnapshot.child("patient_first_name").getValue().toString();
                        String lname = postSnapshot.child("patient_last_name").getValue().toString();
                        String department = postSnapshot.child("patient_department").getValue().toString();
                        String dob = postSnapshot.child("patient_dob").getValue().toString();
                        String age = getAge(dob);
                        String gender = postSnapshot.child("patient_gender").getValue().toString();
                        String id = postSnapshot.getKey().toString();
                        patient = new Patient(id, name, null, lname, department, gender, null, age, null, null, null, null, null, null);
                        patient_array.add(patient);
                        patient_name.add(name);
                        patientadapter.notifyDataSetChanged();
                    }
                    avi.hide();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void search(String s)
    {
        System.out.println("0---in search");

        for(int i = 0;i<patient_name.size();i++)
        {
            System.out.println(">>>>   " + patient_name.get(i));

            if(patient_name.get(i).contains(s))
            {
                patients.add(patient_name.get(i));
            }
        }

    }

    public void launch_new_patient_info(String doc_username, String patient_id) {
        Intent i = new Intent(this, New_patient_info.class);
        i.putExtra("username", doc_username);
        i.putExtra("patient_id", patient_id);
        startActivity(i);
    }

    public void launch_login() {

        SharedPreferences sharedPref = getSharedPreferences("doctor_username", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("doctor_username",null);
        editor.apply();
        startActivity(new Intent(this, Login.class));
    }

    public void launch_doc_profile(String doc_username, String password, String email)
    {
        Intent i = new Intent(this,Doctor_profile.class);
        i. putExtra("username",doc_username);
        i.putExtra("password",password);
        i.putExtra("email",email);
        i.putExtra("ui_status","false");
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public String getAge(String v_dob)
    {
        String s1, s2, s3;
        s1 = ""+ v_dob.charAt(0) + v_dob.charAt(1);
        s2 = "" + v_dob.charAt(3) + v_dob.charAt(4);
        s3 = "" + v_dob.charAt(6) + v_dob.charAt(7) + v_dob.charAt(8) + v_dob.charAt(9);

        int day,month,year;

        day = Integer.parseInt(s1);
        month = Integer.parseInt(s2);
        year = Integer.parseInt(s3);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        String ageS;
        if(age<0) {
            ageS = "NA";

        }
        else
        {
            Integer ageInt = new Integer(age);
            ageS = ageInt.toString();
        }

        return ageS;
    }

    public void dailogopner_delete_acc()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_popup);

        final TextView delete = (TextView) dialog.findViewById(R.id.textView_delete_delete);
        TextView cancel = (TextView) dialog.findViewById(R.id.textView_delete_cancel);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_doctor(doc_username);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    public void delete_doctor(String doc_username)
    {
        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();
        d1.child(doc_username).removeValue();
        launch_login();
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
                sb = Snackbar.make(findViewById(R.id.draw_layout), "No Internet Connection",Snackbar.LENGTH_INDEFINITE);
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
