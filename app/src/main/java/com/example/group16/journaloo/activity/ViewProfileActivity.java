package com.example.group16.journaloo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.model.User;

public class ViewProfileActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        TextView usernameDisplay = findViewById(R.id.username_textview);
        TextView emailDisplay = findViewById(R.id.email_textview);
        User user = wrapper.getLoggedInUser();

        if (wrapper.getLoggedInUser() == null) {
            Log.d("user", wrapper.getLoggedInUser().username);
        } else {
            Log.d("user found", "whatever");
        }

        usernameDisplay.setText(user.username);
        emailDisplay.setText(user.email);

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.auth_token));
                editor.apply();

                Intent intent = new Intent(ViewProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void editPassword(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        finish();
        startActivity(intent);
    }

}
