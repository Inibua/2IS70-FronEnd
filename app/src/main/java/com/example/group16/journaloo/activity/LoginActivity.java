package com.example.group16.journaloo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.model.User;

import java.io.UnsupportedEncodingException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity /*implements LoaderCallbacks<Cursor>*/ {

    // UI references.
    private EditText userNameInput;
    private TextInputEditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox stayLoggedInCheckBox;

    private APIWrapper wrapper = APIWrapper.getWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String authToken = sharedPref.getString(getString(R.string.auth_token), "");
        if (!authToken.isEmpty()) {
            try {
                wrapper.decodeAndStore(authToken);
                final Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        userNameInput = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        stayLoggedInCheckBox = findViewById(R.id.stay_logged_in);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        stayLoggedInCheckBox.setChecked(true);

        Button loginButton = findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Store values at the time of the login attempt.
        String username = userNameInput.getText().toString();
        String password = mPasswordView.getText().toString();


        User loginUser = new User(username, password);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            final Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            wrapper.login(loginUser, new MainThreadCallback() {
                @Override
                public void onFail(Exception error) {
                    Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }

                @Override
                public void onSuccess(String responseBody) {
                    if (stayLoggedInCheckBox.isChecked()) {
                        Log.d("LoginActivity", "stay logged in set!");
                        SharedPreferences sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.auth_token), wrapper.getToken());
                        editor.apply();
                    }

                    startActivity(intent);
                }
            });
        }
    }

    private boolean isPasswordValid(String password) {
        return !password.isEmpty();
    }


    public void signUpLink(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        finish();
        startActivity(intent);
    }

    public void forgotPasswordLink(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        finish();
        startActivity(intent);
    }

//    public void onCheckboxClicked(View view) {
//        stayLoggedIn = ((CheckBox) view).isChecked();
//    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

