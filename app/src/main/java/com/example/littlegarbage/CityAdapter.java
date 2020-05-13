package com.example.littlegarbage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends BaseAdapter {

    private List<String> mlist;
    private Context mContext;

    public CityAdapter(List<String> mlist, Context mContext) {
        mlist = new ArrayList<String>();
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
        City city = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.support_simple_spinner_dropdown_item,null);
            city = new City();
            city.name = convertView.findViewById(R.id.city_choose);
            convertView.setTag(city);
        }else{
            city = (City) convertView.getTag();
        }
        city.name.setText(mlist.get(position));
        return convertView;
    }

    class City{
        TextView name;
    }

}
