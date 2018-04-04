package com.example.group16.journaloo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.models.Journey;

import java.util.List;

public class JourneyCardAdapter extends RecyclerView.Adapter<JourneyCardAdapter.ViewHolder> {
    private List<Journey> journeyList;

    public JourneyCardAdapter(List<Journey> journeyList) {
        this.journeyList = journeyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journey_card_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Journey journey = journeyList.get(position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.cardView.getContext();
                Intent intent = new Intent(context, ViewEntries.class);
                intent.putExtra("id", journey.id);

                context.startActivity(intent);
            }
        });

        holder.title.setText(journey.title);
    }

    @Override
    public int getItemCount() {
        return journeyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public TextView title;


        public ViewHolder(View v) {
            super(v);
            cardView = v;
            title = v.findViewById(R.id.journey_title);
        }
    }


}
