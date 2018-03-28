package com.example.group16.journaloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
    }

    public void editPassword(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        finish();
        startActivity(intent);
    }

    public void displayCredentials (View view){
        TextView usernameDisplay = (TextView) findViewById(R.id.usernameDisplayTextView);
        TextView emailDisplay = (TextView) findViewById(R.id.emailDisplayTextView);
        TextView passwordDisplay = (TextView) findViewById(R.id.passwordDisplayTextView);
    }
}
