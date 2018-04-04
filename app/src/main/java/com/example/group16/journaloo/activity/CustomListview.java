package com.example.group16.journaloo.activity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.group16.journaloo.R;

public class CustomListview extends ArrayAdapter<String>{

    private String[] journeyNames;
    private Activity context;

    public CustomListview(Activity context, String[] journeys) {
        super(context, R.layout.listview_layout,journeys);

        this.context=context;
        this.journeyNames=journeyNames;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) r.getTag();
        }
        viewHolder.tvw1.setText(journeyNames[position]);
        return r;

    }
    class ViewHolder{
        TextView tvw1;
        ViewHolder(View v)
        {
            tvw1= (TextView) v.findViewById(R.id.tvjourneyname);

        }

    }



}