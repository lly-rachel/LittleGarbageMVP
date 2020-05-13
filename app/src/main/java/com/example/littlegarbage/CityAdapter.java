package com.example.littlegarbage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.littlegarbage.db.DBManeger;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends BaseAdapter {

    private List<String> mlist;
    private Context mContext;
    public static String getcity;

    public CityAdapter(List<String> mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final CityViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.city_item,null);
            holder = new CityViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (CityViewHolder) convertView.getTag();
        }
        final String name = mlist.get(position);
        holder.citynameTv.setText(name);

        return convertView;
    }

    class CityViewHolder  {

        TextView citynameTv;


        public CityViewHolder(View itemView){
            citynameTv=itemView.findViewById(R.id.cityname);

        }
    }

}
