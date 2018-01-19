package com.example.krish.medical_app.UI;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.krish.medical_app.Java_classes.Patient;
import com.example.krish.medical_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import static com.example.krish.medical_app.R.id.center_horizontal;
import static com.example.krish.medical_app.R.id.parent;
import static com.example.krish.medical_app.R.id.textView_view_diagnosis_value;


public class New_patient_info extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    protected EditText firstname;
    protected EditText middlename;
    protected EditText lastname;
    protected TextView patient_id;
    protected RadioButton male;
    protected RadioButton female;
    protected RadioButton other;
    protected TextView dob;
    protected ImageButton calendar;
    protected TextView age;
    protected EditText email;
    protected EditText address;
    protected EditText mobile_num;
    protected EditText phone_num;
    protected EditText diagnosis;
    protected ImageButton delete_diagnosis;
    protected ImageButton add_diagnosis;
    protected EditText medical_history;
    protected EditText o_department;
    protected Spinner department_spinner;
    protected LinearLayout other_department;
    protected LinearLayout layout_diagnosis;
    protected TextView create;
    protected TextView cancel;
    protected String doc_username,pat_id;
    protected Patient patient_obj;
    protected DatabaseReference edit_patient;
    protected int id =0;
    protected int count;
    String s_department, spinner_selection;
    ArrayAdapter<CharSequence> adapter;


    ArrayList<String> diagnosis_array;
    ArrayList<String> diagnosis_key;
    ArrayList<Integer> diagnosis_id;
    ArrayList<EditText> diagnosis_editext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.new_patient_info);

        edit_patient = FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getIntent().getExtras();
        doc_username = bundle.getString("username");
        pat_id = bundle.getString("patient_id");

        diagnosis_array = new ArrayList<>();
        diagnosis_editext = new ArrayList<>();
        diagnosis_key = new ArrayList<>();
        diagnosis_id = new ArrayList<>();

        count=0;

        firstname = (EditText)findViewById(R.id.editText_new_first);
        middlename = (EditText)findViewById(R.id.editText_new_middle);
        lastname = (EditText)findViewById(R.id.editText_new_last);
        patient_id = (TextView)findViewById(R.id.Textview_new_id);
        dob = (TextView) findViewById(R.id.textView_new_dob);
        calendar = (ImageButton) findViewById(R.id.imageButton_new_calendar);
        age = (TextView) findViewById(R.id.editText_new_age);
        email = (EditText)findViewById(R.id.editText_new_email);
        address = (EditText)findViewById(R.id.editText_new_address);
        mobile_num = (EditText)findViewById(R.id.editText_new_mobile);
        phone_num = (EditText)findViewById(R.id.editText_new_phone);
       // diagnosis = (EditText)findViewById(R.id.editText_new_diagnosis_type_1);
        //delete_diagnosis = (ImageButton) findViewById(R.id.imageButton_new_delete_diagnosis);
        add_diagnosis = (ImageButton) findViewById(R.id.imageButton_new_add_diagnosis);
        o_department = (EditText)findViewById(R.id.editText_new_other);
        medical_history = (EditText)findViewById(R.id.editText_new_add_medical_history);
        male = (RadioButton)findViewById(R.id.radio_new_male);
        female = (RadioButton)findViewById(R.id.radio_new_female);
        other = (RadioButton)findViewById(R.id.radio_new_other);
        create = (TextView)findViewById(R.id.textView_new_create);
        cancel = (TextView)findViewById(R.id.textView_new_cancel_btn);
        department_spinner = (Spinner) findViewById(R.id.spinner_new_department);
        other_department = (LinearLayout) findViewById(R.id.linearLayout_new_other);
        layout_diagnosis = (LinearLayout) findViewById(R.id.linearLayout_new_multiple_diagnosis_view);
        //add_diagnosis("");

        other_department.setVisibility(View.GONE);

        patient_id.setText(pat_id);

        department_spinner.setOnItemSelectedListener(this);

        adapter = ArrayAdapter.createFromResource(this, R.array.department_array, R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        department_spinner.setAdapter(adapter);

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(New_patient_info.this);
                dialog.setContentView(R.layout.datepicker);
                dialog.setTitle("Pick a date");

                // set the custom dialog components - text, image and button

                final DatePicker datepick= (DatePicker)dialog.findViewById(R.id.calendarView);
                Button select = (Button) dialog.findViewById(R.id.select);


                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int datei,month,year;
                        String  dmy;
                        datei = datepick.getDayOfMonth();

                        month = datepick.getMonth()+1;
                        year= datepick.getYear();
                        if(month<10 && datei<10) {
                            dmy = "0" + datei + "-0" + month + "-" + year ;
                        }
                        else if(month<10){
                            dmy = "" + datei + "-0" + month + "-" + year ;
                        }
                        else if(datei<10){
                            dmy = "0" + datei + "-" + month + "-" + year ;
                        }
                        else{
                            dmy = "" + datei + "-" + month + "-" + year ;
                        }


                        dialog.dismiss();
                        dob.setText(dmy);
                        String s_age = getAge(year,month,datei);
                        if(s_age.compareTo("NA")==0)
                        {
                            dob.setText("");
                            dob.setHintTextColor(Color.RED);
                            dob.setHint("Enter Valid DOB");
                        }
                        else
                        {
                            dob.setText(dmy);
                            age.setText(s_age + " years");
                        }


                    }
                });

                dialog.show();

            }

        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_fname, s_dob,s_mobile,s_diagnosis,s_address,s_gender;
                s_fname = firstname.getText().toString();
                s_dob = dob.getText().toString();
                s_mobile = mobile_num.getText().toString();
                //s_diagnosis = diagnosis.getText().toString();
                s_address = address.getText().toString();

                /*int count_diagnosis = layout_diagnosis.getChildCount();
                LinearLayout l = null;
                EditText e = null;
                for(int i =0; i<count_diagnosis; i++)
                {
                    l = (LinearLayout) layout_diagnosis.getChildAt(i);
                    e = (EditText) l.getChildAt(1);
                    diagnosis_array.add(e.getText().toString());
                }*/

                if(male.isChecked())
                {
                    s_gender = "Male";
                }
                else if(female.isChecked())
                {
                    s_gender = "Female";
                }
                else if(other.isChecked())
                {
                    s_gender = "Other";
                }
                else
                {
                    s_gender = "NA";
                }

                if(spinner_selection.equals("Other"))
                {
                    if(o_department.getText().toString().equals(""))
                    {
                        o_department.setHintTextColor(Color.RED);
                        o_department.setHint("Required Department");
                    }
                    else
                    {
                        s_department = o_department.getText().toString();
                    }
                }

                //diagnosis_array.add(s_diagnosis);
                for (int i = 0; i< diagnosis_editext.size();i++)
                {
                    diagnosis_array.add(diagnosis_editext.get(i).getText().toString());
                }



                if(s_fname.length()==0)
                {
                    firstname.setHintTextColor(Color.RED);
                    firstname.setHint("Required Firstname");
                }
                else if(s_dob.length()==0)
                {
                    dob.setTextColor(Color.RED);
                    dob.setText("Required DOB");
                }
                else if(s_address.length()==0)
                {
                    address.setHintTextColor(Color.RED);
                    address.setHint("Required Address");
                }
                else if(s_mobile.length()==0)
                {
                    mobile_num.setHintTextColor(Color.RED);
                    mobile_num.setHint("Required Mobile No.");
                }
                /*else if(s_diagnosis.length()==0)
                {
                    diagnosis.setHintTextColor(Color.RED);
                    diagnosis.setHint("Required Diagnosis");
                }*/

                else
                {
                    patient_obj = new Patient(pat_id,s_fname,middlename.getText().toString(),lastname.getText().toString(),s_department,s_gender,s_dob,age.getText().toString(),
                            email.getText().toString(),s_address,s_mobile,phone_num.getText().toString(),diagnosis_array,medical_history.getText().toString());
                    patient_obj.firebase_connect(doc_username);
                    launch_My_patients(doc_username);
                }


            }
        });

        add_diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



