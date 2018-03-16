package com.example.group16.journaloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class CreateJourneyActivity extends AppCompatActivity {

    public String nameJourney;
    public boolean privateJourney = false; // journey is set on public by default

    MainActivity obj = new MainActivity();

    EditText editJourneyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journey);

        editJourneyName = (EditText) findViewById(R.id.editJourneyName);

    }

    protected void getJourneyDetails(){
        //get info from fields.
    }

    public void selectItem(View view){
        boolean checked =((CheckBox) view ).isChecked();
        switch (view.getId()){
            case R.id.checkPrivate:
                if(checked){
                    privateJourney = true;
                }
                break;

        }

    }

    public void saveJourney(View view){
        obj.setJourneyActive(true);
        nameJourney = editJourneyName.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
