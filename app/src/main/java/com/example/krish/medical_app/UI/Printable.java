package com.example.krish.medical_app.UI;

/**
 * Created by KRISH on 08-07-2017.
 */

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krish.medical_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.id;

public class Printable extends AppCompatActivity implements View.OnClickListener {

    protected DatabaseReference pdf;
    protected String v_name, v_gender, v_dob, v_diagnosis, v_mobile, v_phone, v_date, v_medhis, v_reffered, v_department;
    protected TextView patient_name;
    protected TextView age;
    protected TextView patient_gender;
    protected TextView mobile;
    protected TextView date;
    protected TextView doctor;
    protected TextView department;
    protected TextView diagnosis;
    protected TextView medical_history;
    protected TextView notes;
    protected TextView create;
    protected ImageView back;
    protected LinearLayout note_list;
    protected String doc_username, pat_id;
    LinearLayout pdflayout;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.printable);
        Bundle bundle = getIntent().getExtras();
        doc_username = bundle.getString("username");
        pat_id = bundle.getString("patient_id");
        init();
        pdf = FirebaseDatabase.getInstance().getReference();
        fn_permission();
        listener();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch_view_patients(doc_username, pat_id);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        pdf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot d1 = dataSnapshot.child(doc_username).child("patients").child(pat_id);

                if (d1.exists()) {
                    v_name = d1.child("patient_first_name").getValue().toString() + " " + d1.child("patient_last_name").getValue().toString();
                    v_gender = d1.child("patient_gender").getValue().toString();
                    v_dob = d1.child("patient_dob").getValue().toString();
                    v_diagnosis = d1.child("patient_diagnosis").getValue().toString();
                    v_mobile = d1.child("patient_mobile").getValue().toString();
                    v_phone = d1.child("patient_phone").getValue().toString();
                    v_medhis = d1.child("patient_medical_history").getValue().toString();
                    v_reffered = dataSnapshot.child(doc_username).child("name").getValue().toString();
                    v_department = d1.child("patient_department").getValue().toString();
                    v_date = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));

                    String note_text = "";
                    for (DataSnapshot postSnapshot : dataSnapshot.child(doc_username).child("patients").child(pat_id).child("notes").getChildren()) {
                        String id = postSnapshot.getKey();
                        note_text += postSnapshot.child("note_title").getValue().toString() +" - " + postSnapshot.child("note_text").getValue().toString() ;
                        note_text += "\n";

                    }

                    View newLayout_notes = LayoutInflater.from(getBaseContext()).inflate(R.layout.print_singleview, note_list, false);

                    TextView note_n = (TextView) newLayout_notes.findViewById(R.id.textView_print_singleview);

                    note_n.setText(note_text);

                    newLayout_notes.setTag(id);
                    newLayout_notes.setClickable(true);
                    newLayout_notes.setFocusable(true);

                    note_list.addView(newLayout_notes);



                    patient_name.setText(v_name);
                    age.setText(getAge(v_dob) + " years");
                    patient_gender.setText(v_gender);
                    mobile.setText(v_mobile);
                    date.setText(v_date);
                    doctor.setText(v_reffered);
                    department.setText(v_department);
                    diagnosis.setText(v_diagnosis);
                    medical_history.setText(v_medhis);



                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {

        patient_name = (TextView) findViewById(R.id.textView_print_name_value);
        age = (TextView) findViewById(R.id.textView_print_age_value);
        patient_gender = (TextView) findViewById(R.id.textView_print_gender_value);
        mobile = (TextView) findViewById(R.id.textView_print_mobile_value);
        date = (TextView) findViewById(R.id.textView_print_date_value);
        doctor = (TextView) findViewById(R.id.textView_print_reffered_value);
        department = (TextView) findViewById(R.id.textView_print_department_value);
        diagnosis = (TextView) findViewById(R.id.textView_print_diagnosis_value);
        medical_history = (TextView) findViewById(R.id.textView_print_medical_history_value);
        //notes = (TextView) findViewById(R.id.textView_print_notes_value);
        create = (TextView) findViewById(R.id.textView_printable_create_pdf);
        pdflayout = (LinearLayout) findViewById(R.id.linearLayout_printable);
        back = (ImageView) findViewById(R.id.imageView_print_back);
        note_list = (LinearLayout)findViewById(R.id.LinearLayout_print_notes_value);

    }

    private void listener() {
        create.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.textView_printable_create_pdf:

                if (boolean_save) {
                    Intent i = new Intent(getApplicationContext(), Pdf_view.class);
                    i.putExtra("username", doc_username);
                    i.putExtra("patient_id", pat_id);
                    startActivity(i);

                } else {
                    if (boolean_permission) {
                        progressDialog = new ProgressDialog(Printable.this);
                        progressDialog.setMessage("Please wait");
                        bitmap = loadBitmapFromView(pdflayout, pdflayout.getWidth(), pdflayout.getHeight());
                        createPdf();
//                        saveBitmap(bitmap);
                    } else {

                    }

                    createPdf();
                    break;
                }
        }
    }

    public void launch_view_patients(String doc_username, String pat_id) {
        Intent i = new Intent(this, View_patient.class);
        i.putExtra("username", doc_username);
        i.putExtra("patient_id", pat_id);
        startActivity(i);
    }

    private void createPdf() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float height = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHeight = (int) height, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);


        // write the document content

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        String v_time = currentDateandTime + "";

        final String targetPdf = "/sdcard/Dentogram/" + v_name +"-"+ v_time +".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            //           Toast.makeText(getApplicationContext(), "Created Successfully", Toast.LENGTH_SHORT).show();
            //          Toast.makeText(getApplicationContext(), "FIleManager -> sdcard -> Dentogram -> "+v_name+".pdf", Toast.LENGTH_SHORT).show();
            Snackbar sb1 = Snackbar.make(findViewById(R.id.printable_ui), "PDF Created Successfully", Snackbar.LENGTH_INDEFINITE);
            sb1.setAction("Open PDF", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(targetPdf);
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(Uri.fromFile(file), "application/pdf");
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    Intent intent = Intent.createChooser(target, "Open File");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // Instruct the user to install a PDF reader here, or something
                    }
                }
            }).setActionTextColor(getResources().getColor(R.color.holo_blue_light));
            sb1.show();
            boolean_save = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }


    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Printable.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(Printable.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Printable.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(Printable.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

    public String getAge(String v_dob) {
        String[] temp = v_dob.split("-");
        int year, month, day;

        day = Integer.parseInt(temp[0]);
        month = Integer.parseInt(temp[1]);
        year = Integer.parseInt(temp[2]);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        String ageS;
        if (age < 0) {
            ageS = "NA";

        } else {
            Integer ageInt = new Integer(age);
            ageS = ageInt.toString();
        }

        return ageS;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    Snackbar sb = null;
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            boolean isConnected = ni != null &&
                    ni.isConnectedOrConnecting();


            if (isConnected) {
                try {
                    sb.dismiss();
                } catch (Exception ex) {
                    Log.e("Exception", ex.getStackTrace().toString());
                }
            } else {
                sb = Snackbar.make(findViewById(R.id.printable_ui), "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
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
}
