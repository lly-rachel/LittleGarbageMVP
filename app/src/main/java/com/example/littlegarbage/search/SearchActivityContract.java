package com.example.littlegarbage.search;

import android.content.Context;
import com.example.littlegarbage.model.bean.GarbageBean;

public interface SearchActivityContract {

    interface Presenter{


        void clickshezhi();
        void clickSearch();

        void getHotSearchData(Context context,String hotHistoryURL,String hotHistoryKey);
        void getImageData(Context context,String imageNameURL,String appkey,String content);
//        void getImageData(String imageUrl);
        void getSoundData(Context context, String citydaima);
        void getPictureData(Context context,String imgBase,String citydaima);
    }

    interface View{
        void clickshezhiFinished();
        void clickSearchFinished();

        void getDataOnSucceed(GarbageBean garbageBean);
        void getDataOnSucceed(String data);
        void getImageDataOnSucceed(String data);
        void getDataOnFailed();


    }
}
