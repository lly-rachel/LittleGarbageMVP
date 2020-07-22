package com.example.littlegarbage.show;

import android.content.Context;

import com.example.littlegarbage.model.bean.GarbageBean;

import java.net.MalformedURLException;

public interface ShowDetailActivityContract {

    interface Presenter{
        void loadData(Context context, String garbage, String garbageString) throws MalformedURLException;
        void loadData(GarbageBean.ResultBean.GarbageInfoBean garbageInfoBean);
        void clickSure();
        void share();
    }

    interface View{
        void getDataOnSucceed(GarbageBean garbageBean,String garbage,String garbageString);
        void getDataOnSucceed(GarbageBean.ResultBean.GarbageInfoBean garbageInfoBean);
        void getDataOnFailed(String garbage,String garbageString);

        void clickSureFinished();
        void shareFinished();
    }
}
