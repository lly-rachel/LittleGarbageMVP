package com.example.littlegarbage.retrofit;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import okhttp3.ConnectionSpec;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Part;

public class RetrofitHelper {

    private String url;

    private static RetrofitHelper instance = null;
    private Retrofit retrofit = null;

    public RetrofitHelper(String url) {
        this.url = url;
        initRetrofit();
    }

    void initRetrofit(){

        //解决handshake failed问题
        //https://api.zhetaoke.com:10001/api/api_suggest.ashx?appkey=3982f6785fcd4b54a214c69f4c167477&content = s"
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .allEnabledCipherSuites()
                .build();//解决在Android5.0版本以下https无法访问
        ConnectionSpec spec1 = new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT).build();//兼容http接口
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectionSpecs(Arrays.asList(spec,spec1));

        retrofit = new Retrofit.Builder()
                //热门搜索 url="https://api.tianapi.com/txapi/hotlajifenlei/"
                .baseUrl(url)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RetrofitHelper getInstance(String url){
        return new RetrofitHelper(url);
    }

    public RetrofitService getServer(){
        return retrofit.create(RetrofitService.class);
    }

    //热门搜索
    public Call<ResponseBody> getHotHistory(String key){
        return getServer().getHotHistory(key);
    }

    //联想词
    public Call<ResponseBody> getImageData(String imageNameKey, String content){
        return getServer().getImageData(imageNameKey,content);
    }

    //文本搜索
    public Call<ResponseBody> getTextData(Map<String,String> map, RequestBody requestbody){
        return getServer().getTextData (map, requestbody);
    }

    //图片搜索
    public Call<ResponseBody> getPictureData(Map<String,String> map, RequestBody requestbody){
        return getServer().getPictureData (map, requestbody);
    }

    //音频搜索
    public Call<ResponseBody> getSoundData(Map<String,String> map, MultipartBody.Part file){
        return getServer().getSoundData (map, file);
    }
}
