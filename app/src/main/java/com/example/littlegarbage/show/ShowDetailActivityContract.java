package com.example.littlegarbage.show;

import com.example.littlegarbage.model.bean.GarbageBean;

public interface ShowDetailActivityContract {

    interface Presenter{
        void loadData(String garbage,String garbageString);
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
