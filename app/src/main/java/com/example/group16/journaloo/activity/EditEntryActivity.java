package com.example.group16.journaloo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.R;

public class EditEntryActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();
    int entry_id;
    String location;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        entry_id = getIntent().getExtras().getInt("id");
        location = getIntent().getExtras().getString("location");
        description = getIntent().getExtras().getString("description");

        TextView locationTextView = (TextView) findViewById(R.id.locationTextView);
        EditText descriptionEditEntry = (EditText) findViewById(R.id.descriptionEditEntryEditText);
        wrapper.getImage(entry_id);
        Bitmap bmp = wrapper.getImageCurrentEntryBitmap();
        ImageView entryImageView = findViewById(R.id.entryImageView);
        entryImageView.setImageBitmap(bmp);
        locationTextView.setText(location);
        descriptionEditEntry.setHint(description);

    }

    public void saveEditEntry(View view){
        EditText descriptionEditEntry = (EditText) findViewById(R.id.descriptionEditEntryEditText);

        if (descriptionEditEntry.getText().toString().matches(description)) {
            Toast.makeText(getApplicationContext(), "There are no changes",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent (this, MainActivity.class);
            finish();
            startActivity(intent);
        } else {
            wrapper.updateEntry(entry_id, descriptionEditEntry.toString());
            Intent intent = new Intent (this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
