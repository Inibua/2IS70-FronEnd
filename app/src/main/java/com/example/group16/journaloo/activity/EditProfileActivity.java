package com.example.group16.journaloo.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.model.User;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();

    public String passwordNew;
    public String passwordConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editPasswordButton (View view) {
        User user = wrapper.getLoggedInUser();
        EditText newPassword = (EditText) findViewById(R.id.newPasswordEditText);
        EditText confirmPassword = (EditText) findViewById(R.id.confirmPasswordEditText);
        passwordNew = newPassword.getText().toString();
        passwordConfirm = confirmPassword.getText().toString();

        if (!Objects.equals(passwordNew, passwordConfirm)) {
            Toast.makeText(getApplicationContext(), "You mistyped your new password", Toast.LENGTH_SHORT).show();
        } else if(Objects.equals(passwordNew, passwordConfirm)) {
            User userUpdate = new User(user.username, user.email, passwordNew);
            wrapper.updateUser(userUpdate);
            Intent intent = new Intent(this, ViewProfileActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
