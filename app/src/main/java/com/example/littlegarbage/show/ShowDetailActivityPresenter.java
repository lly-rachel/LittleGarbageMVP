package com.example.littlegarbage.show;

import android.content.Context;

import com.example.littlegarbage.model.bean.GarbageBean;
import com.example.littlegarbage.model.db.GarbageData;
import com.example.littlegarbage.model.db.GarbageDataBase;
import com.example.littlegarbage.model.db.GarbageDataDao;
import com.example.littlegarbage.retrofit.DataManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.littlegarbage.utils.getMD5Util.getMD5;

public class ShowDetailActivityPresenter implements ShowDetailActivityContract.Presenter{

    ShowDetailActivityContract.View mView;

    GarbageDataDao garbageDataDao;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public ShowDetailActivityPresenter(ShowDetailActivityContract.View mView,GarbageDataBase garbageDataBase) {
        this.mView = mView;
        this.garbageDataDao = garbageDataBase.getGarbageDataDao();
    }

    @Override
    public void loadData(Context context,String garbage,String citydaima) {
        /*根据garbage获取具体信息*/
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

        // 城市代码
        final String[] garbageString = {null};

        long time = System.currentTimeMillis();
        String s1 = getMD5("1a8c89772abf812630f6687255d22a3b" + time);

        RequestBody body = RequestBody.create(JSON, String.valueOf(json));

        Map<String,String> map = new HashMap<>();
        map.put("appkey","f08733d22c104e5dc39f97a323359da9");
        map.put("timestamp",String.valueOf(time));
        map.put("sign",s1);

        Observable<GarbageBean> observable = new DataManager(context,"https://aiapi.jd.com/jdai/").getTextData(map,body);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(garbageBean -> {
                    if (garbageBean.getResult().message.equals("success")) {
                        //获取数据成功
                        mView.getDataOnSucceed(garbageBean,garbage, garbageString[0]);
                    } else {
                        // 获取失败 也保存到数据库，确保历史记录也有这条非法输入，但点击时只显示空信息界面
                        garbageString[0] = "数据获取错误";
                        mView.getDataOnFailed(garbage, garbageString[0]);
                    }
                });

        new Thread(()->{
            loadDB(garbage, garbageString[0]);
        }).start();

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

    private void loadDB(String garbage,String garbageString){
        int i = garbageDataDao.updateInfoByGarbage(garbage, garbageString);

        if (i <= 0) {
            GarbageData garbageData = new GarbageData(garbage, garbageString);
            garbageDataDao.insertGarbageInfo(garbageData);
        }
    }
}
