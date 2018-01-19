package com.example.krish.medical_app.UI;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

/**
 * Created by KRISH on 11-07-2017.
 */

public class View_image extends AppCompatActivity
{

    protected ImageView view_image;
    protected ImageView delete_image;
    protected ImageView back_image;
    protected String doc_username, pat_id,path,image_id,image_path;
    protected DatabaseReference link_delete;
    protected StorageReference image_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.view_image);

        link_delete = FirebaseDatabase.getInstance().getReference();


        view_image = (ImageView) findViewById(R.id.imageView_view_image);
        back_image = (ImageView) findViewById(R.id.imageView_view_image_back);
        delete_image = (ImageView) findViewById(R.id.imageView_view_image_delete);

        Bundle bundle = getIntent().getExtras();
        doc_username = bundle.getString("username");
        pat_id = bundle.getString("patient_id");
        path= bundle.getString("image_link");
        image_id = bundle.getString("image_id");
        image_path = "Prescriptions/" + image_id + ".png";


        //path = link_delete.child(doc_username).child("patients").child(pat_id).child("image links").child(image_id).child("path").getValue().toString();

        /*link_delete.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(doc_username).child("patients").child(pat_id).child("image links").child(image_id).exists())
                {
                    path = dataSnapshot.child(doc_username).child("patients").child(pat_id).child("image links").child(image_id).child("path").getValue().toString();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        Glide.with(this)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view_image);


        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch_view_patients(doc_username,pat_id);
            }
        });

        delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogopener_delete_image();

            }
        });


    }

    public void launch_view_patients(String doc_username, String pat_id)
    {
        Intent i = new Intent(this, View_patient.class);
        i.putExtra("username", doc_username);
        i.putExtra("patient_id", pat_id);
        startActivity(i);
    }

    public void dialogopener_delete_image()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_popup);

        final TextView delete = (TextView)dialog.findViewById(R.id.textView_delete_delete);
        TextView cancel = (TextView)dialog.findViewById(R.id.textView_delete_cancel);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                image_delete = FirebaseStorage.getInstance().getReference(image_path);
                image_delete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Toast toast = Toast.makeText(getApplicationContext(), "Delete Successful", Toast.LENGTH_SHORT);
                        toast.show();
                        launch_view_patients(doc_username,pat_id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                        Toast toast = Toast.makeText(getApplicationContext(), "Delete Unsuccessful", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                link_delete.child(doc_username).child("patients").child(pat_id)
                        .child("image links").child(image_id).removeValue();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
                sb = Snackbar.make(findViewById(R.id.view_image_ui), "No Internet Connection",Snackbar.LENGTH_INDEFINITE);
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
