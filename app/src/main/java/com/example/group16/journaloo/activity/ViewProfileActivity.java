package com.example.group16.journaloo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.model.User;

public class ViewProfileActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        TextView usernameDisplay = (TextView) findViewById(R.id.usernameDisplayTextView);
        TextView emailDisplay = (TextView) findViewById(R.id.emailDisplayTextView);
        TextView passwordDisplay = (TextView) findViewById(R.id.passwordDisplayTextView);
        User user = wrapper.getLoggedInUser();
        if (wrapper.getLoggedInUser() == null){
            Log.d("user", wrapper.getLoggedInUser().username);
        } else {
            Log.d("user found", "whatever");
        }
        usernameDisplay.setText(user.username);
        emailDisplay.setText(user.email);
        passwordDisplay.setText("..........");
    }

    public void editPassword(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        finish();
        startActivity(intent);
    }

}
