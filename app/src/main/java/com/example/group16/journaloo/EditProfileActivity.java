package com.example.group16.journaloo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public void editPassword (View view){
        EditText oldPassword = (EditText) findViewById(R.id.oldPasswordEditText);
        EditText newPassword = (EditText) findViewById(R.id.newPasswordEditText);
        EditText confirmPassword = (EditText) findViewById(R.id.confirmPasswordEditText);


    }
}
