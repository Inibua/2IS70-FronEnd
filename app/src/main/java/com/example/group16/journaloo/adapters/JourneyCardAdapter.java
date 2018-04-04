package com.example.group16.journaloo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.activities.ViewEntriesActivity;
import com.example.group16.journaloo.models.Journey;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class JourneyCardAdapter extends RecyclerView.Adapter<JourneyCardAdapter.ViewHolder> {
    private List<Journey> journeyList;

    public JourneyCardAdapter(List<Journey> journeyList) {
        this.journeyList = journeyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_journey_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Journey journey = journeyList.get(position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.cardView.getContext();
                Intent intent = new Intent(context, ViewEntriesActivity.class);
                intent.putExtra("journeyId", journey.id);
                context.startActivity(intent);
            }
        });

        holder.title.setText(journey.title);
        SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        holder.start.setText(fmt.format(journey.start_date));
        if (journey.end_date != null) {
            holder.end.setText(fmt.format(journey.end_date));
        }
    }

    @Override
    public int getItemCount() {
        return journeyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public TextView title;
        public TextView start;
        public TextView end;


        ViewHolder(View v) {
            super(v);
            cardView = v;
            title = v.findViewById(R.id.journey_title);
            start = v.findViewById(R.id.journey_start);
            end = v.findViewById(R.id.journey_end);
        }
    }


}
