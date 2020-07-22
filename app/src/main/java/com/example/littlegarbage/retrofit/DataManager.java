package com.example.littlegarbage.retrofit;

import android.content.Context;

import com.example.littlegarbage.model.bean.GarbageBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DataManager {

    private RetrofitService retrofitService;

    public DataManager(Context context,String url){
        this.retrofitService = RetrofitHelper.getInstance(context,url).getServer();
    }

    public Observable<GarbageBean> getTextData(Map<String,String> map, RequestBody requestbody){
        return retrofitService.getTextData(map, requestbody);
    }

    public Observable<GarbageBean> getPictureData(Map<String,String> map,RequestBody requestbody){
        return retrofitService.getPictureData(map, requestbody);
    }

    public Observable<GarbageBean> getSoundData( Map<String,String> map, MultipartBody.Part file){
        return retrofitService.getSoundData(map, file);
    }
}
