package com.example.littlegarbage.show;

import android.os.Looper;

import androidx.room.Room;

import com.example.littlegarbage.model.bean.GarbageBean;
import com.example.littlegarbage.model.db.GarbageData;
import com.example.littlegarbage.model.db.GarbageDataBase;
import com.example.littlegarbage.model.db.GarbageDataDao;
import com.example.littlegarbage.retrofit.RetrofitHelper;
import com.example.littlegarbage.utils.HttpUtil;
import com.example.littlegarbage.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.littlegarbage.utils.HttpUtil.getMD5;

public class ShowDetailActivityPresenter implements ShowDetailActivityContract.Presenter{

    ShowDetailActivityContract.View mView;

    GarbageDataDao garbageDataDao;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public ShowDetailActivityPresenter(ShowDetailActivityContract.View mView,GarbageDataBase garbageDataBase) {
        this.mView = mView;
        this.garbageDataDao = garbageDataBase.getGarbageDataDao();
    }

    @Override
    public void loadData(String garbage,String citydaima) {
//        // 启用网络线程
//        HttpThread ht = new HttpThread(garbage,citydaima);
//        ht.start();


        if(citydaima==null){
            citydaima=String.valueOf(310000);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("cityId",citydaima);
            json.put("text",garbage);
        } catch (JSONException e) {
            e.printStackTrace();
        }



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

        Map<String,String> map = new HashMap<>();
        map.put("appkey","f08733d22c104e5dc39f97a323359da9");
        map.put("timestamp",String.valueOf(time));
        map.put("sign",s1);

        RetrofitHelper.getInstance(context,hotHistoryURL).getHotHistory(hotHistoryKey)
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
    public void loadData(GarbageBean.ResultBean.GarbageInfoBean garbageInfoBean) {


        Thread thread = new Thread(()->{
            loadDB(garbageInfoBean.getGarbage_name(), garbageInfoBean.toString());
        });
        thread.start();
        mView.getDataOnSucceed(garbageInfoBean);

    }

    @Override
    public void clickSure() {
        mView.clickSureFinished();
    }

    @Override
    public void share() {
        mView.shareFinished();
    }

    /*根据garbage获取具体信息*/
    public class HttpThread extends Thread {

        String garbage;
        String citydaima;

        public HttpThread(String garbage,String citydaima) {
            this.garbage = garbage;
            this.citydaima = citydaima;
        }

        @Override
        public void run() {
            super.run();
            GarbageBean garbageBean = null;
            JsonParser jp = new JsonParser();

            // 城市代码
            String garbageString = null;
            try {

                garbageString = HttpUtil.sendOkHttpRequest(garbage, citydaima);
                // 调用自定义的 JSON 解析类解析获取的 JSON 数据
                garbageBean = jp.GarbageParse(garbageString);
            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }

            if (garbageBean != null) {
                //获取数据成功
                mView.getDataOnSucceed(garbageBean,garbage,garbageString);

            } else {
                // 获取失败 也保存到数据库，确保历史记录也有这条非法输入，但点击时只显示空信息界面
                garbageString = "数据获取错误";
                mView.getDataOnFailed(garbage,garbageString);

            }
            loadDB(garbage,garbageString);

        }
    }

    private void loadDB(String garbage,String garbageString){
        int i = garbageDataDao.updateInfoByGarbage(garbage, garbageString);

        if (i <= 0) {
            GarbageData garbageData = new GarbageData(garbage, garbageString);
            garbageDataDao.insertGarbageInfo(garbageData);
        }
    }
}
