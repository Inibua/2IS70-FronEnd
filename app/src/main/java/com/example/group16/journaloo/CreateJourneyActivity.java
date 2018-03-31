package com.example.group16.journaloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateJourneyActivity extends AppCompatActivity {

    public String nameJourney;
    public boolean privateJourney = false; // journey is set on public by default
    public boolean journeyActive = false;
    private APIWrapper wrapper = APIWrapper.getWrapper();

    EditText editJourneyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journey);

        editJourneyName = (EditText) findViewById(R.id.editJourneyName);
    }


    public void selectItem(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkPrivate:
                if (checked) {
                    privateJourney = true;
                }
                break;
        }
    }


    public void saveJourney(View view) throws NoJourneyException {

        if (editJourneyName.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(), "Please give the journey a name",
                    Toast.LENGTH_SHORT).show();

        } else {
            journeyActive = true;
            nameJourney = editJourneyName.getText().toString();
            Journey journeyToBeCreated = new Journey(nameJourney);
            wrapper.createJourney(journeyToBeCreated); // CREATE A NEW JOURNEY
            wrapper.getCurrentJourneyRequest(); // REQUEST THE CURRENT JOURNEY
            Journey activeJourneyObj = wrapper.getCurrentJourney();// GET THAT JOURNEY AS ACTIVE JOURNEY
            if (activeJourneyObj != null) { // IF NOT NULL, CONTINUE AS EXPECTED
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("nameJourney", activeJourneyObj.title);
                intent.putExtra("isActive", journeyActive);
                finish();
                startActivity(intent);
            } else { // IF NULL TOAST FOR SOMETHING WRONG AND TRY AGAIN
                Toast.makeText(getApplicationContext(), "Something went wrong when getting " +
                        "active journey", Toast.LENGTH_LONG).show();
                journeyActive = false;
                Intent intent = new Intent(this, CreateJourneyActivity.class);
                finish();
                startActivity(intent);
            }
        }
    }
}
