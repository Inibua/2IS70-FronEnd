package com.example.group16.journaloo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.activities.EditProfileActivity;
import com.example.group16.journaloo.activities.LoginActivity;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.models.User;

public class ViewProfileFragment extends Fragment {
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = wrapper.getLoggedInUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_profile, container, false);
        TextView usernameDisplay = rootView.findViewById(R.id.username_textview);
        TextView emailDisplay = rootView.findViewById(R.id.email_textview);

        usernameDisplay.setText(user.username);
        emailDisplay.setText(user.email);

        Button logoutButton = rootView.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wrapper.logout(getContext());

                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        Button deleteUserButton = rootView.findViewById(R.id.delete_user_button);
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAccountConfirmationDialogFragment alert =
                        new DeleteAccountConfirmationDialogFragment();
                FragmentManager fm = getFragmentManager();
                alert.show(fm, "");
            }
        });

        return rootView;
    }

    public void editPassword(View view) {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }
}
