package com.example.group16.journaloo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.models.User;

public class EditPasswordActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public void editPasswordButton (View view) {
        User user = wrapper.getLoggedInUser();
        EditText newPassword = findViewById(R.id.newPasswordEditText);
        EditText confirmPassword = findViewById(R.id.confirmPasswordEditText);
        String passwordNew = newPassword.getText().toString();
        String passwordConfirm = confirmPassword.getText().toString();

        if (!passwordNew.equals(passwordConfirm)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT)
                    .show();
        } else {
            User userUpdate = new User(user.username, user.email, passwordNew);
            wrapper.updateUser(userUpdate);
            finish();
        }
    }
}
