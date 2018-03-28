package com.example.group16.journaloo;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    public String passwordNew;
    public String passwordConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editPasswordButton (View view){
        EditText oldPassword = (EditText) findViewById(R.id.oldPasswordEditText);
        EditText newPassword = (EditText) findViewById(R.id.newPasswordEditText);
        EditText confirmPassword = (EditText) findViewById(R.id.confirmPasswordEditText);
        passwordNew = newPassword.getText().toString();
        passwordConfirm = confirmPassword.getText().toString();
        //add oldpassword check
        if (!Objects.equals(passwordNew, passwordConfirm)){
            Toast.makeText(getApplicationContext(), "You mistyped your new password", Toast.LENGTH_SHORT).show();

        } else if(Objects.equals(passwordNew, passwordConfirm)) {
            passwordNew = newPassword.getText().toString();
            Intent intent = new Intent(this, ViewProfileActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
