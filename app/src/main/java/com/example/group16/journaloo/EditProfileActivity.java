package com.example.group16.journaloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    public String passwordNew;
    public String passwordConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public void editPasswordButton (View view){
        EditText oldPassword = (EditText) findViewById(R.id.oldPasswordEditText);
        EditText newPassword = (EditText) findViewById(R.id.newPasswordEditText);
        EditText confirmPassword = (EditText) findViewById(R.id.confirmPasswordEditText);
        passwordNew = newPassword.getText().toString();
        passwordConfirm = confirmPassword.getText().toString();
        //add oldpassword check
        if (passwordNew != passwordConfirm ){
            Toast.makeText(getApplicationContext(), "You mistyped your new password", Toast.LENGTH_SHORT).show();

        } else if(passwordNew == passwordConfirm) {
            passwordNew = newPassword.getText().toString();
            Intent intent = new Intent(this, ViewProfileActivity.class);
            finish();
            startActivity(intent);
        }

    }


}
