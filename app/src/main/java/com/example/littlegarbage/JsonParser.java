package com.example.littlegarbage;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    public GarbageBean GarbageParse (String garbageString) {
        GarbageBean garbageInfo=new GarbageBean();
        try {
            JSONObject joGarbage = new JSONObject(garbageString);

            garbageInfo.setCode(joGarbage.getString("code"));
            garbageInfo.setCharge(joGarbage.getBoolean("charge"));
            garbageInfo.setMsg(joGarbage.getString("msg"));
            garbageInfo.setRemain(joGarbage.getInt("remain"));
            garbageInfo.setResult(joGarbage.getString("result"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return garbageInfo;
    }

}
