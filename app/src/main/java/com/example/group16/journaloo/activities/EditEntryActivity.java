package com.example.group16.journaloo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.models.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EditEntryActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private final static Gson gson = new Gson();
    int entry_id;
    int journey_id;
    String location;
    String description;
    private Entry entryToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        entry_id = getIntent().getExtras().getInt("id");
        location = getIntent().getExtras().getString("location");
        description = getIntent().getExtras().getString("description");
        journey_id = getIntent().getExtras().getInt("journey_id");

        TextView locationTextView = findViewById(R.id.locationTextView);
        EditText descriptionEditEntry = findViewById(R.id.descriptionEditEntryEditText);
        wrapper.getImage(entry_id);
        Bitmap bmp = wrapper.getImageCurrentEntryBitmap();
        ImageView entryImageView = findViewById(R.id.entryImageView);
        entryImageView.setImageBitmap(bmp);
        locationTextView.setText(location);
        descriptionEditEntry.setHint(description);

    }

    public void saveEditEntry(View view){
        EditText descriptionEditEntry = findViewById(R.id.descriptionEditEntryEditText);

        String newDescription = descriptionEditEntry.getText().toString();
        wrapper.updateEntry(entry_id, journey_id, newDescription);
        Intent intent = new Intent (this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     * Deletes the entry that is currently being viewed in the edit activity
     * Redirects to the entry list of that journey
     *
     * @param view
     */
    public void deleteEditEntry(View view) {

        wrapper.getEntry(entry_id, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
            }

            @Override
            public void onSuccess(String responseBody) {
                entryToDelete = gson.fromJson(responseBody, Entry.class);
            }
        });
        wrapper.deleteEntry(entryToDelete);
        Intent intent = new Intent (this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
