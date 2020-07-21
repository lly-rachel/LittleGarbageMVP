package com.example.littlegarbage.retrofit;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class RetrofitHelper {

    private Context context;
    private String url;

    private static RetrofitHelper instance = null;
    private Retrofit retrofit = null;

    public RetrofitHelper(Context context, String url) {
        this.context = context;
        this.url = url;
        retrofit = new Retrofit.Builder()
                //热门搜索 url="https://api.tianapi.com/txapi/hotlajifenlei/"
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    public static RetrofitHelper getInstance(Context context,String url){
        return new RetrofitHelper(context,url);
    }

    public RetrofitService getServer(){
        return retrofit.create(RetrofitService.class);
    }

    //热门搜索
    public Call<ResponseBody> getHotHistory(String key){
        return getServer().getHotHistory(key);
    }

//    //联想词
//    public Call<ResponseBody> getImageData( String imageNameKey, String content){
//        return getServer().getImageData(imageNameKey,content);
//    }
}
