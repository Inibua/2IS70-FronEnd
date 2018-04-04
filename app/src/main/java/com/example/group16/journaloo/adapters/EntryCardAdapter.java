package com.example.group16.journaloo.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.activities.EditEntryActivity;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.GlideApp;
import com.example.group16.journaloo.models.Entry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EntryCardAdapter extends RecyclerView.Adapter<EntryCardAdapter.ViewHolder> {
    private static final String TAG = "CardAdapter";
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private List<Entry> entryList;

    public EntryCardAdapter(List<Entry> entryList) {
        this.entryList = entryList;
    }

    @Override
    public EntryCardAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                          int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_entry_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Entry entry = entryList.get(position);

        if (entry.user_id == wrapper.getLoggedInUser().id) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = holder.cardView.getContext();
                    Intent intent = new Intent(context, EditEntryActivity.class);
                    intent.putExtra("entryId", entry.id);
                    context.startActivity(intent);
                }
            });
        }

        String url = "https://polar-cove-19347.herokuapp.com/entry/" + entry.id + "/image";

        GlideApp.with(holder.image.getContext())
                .load(url)
                .centerCrop()
                .into(holder.image);

        SimpleDateFormat fmt = new SimpleDateFormat("MMMM dd yyyy", Locale.ENGLISH);
        holder.date.setText(fmt.format(entry.created));

        holder.location.setText(entry.location);
        holder.description.setText(entry.description);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public TextView date;
        public TextView location;
        public TextView description;
        public ImageView image;


        ViewHolder(View v) {
            super(v);
            cardView = v;
            location = v.findViewById(R.id.locationTextView);
            date = v.findViewById(R.id.dateTextView);
            description = v.findViewById(R.id.descriptionTextView);
            image = v.findViewById(R.id.entryImageView);
        }
    }
}

