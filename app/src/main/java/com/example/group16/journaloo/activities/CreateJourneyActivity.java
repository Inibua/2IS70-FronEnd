package com.example.group16.journaloo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.models.Journey;
import com.example.group16.journaloo.models.User;

public class CreateJourneyActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private EditText editJourneyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journey);
        editJourneyName = findViewById(R.id.editJourneyName);
    }

    public void saveJourney(View view) {
        if (!isNameOk(editJourneyName.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "Please give the journey a name of a length between 3 and 40 characters",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String title = editJourneyName.getText().toString();
        User user = wrapper.getLoggedInUser();
        Journey.NewJourney newJourney = new Journey.NewJourney(user.id, title);
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        wrapper.createJourney(newJourney, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                Toast.makeText(getApplicationContext(), "Failed to create journey",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(String responseBody) {
                finish();
                startActivity(intent);
            }
        });
    }

    private boolean isNameOk(String name) {
        return name.length() > 2 && name.length() < 40;
    }
}
