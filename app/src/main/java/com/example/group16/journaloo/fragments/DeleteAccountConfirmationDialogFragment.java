package com.example.group16.journaloo.fragments;

import android.app.AlertDialog; // Maybe change this to the other import depending on how it looks
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.group16.journaloo.activities.LoginActivity;
import com.example.group16.journaloo.activities.ViewProfileActivity;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.models.User;

public class DeleteAccountConfirmationDialogFragment extends DialogFragment {
    private User userToDelete;
    private APIWrapper wrapper;
    private Context aContext;

    public DeleteAccountConfirmationDialogFragment() {
        super();
        this.wrapper = APIWrapper.getWrapper();
        this.userToDelete = wrapper.getLoggedInUser();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.aContext = getActivity();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage("Delete account?")
                     .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // APIWrapper request to delete account
                wrapper.deleteUser(userToDelete);
                wrapper.logout(aContext);
                dialogInterface.cancel();
                Intent intent = new Intent(aContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dialog cancelled
                dialogInterface.cancel();
            }
        });
        return dialogBuilder.create();
    }

}
