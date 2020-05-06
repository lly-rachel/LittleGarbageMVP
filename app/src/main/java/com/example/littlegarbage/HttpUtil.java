package com.example.littlegarbage;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    public static String sendOkHttpRequest(String garbage) {

        OkHttpClient client = new OkHttpClient();

        String url1= "https://aiapi.jd.com/jdai/garbageTextSearch?appkey=f08733d22c104e5dc39f97a323359da9&timestamp=";
        long time =System.currentTimeMillis();
        String s1 = GetMD5.md5("1a8c89772abf812630f6687255d22a3b"+time);
        String url = url1+time+"&sign="+s1;

        RequestBody requestBody = new FormBody.Builder()
                .add("cityId","310000")
                .add("text",garbage)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .post(requestBody)
                .build();

        final String[] responseData = {null};

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code()==200){
                    responseData[0] = response.body().string();
                }

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });



        return responseData[0];
    }
}