/*
                View newLayout_diagnosis = LayoutInflater.from(getBaseContext()).inflate(R.layout.diagnosis_singleview, layout_diagnosis, false);
                EditText diagnosis_value = (EditText) newLayout_diagnosis.findViewById(R.id.editText_diagnosis_singleview);

                ImageButton diagnosis_delete = (ImageButton) newLayout_diagnosis.findViewById(R.id.imageButton_diagnosis_singleview_delete);
*/
               // int id =Integer.parseInt(UUID.randomUUID()+"");

                add_diagnosis("");


            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch_My_patients(doc_username);
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

        spinner_selection = department_spinner.getItemAtPosition(position).toString();

        if(spinner_selection.equals("Other"))
        {
            other_department.setVisibility(View.VISIBLE);
            s_department = o_department.getText().toString();


        }
        else
        {
            other_department.setVisibility(View.INVISIBLE);
            s_department = spinner_selection;
        }

    }

    public void onNothingSelected(AdapterView<?> arg0)
    {
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        diagnosis_array.clear();
        diagnosis_key.clear();
        diagnosis_id.clear();
        diagnosis_editext.clear();

        edit_patient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot d1 = dataSnapshot.child(doc_username).child("patients").child(pat_id);
                if(d1.exists()) {
                    if(count==0)
                    {
                        for (DataSnapshot postSnapshot : dataSnapshot.child(doc_username).child("patients").child(pat_id).child("patient_diagnosis").getChildren())
                        {
                            String text = postSnapshot.getValue().toString();
                            String key = postSnapshot.getKey();
                            diagnosis_key.add(key);
                            add_diagnosis(text);
                        }
                        count = 1;
                    }


                    int pos = 0;
                    for (int i = 0; i < 8; i++)
                    {
                        if (department_spinner.getItemAtPosition(i).equals(d1.child("patient_department").getValue().toString()))
                        {
                            pos = i;
                            break;
                        }
                        else
                        {
                            pos = 8;
                        }

                    }
                    if(pos == 8)
                    {
                        other_department.setVisibility(View.VISIBLE);
                        Log.i(d1.child("patient_department").getValue().toString(),"department");
                        o_department.setText(d1.child("patient_department").getValue().toString());
                    }

                    department_spinner.setSelection(pos);
                    firstname.setText(d1.child("patient_first_name").getValue().toString());
                    middlename.setText(d1.child("patient_middle_name").getValue().toString());
                    lastname.setText(d1.child("patient_last_name").getValue().toString());
                    patient_id.setText(d1.getKey().toString());
                    dob.setText(d1.child("patient_dob").getValue().toString());
                    age.setText(getAge(dob.getText().toString())+" years");
                    email.setText(d1.child("patient_email").getValue().toString());
                    address.setText(d1.child("patient_address").getValue().toString());
                    mobile_num.setText(d1.child("patient_mobile").getValue().toString());
                    phone_num.setText(d1.child("patient_phone").getValue().toString());
                    //diagnosis.setText(d1.child("patient_diagnosis").getValue().toString());
                    medical_history.setText(d1.child("patient_medical_history").getValue().toString());
                    String gender_s = d1.child("patient_gender").getValue().toString();
                    if (gender_s.equals("Male")) {
                        male.setChecked(true);
                    }
                    else if (gender_s.equals("Female"))
                    {
                        female.setChecked(true);
                    }
                    else if (gender_s.equals("Other"))
                    {
                        other.setChecked(true);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void launch_My_patients(String doc_username)
    {
        Intent i = new Intent(this,My_patients.class);
        i.putExtra("username",doc_username);
        startActivity(i);
    }

    public String getAge(int year, int month, int day){
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

    public String getAge(String v_dob)
    {
        String[] temp = v_dob.split("-");
        int year, month, day;

        day = Integer.parseInt(temp[0]);
        month = Integer.parseInt(temp[1]);
        year = Integer.parseInt(temp[2]);

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


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver  , new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
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
                sb = Snackbar.make(findViewById(R.id.new_patient_ui), "No Internet Connection",Snackbar.LENGTH_INDEFINITE);
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

    @Override
    public void onBackPressed() {
    }

    public void add_diagnosis(String text)
    {
        final EditText diagnosis_value = new EditText(New_patient_info.this);
        diagnosis_value.setHint("Type diagnosis");
        diagnosis_value.setId(id);
        LinearLayout.LayoutParams edit = new LinearLayout.LayoutParams((int)convertDpToPixel(230,New_patient_info.this), LinearLayout.LayoutParams.MATCH_PARENT);
        edit.width = (int)convertDpToPixel(230,New_patient_info.this);
        edit.height = (int)convertDpToPixel(45,New_patient_info.this);
        edit.setMarginStart((int)convertDpToPixel(20,New_patient_info.this));
        edit.setMarginEnd((int)convertDpToPixel(20,New_patient_info.this));
        diagnosis_value.setSingleLine(true);
        diagnosis_value.setLayoutParams(edit);
        diagnosis_value.setText(text);
        diagnosis_editext.add(diagnosis_value);

        final ImageView tp = new ImageView(New_patient_info.this);
        tp.setBackgroundResource(R.drawable.type_diagnosis);
        tp.setMinimumWidth((int)convertDpToPixel(30,New_patient_info.this));
        tp.setMinimumHeight((int)convertDpToPixel(30,New_patient_info.this));
        tp.setClickable(false);
        LinearLayout.LayoutParams img = new LinearLayout.LayoutParams((int)convertDpToPixel(30,New_patient_info.this), (int)convertDpToPixel(30,New_patient_info.this));
        img.width = (int)convertDpToPixel(30,New_patient_info.this);
        img.height = (int)convertDpToPixel(30,New_patient_info.this);
        img.gravity = Gravity.CENTER;
        img.setMarginStart((int)convertDpToPixel(20,New_patient_info.this));
        tp.setPadding((int)convertDpToPixel(5,New_patient_info.this), (int)convertDpToPixel(5,New_patient_info.this), (int)convertDpToPixel(5,New_patient_info.this), (int)convertDpToPixel(5,New_patient_info.this));
        tp.setLayoutParams(img);

        //int id_del =Integer.parseInt(UUID.randomUUID()+"");

        final ImageView del = new ImageView(New_patient_info.this);
        del.setBackgroundResource(R.drawable.delete_diagnosis);
        del.setId(id);
        del.setMinimumWidth((int)convertDpToPixel(30,New_patient_info.this));
        del.setMinimumHeight((int)convertDpToPixel(30,New_patient_info.this));
        del.setClickable(false);
        LinearLayout.LayoutParams img_del = new LinearLayout.LayoutParams((int)convertDpToPixel(30,New_patient_info.this), (int)convertDpToPixel(30,New_patient_info.this));
        img_del.width = (int)convertDpToPixel(30,New_patient_info.this);
        img_del.height = (int)convertDpToPixel(30,New_patient_info.this);
        img_del.gravity = Gravity.CENTER;
        img_del.setMarginEnd((int)convertDpToPixel(10,New_patient_info.this));
        del.setPadding((int)convertDpToPixel(5,New_patient_info.this), (int)convertDpToPixel(5,New_patient_info.this), (int)convertDpToPixel(5,New_patient_info.this), (int)convertDpToPixel(5,New_patient_info.this));
        del.setLayoutParams(img_del);

        final LinearLayout lay = new LinearLayout(New_patient_info.this);
        LinearLayout.LayoutParams lay_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lay.setLayoutParams(lay_params);

        lay.addView(tp);
        lay.addView(diagnosis_value);
        lay.addView(del);

        layout_diagnosis.addView(lay);
        diagnosis_id.add(id);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int del_id = del.getId();
                if(del_id<diagnosis_key.size())
                {
                    int i = diagnosis_id.indexOf(del_id);
                    edit_patient.child(doc_username).child("patients").child(pat_id).child("patient_diagnosis").child(diagnosis_key.get(i)).removeValue();
                }
                diagnosis_editext.remove(diagnosis_value);
                layout_diagnosis.removeView(lay);
                int d = diagnosis_id.indexOf(del_id);
                diagnosis_id.remove(d);
            }
        });

        id++;

    }
}
