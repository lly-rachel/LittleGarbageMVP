package com.example.littlegarbage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.littlegarbage.R;
import com.example.littlegarbage.db.DBManeger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_history, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String name = mDatas.get(position);
        holder.garbagenameTv.setText(name);
        holder.deleteIv.setOnClickListener(v -> {
            mDatas.remove(name);
            DBManeger.deleteInfoByGarbage(name);
            notifyDataSetChanged();
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.search_history_name) TextView garbagenameTv;
        @BindView(R.id.search_history_delete) ImageView deleteIv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
