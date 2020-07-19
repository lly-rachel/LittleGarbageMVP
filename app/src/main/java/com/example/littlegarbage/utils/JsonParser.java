package com.example.littlegarbage.utils;

import com.example.littlegarbage.model.bean.GarbageBean;
import com.google.gson.Gson;

public class JsonParser {
    public GarbageBean GarbageParse (String garbageString) {

        Gson gson = new Gson();
        GarbageBean garbageBean = gson.fromJson(garbageString, GarbageBean.class);
        if (garbageBean.getResult().message.equals("success")) {
            return garbageBean;
        } else {
            return null;
        }

    }

}
