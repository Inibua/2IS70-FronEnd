package com.example.group16.journaloo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.activities.CreateJourneyActivity;

public class NoJourneyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_no_journey, container, false);

        Button newJourneyButton = rootView.findViewById(R.id.newJourneyButton);
        newJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateJourneyActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
