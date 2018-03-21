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

/**
 * Created by s146958 on 21-3-2018.
 */

public class CustomEntryListview extends ArrayAdapter<String> {

    private String[] entryname;
    private String[] entrydesc;
    private Integer[] entryimigid;
    private Activity context;

    public CustomEntryListview(Activity context, String[] entryname,String[] entrydesc,Integer[] entryimigid) {
        super(context, R.layout.entrylistview_layout,entryname);

        this.context=context;
        this.entryname=entryname;
        this.entrydesc=entrydesc;
        this.entryimigid=entryimigid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.entrylistview_layout,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) r.getTag();
        }
        viewHolder.ivw.setImageResource(entryimigid[position]);
        viewHolder.tvw1.setText(entryname[position]);
        viewHolder.tvw2.setText(entrydesc[position]);
        return r;

    }
    class ViewHolder{
        TextView tvw1;
        TextView tvw2;
        ImageView ivw;
        ViewHolder(View v)
        {
            tvw1= (TextView) v.findViewById(R.id.tventryname);
            tvw2= (TextView) v.findViewById(R.id.tventrydescription);
            ivw= (ImageView) v.findViewById(R.id.imageViewent);

        }

    }
}