package com.example.group16.journaloo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.group16.journaloo.api.APIWrapper;
import com.google.gson.Gson;

public class ViewEntriesActivity extends AppCompatActivity {
    final static Gson gson = new Gson();
    private APIWrapper wrapper = APIWrapper.getWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}