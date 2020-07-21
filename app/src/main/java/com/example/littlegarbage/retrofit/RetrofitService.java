package com.example.littlegarbage.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    //Get Post请求接口

    //热门搜索
    // final static String hotSearchHistoryURL = "https://api.tianapi.com/txapi/hotlajifenlei/index?key=2fb9da721d164cdc0a45b990545796fa";
    @GET("index")
    Call<ResponseBody> getHotHistory(@Query("key") String key);
}
