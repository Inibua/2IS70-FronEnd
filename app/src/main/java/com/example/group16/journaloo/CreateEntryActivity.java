package com.example.group16.journaloo;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class CreateEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        String path = "sdcard/camera_app/cam_image.jpg";

        ImageView entryView = (ImageView) findViewById(R.id.entryView);
        entryView.setImageDrawable(Drawable.createFromPath(path));





    }


}
