package com.example.group16.journaloo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListview extends ArrayAdapter<String>{

    private String[] journeyname;
    private String[] desc;
    private Integer[] imigid;
    private Activity context;

    public CustomListview(Activity context, String[] journeyname,String[] desc,Integer[] imigid) {
        super(context, R.layout.listview_layout,journeyname);

        this.context=context;
        this.journeyname=journeyname;
        this.desc=desc;
        this.imigid=imigid;
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
        viewHolder.ivw.setImageResource(imigid[position]);
        viewHolder.tvw1.setText(journeyname[position]);
        viewHolder.tvw2.setText(desc[position]);
        return r;

    }
    class ViewHolder{
        TextView tvw1;
        TextView tvw2;
        ImageView ivw;
        ViewHolder(View v)
        {
            tvw1= (TextView) v.findViewById(R.id.tvjourneyname);
            tvw2= (TextView) v.findViewById(R.id.tvdescription);
            ivw= (ImageView) v.findViewById(R.id.entryImageView);

        }

    }
}