package com.example.group16.journaloo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.model.Entry;
import com.example.group16.journaloo.R;

import java.io.FileInputStream;

public class NewEntryActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        Bitmap bmp = null;
        filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView entryImageView = findViewById(R.id.entryImageView);
        entryImageView.setImageBitmap(bmp);

        //String path = "sdcard/camera_app/cam_image.jpg";
        //entryView.setImageDrawable(Drawable.createFromPath(path));

    }

    public void saveEntry(View view) {
        EditText descriptionEntry = findViewById(R.id.descriptionEditEntryEditText);
        EditText locationEntry = findViewById(R.id.locationEntryEditText);
        final Intent intent = new Intent(this, MainActivity.class);

        if (locationEntry.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please give the entry a location",
                    Toast.LENGTH_LONG).show();
        } else if (descriptionEntry.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please give the entry a description",
                    Toast.LENGTH_LONG).show();
        } else {
            Entry.NewEntry newEntry = new Entry.NewEntry();
            newEntry.description = descriptionEntry.getText().toString();
            newEntry.location = locationEntry.getText().toString();
            wrapper.createEntry(newEntry, filename, new MainThreadCallback() {
                @Override
                public void onFail(Exception error) {
                    Toast.makeText(getApplicationContext(), "Failed to create entry",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(String responseBody) {
                    finish();
                    startActivity(intent);
                }
            });



        }
    }
}