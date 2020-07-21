package com.example.littlegarbage.retrofit;



import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitService {
    //Get Post请求接口

    //热门搜索
    // final static String hotSearchHistoryURL = "https://api.tianapi.com/txapi/hotlajifenlei/index?key=2fb9da721d164cdc0a45b990545796fa";
    @GET("index")
    Call<ResponseBody> getHotHistory(@Query("key") String key);

//    //联想词
//    //"https://api.zhetaoke.com:10001/api/api_suggest.ashx?appkey=3982f6785fcd4b54a214c69f4c167477&content=s";
//    @GET("api_suggest.ashx")
//    Call<ResponseBody> getImageData(@Query("appkey") String appkey,@Query("content") String content);

    //获取文本信息
    //


}
