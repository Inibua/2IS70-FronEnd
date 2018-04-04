package com.example.group16.journaloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewProfileActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        TextView usernameDisplay = findViewById(R.id.usernameDisplayTextView);
        TextView emailDisplay = findViewById(R.id.emailDisplayTextView);
        TextView passwordDisplay = findViewById(R.id.passwordDisplayTextView);
        User user = wrapper.getLoggedInUser();
        if (wrapper.getLoggedInUser() == null){
            Log.d("user", wrapper.getLoggedInUser().userName);
        } else {
            Log.d("user found", "whatever");
        }
        usernameDisplay.setText(user.userName);
        emailDisplay.setText(user.email);
        //passwordDisplay.setText(user.password);
    }

    public void editPassword(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        finish();
        startActivity(intent);
    }

}
