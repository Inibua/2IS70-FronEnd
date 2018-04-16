package com.example.group16.journaloo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditEntryActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        TextView locationTextView = (TextView) findViewById(R.id.locationTextView);
        EditText descriptionEditEntry = (EditText) findViewById(R.id.descriptionEditEntryEditText);
        Entry entry = wrapper.getEntry(Entry entry);
        locationTextView.setText(entry.location);
        descriptionEditEntry.setHint(entry.description);

    }

    public void saveEditEntry(View view){
        EditText descriptionEditEntry = (EditText) findViewById(R.id.descriptionEditEntryEditText);

        if (descriptionEditEntry.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Please give the entry a description", Toast.LENGTH_SHORT).show();
        }
    }
}
