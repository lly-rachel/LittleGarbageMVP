package com.example.littlegarbage;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public GarbageBean GarbageParse (String garbageString) {
        GarbageBean garbageBean=null;
        try {
            JSONObject joGarbage = new JSONObject(garbageString);

            String code = joGarbage.getString("code");


            if(code.equals("10000")){
                Integer remain = joGarbage.getInt("remain");
                Boolean charge = joGarbage.getBoolean("charge");
                String msg = joGarbage.getString("msg");
                String result = joGarbage.getString("result");

                JSONObject resultGarbage = new JSONObject(result);

                Integer status = resultGarbage.getInt("status");
                String message = resultGarbage.getString("message");
                if(status==0){
                    JSONArray listArray = resultGarbage.getJSONArray("garbage_info");
                    List<GarbageBean.ResultBean.GarbageInfoBean> garbageInfoBeanList = new ArrayList<>();

                    String cate_name = null;
                    String city_id = null;
                    String city_name = null;
                    double confidence = 0;
                    String garbage_name = null;
                    String ps = null;

                    if(listArray!=null){
                        for(int i = 0;i<listArray.length();i++){
                            JSONObject  jsonArray= listArray.getJSONObject(i);
                            cate_name = jsonArray.getString("cate_name");
                            city_id =jsonArray.getString("city_id");
                            city_name =jsonArray.getString("city_name");
                            confidence =jsonArray.getDouble("confidence");
                            garbage_name =jsonArray.getString("garbage_name");
                            ps =jsonArray.getString("ps");
                        }

                    }



                    garbageInfoBeanList.add(new GarbageBean.ResultBean
                            .GarbageInfoBean(cate_name,city_id,city_name,confidence,garbage_name,ps));


                    GarbageBean.ResultBean resultBean = new GarbageBean.ResultBean(message,status,garbageInfoBeanList);
                    garbageBean=new GarbageBean(code,charge,remain,msg,resultBean);

                }


            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

        return garbageBean;
    }

}
