package com.example.group16.journaloo.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.GlideApp;
import com.example.group16.journaloo.model.Entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EntryCardAdapter extends RecyclerView.Adapter<EntryCardAdapter.ViewHolder> {
    private List<Entry> entryList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public TextView date;
        public TextView location;
        public TextView description;
        public ImageView image;


        public ViewHolder(View v) {
            super(v);
            cardView = v;
            location = v.findViewById(R.id.locationTextView);
            date =  v.findViewById(R.id.dateTextView);
            description = v.findViewById(R.id.descriptionTextView);
            image = v.findViewById(R.id.entryImageView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EntryCardAdapter(List<Entry> entryList) {
        this.entryList = entryList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EntryCardAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Entry entry = entryList.get(position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.cardView.getContext();
                Intent intent = new Intent(context, EditEntryActivity.class);
                Log.i("EntryCard", "Hi you clicked a button amigo");
                intent.putExtra("id", entry.id);
                intent.putExtra("location", entry.location);
                intent.putExtra("description", entry.description);

                context.startActivity(intent);
            }
        });

        String url = "https://polar-cove-19347.herokuapp.com/entry/" + entry.id + "/image";

        GlideApp.with(holder.image.getContext())
                .load(url)
                .centerCrop()
                .into(holder.image);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.ENGLISH);
//        format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        try {
            Date date = format.parse(entry.created);
            holder.date.setText(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.location.setText(entry.location);
        holder.description.setText(entry.description);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return entryList.size();
    }
}

