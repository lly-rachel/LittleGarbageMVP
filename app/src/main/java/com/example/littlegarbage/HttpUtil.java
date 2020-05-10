package com.example.littlegarbage;


import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class HttpUtil {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static String sendOkHttpRequest(String garbage) throws JSONException, MalformedURLException {

        JSONObject json = new JSONObject();
        json.put("cityId",String.valueOf(310000));
        json.put("text",garbage);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(7000, TimeUnit.SECONDS)
                .writeTimeout(7000, TimeUnit.SECONDS)
                .readTimeout(7000, TimeUnit.SECONDS)
                .build();

        String url1 = "https://aiapi.jd.com/jdai/garbageTextSearch?appkey=f08733d22c104e5dc39f97a323359da9&timestamp=";
        long time = System.currentTimeMillis();
        String s1 = GetMD5.md5("1a8c89772abf812630f6687255d22a3b" + time);
        String urls = url1 + time + "&sign=" + s1;

        URL url = new URL(urls);

        RequestBody body = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .post(body)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.code()==200) {
                String message=response.body().string();
                return message;
            } else {

                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sendOkHttpPictureRequest(String imgbase) throws JSONException, MalformedURLException {

        JSONObject json = new JSONObject();
        json.put("cityId",String.valueOf(310000));
        json.put("imgBase64",imgbase);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(7000, TimeUnit.SECONDS)
                .writeTimeout(7000, TimeUnit.SECONDS)
                .readTimeout(7000, TimeUnit.SECONDS)
                .build();

        String url1 = "https://aiapi.jd.com/jdai/garbageImageSearch?appkey=f08733d22c104e5dc39f97a323359da9&timestamp=";
        long time = System.currentTimeMillis();
        String s1 = GetMD5.md5("1a8c89772abf812630f6687255d22a3b" + time);
        String urls = url1 + time + "&sign=" + s1;

        URL url = new URL(urls);

        RequestBody body = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .post(body)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.code()==200) {
                String message=response.body().string();
                return message;
            } else {

                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
