package com.example.group16.journaloo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.GlideApp;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.models.Entry;
import com.google.gson.Gson;

public class EditEntryActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private final static Gson gson = new Gson();
    private Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        int entryId = getIntent().getIntExtra("entryId", -1);
        wrapper.getEntry(entryId, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                finish();
            }

            @Override
            public void onSuccess(String responseBody) {
                entry = gson.fromJson(responseBody, Entry.class);

                TextView locationTextView = findViewById(R.id.locationTextView);
                EditText descriptionEditEntry = findViewById(R.id.descriptionEditEntryEditText);
                ImageView entryImageView = findViewById(R.id.entryImageView);

                String url = "https://polar-cove-19347.herokuapp.com/entry/" + entry.id + "/image";
                GlideApp.with(getApplicationContext())
                        .load(url)
                        .centerCrop()
                        .into(entryImageView);

                locationTextView.setText(entry.location);
                descriptionEditEntry.setHint(entry.description);
            }
        });
    }

    public void saveEditEntry(View view){
        EditText descriptionEditEntry = findViewById(R.id.descriptionEditEntryEditText);
        entry.description = descriptionEditEntry.getText().toString();

        // TODO: get updated value of location from EditText

        wrapper.updateEntry(entry, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to update entry", Toast
                        .LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String responseBody) {
                finish();
            }
        });
    }

    /**
     * Deletes the entry that is currently being viewed in the edit activity
     * Redirects to the entry list of that journey
     */
    public void deleteEditEntry(View view) {
        wrapper.deleteEntry(entry.id, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to delete entry", Toast
                        .LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String responseBody) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

    }
}
