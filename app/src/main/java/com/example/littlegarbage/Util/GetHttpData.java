package com.example.littlegarbage.Util;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class GetHttpData {



    public static String GetHotData(String s) throws MalformedURLException {

        String res = "";

        StringBuilder sb = new StringBuilder();

        URL url=new URL(s);
        try{
            // 得到connection对象
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);

            //获取失败
            if(httpURLConnection.getResponseCode()!= HttpURLConnection.HTTP_OK){

                return null;

            }
            // 使用 InputStreamReader 进行数据接收
            InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
            // 缓存
            BufferedReader br = new BufferedReader(isr);

            String temp = null;
            // 读取接收的数据
            while ( (temp = br.readLine()) != null) {
                sb.append(temp);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        res = sb.toString();



        return res;

    }
}
