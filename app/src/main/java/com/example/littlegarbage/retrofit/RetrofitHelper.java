package com.example.littlegarbage.retrofit;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.net.URL;

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
        initRetrofit();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                //热门搜索 url="https://api.tianapi.com/txapi/hotlajifenlei/"
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RetrofitHelper getInstance(Context context,String url){
        if(instance == null){
            instance = new RetrofitHelper(context,url);
        }
        return instance;
    }

    public RetrofitService getServer(){
        return retrofit.create(RetrofitService.class);
    }

    public Call<ResponseBody> getHotHistory(String key){
        return getServer().getHotHistory(key);
    }
}
