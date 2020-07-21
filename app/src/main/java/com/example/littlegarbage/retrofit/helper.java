package com.example.littlegarbage.retrofit;

import android.content.Context;

import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class helper {
    private Context context;
    private String url;

    private static RetrofitHelper instance = null;
    private Retrofit retrofit = null;

    public helper(Context context, String url) {
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


    public static RetrofitHelper getInstance(Context context, String url){
        if(instance==null){
            instance = new RetrofitHelper(context,url);
        }
        return instance;
    }

    public service getServer(){
        return  retrofit.create(service.class);
    }



    //联想词
    public Call<ResponseBody> getImageData(String imageNameKey, String content){
        return getServer().getImageData(imageNameKey,content);
    }
}
