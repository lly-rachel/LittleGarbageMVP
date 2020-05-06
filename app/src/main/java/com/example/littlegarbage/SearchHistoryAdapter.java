package com.example.littlegarbage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.littlegarbage.db.DBManeger;

import java.util.List;

public class SearchHistoryAdapter extends BaseAdapter {

    Context context;
    List<String> mDatas;

    public SearchHistoryAdapter(Context context, List<String> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_history,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final String name = mDatas.get(position);
        holder.garbagenameTv.setText(name);
        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(name);
                DBManeger.deleteInfoByGarbage(name);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }



    class ViewHolder  {

        ImageView deleteIv;
        TextView garbagenameTv;

        public ViewHolder(View itemView){

            deleteIv=itemView.findViewById(R.id.search_history_delete);
            garbagenameTv=itemView.findViewById(R.id.search_history_name);




        }
    }
}
