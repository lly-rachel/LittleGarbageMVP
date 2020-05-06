package com.example.littlegarbage;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    public static String sendOkHttpRequest(String garbage) {

        OkHttpClient client = new OkHttpClient();

        String url1 = "https://aiapi.jd.com/jdai/garbageTextSearch?appkey=f08733d22c104e5dc39f97a323359da9&timestamp=";
        long time =System.currentTimeMillis()+2*60*1000;
        String s1 = "&sign="+GetMD5.md5("1a8c89772abf812630f6687255d22a3b"+time);
        String url = url1+time+s1;

        RequestBody requestBody = new FormBody.Builder()
                .add("cityId","310000")
                .add("text",garbage)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        String responseData = null;

        try {

            Response response =client.newCall(request).execute();
            if(response.code()==200){
                responseData = response.body().string();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseData;
    }
}
