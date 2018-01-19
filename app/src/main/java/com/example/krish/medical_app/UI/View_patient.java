package com.example.krish.medical_app.UI;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.krish.medical_app.Java_classes.Note;
import com.example.krish.medical_app.Java_classes.Picture;
import com.example.krish.medical_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.master.permissionhelper.PermissionHelper;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by KRISH on 08-06-2017.
 */

public class View_patient extends AppCompatActivity {

    // private FirebaseStorage storage = FirebaseStorage.getInstance();


    protected ImageButton back;
    protected ImageButton more;
    protected TextView patient;
    protected TextView patient_name;
    protected TextView gender;
    protected TextView age;
    protected TextView dob_value;
    protected TextView id_value;
    protected TextView diagnosis_value;
    protected TextView mobile_value;
    protected TextView phone_value;
    protected TextView reffered_by;
    protected TextView department;
    protected TextView medical_history_value;
    protected ImageButton notes;
    protected ImageButton images;
    protected AVLoadingIndicatorView avi;
    protected String doc_username, pat_id,image_id;
    protected DatabaseReference view_patient, note_ref;
    protected StorageReference photos_storage;
    protected ArrayList<Note> note_array;
    protected ArrayList<Picture> picture_array;
    //protected NoteAdapter noteadapter;
    //protected PictureAdapter pictureadapter;
    protected LinearLayout note_list;
    protected LinearLayout picture_list;
    protected Dialog dialog_images;
   // protected Dialog dialog_view_image;
   // protected ImageView imagevw;
    protected ImageView img_p;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.view_patient);


        Bundle bundle = getIntent().getExtras();
        doc_username = bundle.getString("username");
        pat_id = bundle.getString("patient_id");

        note_array = new ArrayList<>();
        picture_array = new ArrayList<>();

        view_patient = FirebaseDatabase.getInstance().getReference();
        note_ref = view_patient.child(doc_username).child("patients").child(pat_id).child("notes");

        back = (ImageButton) findViewById(R.id.imageButton_view_back);
        more = (ImageButton) findViewById(R.id.imageButton_view_more);
        patient = (TextView) findViewById(R.id.textView_view_patient);
        reffered_by = (TextView) findViewById(R.id.textView_view_id_reffered);
        department = (TextView) findViewById(R.id.textView_view_id_department);
        patient_name = (TextView) findViewById(R.id.textView_view_patient_name);
        gender = (TextView) findViewById(R.id.textView_view_gender);
        age = (TextView) findViewById(R.id.textView_view_age);
        dob_value = (TextView) findViewById(R.id.textView_view_dob_value);
        id_value = (TextView) findViewById(R.id.textView_view_id_value);
        diagnosis_value = (TextView) findViewById(R.id.textView_view_diagnosis_value);
        mobile_value = (TextView) findViewById(R.id.textView_view_mobile_value);
        phone_value = (TextView) findViewById(R.id.textView_view_phone_value);
        medical_history_value = (TextView) findViewById(R.id.textView_view_medical_history_value);
        notes = (ImageButton) findViewById(R.id.imageButton_view_notes);
        images = (ImageButton) findViewById(R.id.imageButton_view_images);

        avi =  (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.hide();
       // imagevw = (ImageView) findViewById(R.id.imageView_view_image);

       // noteadapter = new NoteAdapter(getApplicationContext(), note_array);
        note_list = (LinearLayout) findViewById(R.id.listView_notes);


        //note_list.setAdapter(noteadapter);

        /*note_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Note n = note_array.get(i);
                Intent in = new Intent(View_patient.this, Notes.class);
                in.putExtra("patient_id", pat_id);
                in.putExtra("username", doc_username);
                in.putExtra("note_id", n.getNotes_id());
                startActivity(in);
            }
        });

        note_list.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
// Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
// Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
// Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });*/

       // pictureadapter = new PictureAdapter(getApplicationContext(), picture_array);
        picture_list = (LinearLayout) findViewById(R.id.listView_photos);
       /* picture_list.setAdapter(pictureadapter);

        picture_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Picture p = picture_array.get(i);
                dialogopener_view_image(p);
            }
        });

        picture_list.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
// Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
// Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
// Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch_my_patients(doc_username);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(View_patient.this, more);
                menu.getMenuInflater().inflate(R.menu.patient_profile_options, menu.getMenu());

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.patient_profile_options_edit:
                                launch_new_patient_info(doc_username, pat_id);
                                return true;

                            case R.id.patient_profile_options_save_printable_copy:
                                launch_printable(doc_username, pat_id);
                                return true;

                            case R.id.patient_profile_options_delete:
                                dialogopener();
                                return true;

                            default:
                                return true;
                        }

                    }
                });

                menu.show();
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch_notes(doc_username, pat_id);
            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(isReadAllowedofCamera()) && !(isReadAllowedofRead()) && !(isReadAllowedofWrite())) {
                    request(0);
                } else if (!(isReadAllowedofCamera())) {
                    request(1);
                } else if (!(isReadAllowedofRead()) && !(isReadAllowedofWrite())) {
                    request(2);
                }

                dialogopener_images();


            }
        });

    }

    private boolean isReadAllowedofCamera() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(View_patient.this, android.Manifest.permission.CAMERA);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private boolean isReadAllowedofRead() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(View_patient.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private boolean isReadAllowedofWrite() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(View_patient.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void request(int id) {

        if (id == 0) {
            PermissionHelper permissionHelper = new PermissionHelper(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            permissionHelper.request(new PermissionHelper.PermissionCallback() {
                @Override
                public void onPermissionGranted() {
                }

                @Override
                public void onPermissionDenied() {


                }

                @Override
                public void onPermissionDeniedBySystem() {

                }
            });
        } else if (id == 1) {
            PermissionHelper permissionHelper = new PermissionHelper(this, new String[]{android.Manifest.permission.CAMERA}, 100);
            permissionHelper.request(new PermissionHelper.PermissionCallback() {
                @Override
                public void onPermissionGranted() {

                }

                @Override
                public void onPermissionDenied() {


                }

                @Override
                public void onPermissionDeniedBySystem() {

                }
            });
        } else if (id == 2) {
            PermissionHelper permissionHelper = new PermissionHelper(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            permissionHelper.request(new PermissionHelper.PermissionCallback() {
                @Override
                public void onPermissionGranted() {

                }

                @Override
                public void onPermissionDenied() {


                }

                @Override
                public void onPermissionDeniedBySystem() {

                }
            });
        }

        if (isReadAllowedofCamera() && isReadAllowedofRead() && isReadAllowedofWrite()) {
            dialogopener_images();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        view_patient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot d1 = dataSnapshot.child(doc_username).child("patients").child(pat_id);

                if (d1.exists()) {
                    String v_name, v_gender, v_dob, v_diagnosis, v_mobile, v_phone, v_medhis, v_reffered, v_department;
                    v_name = d1.child("patient_first_name").getValue().toString() + " " + d1.child("patient_last_name").getValue().toString();
                    v_gender = d1.child("patient_gender").getValue().toString();
                    v_dob = d1.child("patient_dob").getValue().toString();
                    //v_diagnosis = d1.child("patient_diagnosis").getValue().toString();
                    v_mobile = d1.child("patient_mobile").getValue().toString();
                    v_phone = d1.child("patient_phone").getValue().toString();
                    v_medhis = d1.child("patient_medical_history").getValue().toString();
                    v_reffered = dataSnapshot.child(doc_username).child("name").getValue().toString();
                    v_department = d1.child("patient_department").getValue().toString();
                    v_diagnosis = "";

                    for (DataSnapshot postSnapshot : dataSnapshot.child(doc_username).child("patients").child(pat_id).child("patient_diagnosis").getChildren())
                    {

                        v_diagnosis += postSnapshot.getValue().toString();
                        v_diagnosis += "\n";

                    }

                    patient.setText(v_name.toUpperCase());
                    patient_name.setText(v_name);
                    reffered_by.setText(v_reffered);
                    department.setText(v_department);
                    gender.setText(v_gender);
                    age.setText(getAge(v_dob)+" years");
                    dob_value.setText(v_dob);
                    id_value.setText(pat_id);
                    diagnosis_value.setText(v_diagnosis);
                    mobile_value.setText(v_mobile);
                    phone_value.setText(v_phone);
                    medical_history_value.setText(v_medhis);

                    note_list.removeAllViews();
                    picture_list.removeAllViews();
                    //noteadapter.clear();
                    //pictureadapter.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.child(doc_username).child("patients").child(pat_id).child("notes").getChildren()) {
                        String date = postSnapshot.child("note_date").getValue().toString();
                        String title = postSnapshot.child("note_title").getValue().toString();
                        String id = postSnapshot.getKey();

                        View newLayout_notes = LayoutInflater.from(getBaseContext()).inflate(R.layout.notes_singleview, note_list, false);

                        TextView title_n = (TextView)newLayout_notes.findViewById(R.id.textView_notes_singleview_title);
                        TextView date_n = (TextView)newLayout_notes.findViewById(R.id.textView_notes_singleview_date_value);

                        title_n.setText(title);
                        date_n.setText(date);


                        newLayout_notes.setTag(id);
                        newLayout_notes.setClickable(true);
                        newLayout_notes.setFocusable(true);

                        newLayout_notes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in = new Intent(View_patient.this, Notes.class);
                                in.putExtra("patient_id", pat_id);
                                in.putExtra("username", doc_username);
                                in.putExtra("note_id",v.getTag().toString());
                                startActivity(in);

                            }
                        });

                        note_list.addView(newLayout_notes);

                        View v = new View(getBaseContext());
                        v.setLayoutParams(new LinearLayout.LayoutParams(
                                ActionBar.LayoutParams.MATCH_PARENT,
                                5
                        ));

                        v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                        note_list.addView(v);


                    }

                    for (DataSnapshot postSnapshot : dataSnapshot.child(doc_username).child("patients").child(pat_id).child("image links").getChildren()) {
                        final String path = postSnapshot.child("path").getValue().toString();
                        final String date = postSnapshot.child("date").getValue().toString();
                        final String id = postSnapshot.getKey();

                        View newLayout = LayoutInflater.from(getBaseContext()).inflate(R.layout.photos_singleview, picture_list, false);

                        img_p = (ImageView) newLayout.findViewById(R.id.imgView);
                        TextView date_p = (TextView)newLayout.findViewById(R.id.textView_photos_singleview_date_value);

                        date_p.setText(date);
                        Glide.with(newLayout.getContext())
                                .load(path)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(img_p);



                        newLayout.setTag(id);
                        newLayout.setClickable(true);
                        newLayout.setFocusable(true);

                        newLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               launch_view_image(doc_username,pat_id,path,id);

                            }
                        });

                        picture_list.addView(newLayout);

                        View v = new View(getBaseContext());
                        v.setLayoutParams(new LinearLayout.LayoutParams(
                                ActionBar.LayoutParams.MATCH_PARENT,
                                5
                        ));

                        v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                        picture_list.addView(v);


                        //picture = new Picture(id, path, date);
                        //picture_array.add(picture);
                        //pictureadapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ImageView imageView, imageView1;

    public void dialogopener_images() {
        dialog_images = new Dialog(View_patient.this);
        dialog_images.setContentView(R.layout.pictures_options_popup);

        final TextView gallery_btn = (TextView) dialog_images.findViewById(R.id.textView_pictures_options_gallery);
        TextView camera_btn = (TextView) dialog_images.findViewById(R.id.textView_pictures_options_camera);
        TextView cancel_btn = (TextView) dialog_images.findViewById(R.id.textView_pictures_options_cancel);


        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadAllowedofWrite() && isReadAllowedofRead()) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    request(2);
                }
            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadAllowedofCamera()) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                } else {
                    request(1);
                }
            }
        });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_images.dismiss();
            }
        });

        dialog_images.setCanceledOnTouchOutside(false);
        dialog_images.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            String imagePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            //img_p.setImageBitmap(bitmap);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data_compress = baos.toByteArray();
            pic_storage(data_compress);
            dialog_images.dismiss();
            launch_view_patient(doc_username, pat_id);

        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                //img_p.setImageBitmap(photo);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data_compress = baos.toByteArray();
                pic_storage(data_compress);
                dialog_images.dismiss();
                launch_view_patient(doc_username, pat_id);

            }

        }


    }

    public void dialogopener() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_popup);

        final TextView delete = (TextView) dialog.findViewById(R.id.textView_delete_delete);
        TextView cancel = (TextView) dialog.findViewById(R.id.textView_delete_cancel);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_patient(doc_username, pat_id);
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

    public void pic_storage(byte[] data_compress) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        image_id = currentDateandTime + "";

        String image_path = "Prescriptions/" + image_id + ".png";
        photos_storage = FirebaseStorage.getInstance().getReference(image_path);

        avi.show();

        UploadTask uploadTask = photos_storage.putBytes(data_compress);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String path = taskSnapshot.getDownloadUrl().toString();
                String date_s = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));

                Map<String,String> map_picture = new HashMap<String,String>();
                map_picture.put("date",date_s);
                map_picture.put("path",path);

                view_patient.child(doc_username).child("patients").child(pat_id)
                        .child("image links").child(image_id).setValue(map_picture);


                avi.hide();
                //Glide.with(getApplicationContext()).load(path).into(pres);
                Toast toast = Toast.makeText(getApplicationContext(), "Upload Successfully", Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Upload Unsuccessful", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void launch_printable(String doc_username, String pat_id) {
        Intent i = new Intent(this, Printable.class);
        i.putExtra("username", doc_username);
        i.putExtra("patient_id", pat_id);
        startActivity(i);
    }

    public void launch_notes(String doc_username, String pat_id) {

        Intent i = new Intent(this, Notes.class);
        i.putExtra("username", doc_username);
        i.putExtra("patient_id", pat_id);
        String notes_id = note_ref.push().getKey().toString();
        i.putExtra("note_id", notes_id);
        startActivity(i);
    }


    public void launch_new_patient_info(String doc_username, String pat_id) {

        Intent i = new Intent(this, New_patient_info.class);
        i.putExtra("username", doc_username);
        i.putExtra("patient_id", pat_id);
        startActivity(i);
    }

    public void delete_patient(String doc_username, String pat_id) {
        view_patient.child(doc_username).child("patients").child(pat_id).removeValue();
        launch_my_patients(doc_username);
    }

    public void launch_view_image(String doc_username, String pat_id,String path,String image_id)
    {
        Intent i = new Intent(this, View_image.class);
        i.putExtra("username", doc_username);
        i.putExtra("patient_id", pat_id);
        i.putExtra("image_link", path);
        i.putExtra("image_id",image_id);
        startActivity(i);
    }

    public void launch_my_patients(String doc_username) {

        Intent i = new Intent(this, My_patients.class);
        i.putExtra("username", doc_username);
        startActivity(i);
    }

    public void launch_view_patient(String doc_username, String pat_id) {

        Intent i = new Intent(this, View_patient.class);
        i.putExtra("username", doc_username);
        i.putExtra("patient_id", pat_id);
        startActivity(i);
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
                sb = Snackbar.make(findViewById(R.id.view_patient_ui), "No Internet Connection",Snackbar.LENGTH_INDEFINITE);
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