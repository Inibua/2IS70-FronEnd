package com.example.group16.journaloo.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.models.Entry;

import java.util.ArrayList;

/**
 * Created by s146958 on 21-3-2018.
 */

public class CustomEntryListview extends ArrayAdapter<Entry> {

    private ArrayList<Entry> entries;
    private Activity context;

    public CustomEntryListview(Activity context, ArrayList<Entry> entries) {
        super(context, R.layout.entrylistview_layout);
        this.context = context;
        this.entries = entries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Entry entry = getItem(position);
        assert entry != null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entrylistview_layout, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.tvw2.setText(entry.description);

//        Button btButton = convertView.findViewById(R.id.entryListView);
//        // Cache row position inside the button using `setTag`
//        btButton.setTag(position);
        // Attach the click event handler
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Entry entry = getItem(position);
                Intent intent = new Intent(context, EditEntryActivity.class);
                Log.i("w/e", "Hi you clicked a button amigo");
                intent.putExtra("id", entry.id);
                intent.putExtra("location", entry.location);
                intent.putExtra("description", entry.description);

                context.startActivity(intent);
            }
        });
        // ... other view population as needed...
        // Return the completed view
        return convertView;
    }

    class ViewHolder {
        TextView tvw1;
        TextView tvw2;
        ImageView ivw;

        ViewHolder(View v) {
            tvw1 = v.findViewById(R.id.tventryname);
            tvw2 = v.findViewById(R.id.tventrydescription);
            ivw = v.findViewById(R.id.imageViewent);
        }


    }
}