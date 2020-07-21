package com.example.littlegarbage.search;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.example.littlegarbage.model.bean.GarbageBean;
import com.example.littlegarbage.retrofit.RetrofitHelper;
import com.example.littlegarbage.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.littlegarbage.utils.getMD5Util.getMD5;

public class SearchActivityPresenter implements SearchActivityContract.Presenter {

    SearchActivityContract.View mView;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public SearchActivityPresenter(SearchActivityContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void clickshezhi() {
        mView.clickshezhiFinished();
    }

    @Override
    public void clickSearch() {
        mView.clickSearchFinished();
    }

    @Override
    public void getHotSearchData(String hotHistoryURL,String hotHistoryKey) {

        //https://api.tianapi.com/txapi/hotlajifenlei/index?key=2fb9da721d164cdc0a45b990545796fa
        /*获取热门搜索数据*/
        RetrofitHelper.getInstance(hotHistoryURL).getHotHistory(hotHistoryKey)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String  result = null;
                        try {
                            result = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mView.getDataOnSucceed(result);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mView.getDataOnFailed();
                    }
                });
    }

    @Override
    public void getImageData(String imageNameURL, String appkey, String content) {

        //获取联想词数据
        RetrofitHelper.getInstance(imageNameURL).getImageData(appkey,content)
                    .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String  result = null;
                        try {
                            result = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mView.getImageDataOnSucceed(result);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mView.getDataOnFailed();
                    }
                });


    }


    @Override
    public void getSoundData(Context context, String citydaima) {

        File sound = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/BitmapTest/"+"yinfu.wav");
        /*根据图片获取具体信息*/
        if (citydaima == null) {
            citydaima = String.valueOf(310000);
        }

        String model = Build.MODEL;
        String version = Build.VERSION.RELEASE;
        Integer packagecode = packageCode(context);

        JSONObject jsonEncode = new JSONObject();
        try {
            jsonEncode.put("channel",1);//int类型，⾳频声道数，目前只⽀持单声道，填1
            jsonEncode.put("format","wav");//string类型，⾳频格式，支持wav， amr，mp3
            jsonEncode.put("sample_rate",16000);//int类型，采样率，目前只⽀持填写16000
            jsonEncode.put("post_process",0);//int类型，数字后处理:1为强制开启(开启后，会把结果中的数字汉字转换成阿拉伯数字。例如，识别结果中的“一千”会 转成“1000”)，0为根据服务端配置是否进行数字后处理
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonProperty = new JSONObject();
        try {
            jsonProperty.put("autoend",false);
            jsonProperty.put("encode",jsonEncode);
            jsonProperty.put("platform","Android&"+model+"&"+version);//{平台}&{机型}&{系统版本号}
            jsonProperty.put("version",String.valueOf(packagecode));//客户端版本号
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 城市代码
        final String[] garbageString = {null};

        long time = System.currentTimeMillis();
        String s1 = getMD5("1a8c89772abf812630f6687255d22a3b" + time);


        Map<String, String> map = new HashMap<>();
        map.put("appkey", "f08733d22c104e5dc39f97a323359da9");
        map.put("timestamp", String.valueOf(time));
        map.put("sign", s1);
        map.put("cityId", citydaima);
        map.put("property", String.valueOf(jsonProperty));

        RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), sound);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file", sound.getName(), requestFile);

        RetrofitHelper.getInstance("https://aiapi.jd.com/jdai/").getSoundData(map,file)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            garbageString[0] = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        JsonParser jp = new JsonParser();
                        GarbageBean garbageBean = jp.GarbageParse(garbageString[0]);
                        if (garbageBean != null) {
                            mView.getDataOnSucceed(garbageBean);

                        } else {
                            mView.getDataOnFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mView.getDataOnFailed();
                    }
                });

    }

    @Override
    public void getPictureData(String imgBase,String citydaima){

            /*根据图片获取具体信息*/
            if (citydaima == null) {
                citydaima = String.valueOf(310000);
            }

            JSONObject json = new JSONObject();
            try {
                json.put("cityId", citydaima);
                json.put("imgBase64", imgBase);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 城市代码
            final String[] garbageString = {null};

            long time = System.currentTimeMillis();
            String s1 = getMD5("1a8c89772abf812630f6687255d22a3b" + time);

            RequestBody body = RequestBody.create(JSON, String.valueOf(json));

            Map<String, String> map = new HashMap<>();
            map.put("appkey", "f08733d22c104e5dc39f97a323359da9");
            map.put("timestamp", String.valueOf(time));
            map.put("sign", s1);

            RetrofitHelper.getInstance("https://aiapi.jd.com/jdai/").getPictureData(map, body)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                garbageString[0] = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            JsonParser jp = new JsonParser();
                            GarbageBean garbageBean = jp.GarbageParse(garbageString[0]);
                            if (garbageBean != null) {
                                mView.getDataOnSucceed(garbageBean);

                            } else {
                                mView.getDataOnFailed();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            mView.getDataOnFailed();
                        }
                    });

    }


    private Integer packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

}
