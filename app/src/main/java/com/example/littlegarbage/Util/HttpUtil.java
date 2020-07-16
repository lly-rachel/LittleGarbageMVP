package com.example.littlegarbage.Util;


import android.os.Environment;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class HttpUtil {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static String sendOkHttpRequest(String garbage,String citydaima) throws JSONException, MalformedURLException {

        if(citydaima==null){
            citydaima=String.valueOf(310000);
        }

        JSONObject json = new JSONObject();
        json.put("cityId",citydaima);
        json.put("text",garbage);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(7000, TimeUnit.SECONDS)
                .writeTimeout(7000, TimeUnit.SECONDS)
                .readTimeout(7000, TimeUnit.SECONDS)
                .build();

        String url1 = "https://aiapi.jd.com/jdai/garbageTextSearch?appkey=f08733d22c104e5dc39f97a323359da9&timestamp=";
        long time = System.currentTimeMillis();
        String s1 = getMD5("1a8c89772abf812630f6687255d22a3b" + time);
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

    public static String sendOkHttpPictureRequest(String imgbase,String citydaima) throws JSONException, MalformedURLException {

        if(citydaima==null){
            citydaima=String.valueOf(310000);
        }

        JSONObject json = new JSONObject();
        json.put("cityId",citydaima);
        json.put("imgBase64",imgbase);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(7000, TimeUnit.SECONDS)
                .writeTimeout(7000, TimeUnit.SECONDS)
                .readTimeout(7000, TimeUnit.SECONDS)
                .build();

        String url1 = "https://aiapi.jd.com/jdai/garbageImageSearch?appkey=f08733d22c104e5dc39f97a323359da9&timestamp=";
        long time = System.currentTimeMillis();
        String s1 = getMD5("1a8c89772abf812630f6687255d22a3b" + time);
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


    public static String sendOkHttpSoundRequest(String model,String VERSION,Integer packagecode,String citydaima) throws JSONException, MalformedURLException {

        File sound = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/BitmapTest/"+"yinfu.wav");

        if(citydaima==null){
            citydaima=String.valueOf(310000);
        }

        JSONObject jsonEncode = new JSONObject();
        jsonEncode.put("channel",1);//int类型，⾳频声道数，目前只⽀持单声道，填1
        jsonEncode.put("format","wav");//string类型，⾳频格式，支持wav， amr，mp3
        jsonEncode.put("sample_rate",16000);//int类型，采样率，目前只⽀持填写16000
        jsonEncode.put("post_process",0);//int类型，数字后处理:1为强制开启(开启后，会把结果中的数字汉字转换成阿拉伯数字。例如，识别结果中的“一千”会 转成“1000”)，0为根据服务端配置是否进行数字后处理


        JSONObject jsonProperty = new JSONObject();
        jsonProperty.put("autoend",false);
        jsonProperty.put("encode",jsonEncode);
        jsonProperty.put("platform","Android&"+model+"&"+VERSION);//{平台}&{机型}&{系统版本号}
        jsonProperty.put("version",String.valueOf(packagecode));//客户端版本号


        String url1 = "https://aiapi.jd.com/jdai/garbageVoiceSearch?";
        String appkey = "appkey=f08733d22c104e5dc39f97a323359da9&timestamp=";
        long time = System.currentTimeMillis();
        String s1 = getMD5("1a8c89772abf812630f6687255d22a3b" + time);
        String urls = url1+appkey + time + "&sign=" + s1;

        URL url = new URL(urls);

        MediaType parse = MediaType.parse("application/octet-stream");//application/octet-stream
        if (sound !=null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(7000, TimeUnit.SECONDS)
                .writeTimeout(7000, TimeUnit.SECONDS)
                .readTimeout(7000, TimeUnit.SECONDS)
                    .build();

            RequestBody requestBody = RequestBody.create(parse,sound);
            MultipartBody build = new MultipartBody.Builder()
                    .addFormDataPart("file", sound.getName(), requestBody)
                    .setType(MultipartBody.FORM)
                    .build();


            Request request =new Request.Builder()
                    .url(url)
                    .addHeader("cityId", citydaima)
                    .addHeader("property", String.valueOf(jsonProperty))
                    .post(build)
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
        }

        return null;
    }

    //sign参数需要MD5加密
    public static String getMD5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
