package com.example.krish.medical_app.UI;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krish.medical_app.R;

import static android.R.attr.data;

/**
 * Created by KRISH on 13-06-2017.
 */

public class Pictures_options_popup extends AppCompatActivity {

    protected TextView gallery;
    protected TextView camera;
    protected TextView cancel;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int REQUEST_IMAGE_CAPTURE = 2;

    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.pictures_options_popup);

        gallery = (TextView) findViewById(R.id.textView_pictures_options_gallery);
        camera = (TextView) findViewById(R.id.textView_pictures_options_camera);
        cancel = (TextView) findViewById(R.id.textView_pictures_options_cancel);
        imageView = (ImageView) findViewById(R.id.imgView);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });



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

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(photo);

        }

    }

}

