package com.example.littlegarbage.retrofit;

import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitService {
    //Get Post请求接口

    //热门搜索
    // "https://api.tianapi.com/txapi/hotlajifenlei/index?key=2fb9da721d164cdc0a45b990545796fa";
    @GET("index")
    Call<ResponseBody> getHotHistory(@Query("key") String key);

    //联想词
    //"https://api.zhetaoke.com:10001/api/api_suggest.ashx?appkey=3982f6785fcd4b54a214c69f4c167477&content=s";
    @GET("api_suggest.ashx")
    Call<ResponseBody> getImageData(@Query("appkey") String appkey, @Query("content") String content);

    //获取文本信息
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("garbageTextSearch")
    Call<ResponseBody> getTextData(@QueryMap Map<String,String> map, @Body RequestBody requestbody);

    //图片
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("garbageImageSearch")
    Call<ResponseBody> getPictureData(@QueryMap Map<String,String> map, @Body RequestBody requestbody);

    //音频
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("garbageVoiceSearch")
    @Multipart
    Call<ResponseBody> getSoundData(@QueryMap Map<String,String> map, @Part MultipartBody.Part file);

}
