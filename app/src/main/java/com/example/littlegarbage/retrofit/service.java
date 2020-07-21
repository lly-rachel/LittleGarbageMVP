package com.example.littlegarbage.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface service {
    //联想词
    //"https://api.zhetaoke.com:10001/api/api_suggest.ashx?appkey=3982f6785fcd4b54a214c69f4c167477&content=s";
    @GET("api_suggest.ashx")
    Call<ResponseBody> getImageData(@Query("appkey") String appkey, @Query("content") String content);
}
