package com.example.group16.journaloo.activity;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.GlideApp;
import com.example.group16.journaloo.model.Entry;
import okhttp3.HttpUrl;

import java.util.List;

public class EntryCardAdapter extends RecyclerView.Adapter<EntryCardAdapter.ViewHolder> {
    private List<Entry> entryList;
    private APIWrapper wrapper;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public TextView date;
        public TextView location;
        public TextView description;
        public ImageView image;


        public ViewHolder(View v) {
            super(v);
            cardView = v;
            description = v.findViewById(R.id.descriptionTextView);
            image = v.findViewById(R.id.entryImageView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EntryCardAdapter(List<Entry> entryList) {
        this.entryList = entryList;
        this.wrapper = APIWrapper.getWrapper();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EntryCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Entry entry = entryList.get(position);
        String url = "https://polar-cove-19347.herokuapp.com/entry/" + entry.id + "/image";

        GlideApp.with(holder.image.getContext())
                .load(url)
                .centerCrop()
                .into(holder.image);

        holder.description.setText(entry.description);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return entryList.size();
    }
}

