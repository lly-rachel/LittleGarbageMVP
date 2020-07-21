package com.example.littlegarbage.moreChoose;

import android.content.Context;
import android.os.Environment;
import android.widget.ListView;
import android.widget.TextView;

import com.example.littlegarbage.adapter.CityAdapter;

import java.io.File;
import java.util.List;

public class MoreChoosePresenter implements MoreChooseContract.Presenter{

    MoreChooseContract.View mView;

    public MoreChoosePresenter(MoreChooseContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void clickSure() {
        mView.clickSureFinished();
    }

    @Override
    public void deleteFile(Context context) {
        String basepath = Environment.getExternalStorageDirectory() + "/BitmapTest";

        File file = new File(basepath + "/yinfu.wav");
        if (file.exists()) {
            file.delete();
        }

        File soundfile = new File(basepath + "/yinfu.pcm");
        if (soundfile.exists()) {
            soundfile.delete();
        }

        File share_picture = new File(basepath + "/share.png");
        if (share_picture.exists()) {
            share_picture.delete();
        }

        File picture = new File(basepath + "/output_image.png");
        if (picture.exists()) {
            picture.delete();
        }

        File path = new File(basepath);
        if (path.exists()) {
            path.delete();
        }

        mView.deleteFileFinished();
    }

    @Override
    public void ShowDialog(Context context, boolean isFirst, List<String> list, TextView citychooseTv, ListView citylist) {
        //避免连续点击，list连续添加,只在第一次点击时添加
        if (isFirst) {
            //310000(上海市)、330200(宁波市)、610100(西安市)、440300(深圳市)、北京市(110000)
            list.add("上海");
            list.add("宁波");
            list.add("西安");
            list.add("深圳");
            list.add("北京");
            isFirst = false;
        }


        CityAdapter adapter = new CityAdapter(list, context);
        adapter.notifyDataSetChanged();
        citylist.setAdapter(adapter);
        citylist.setOnItemClickListener((arg0, arg1, positon, id) -> citychooseTv.setText(list.get(positon)));

        mView.ShowDialogFinished();
    }


}
