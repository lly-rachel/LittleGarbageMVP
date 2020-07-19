package com.example.littlegarbage.show;

import android.os.Looper;

import androidx.room.Room;

import com.example.littlegarbage.model.bean.GarbageBean;
import com.example.littlegarbage.model.db.GarbageData;
import com.example.littlegarbage.model.db.GarbageDataBase;
import com.example.littlegarbage.model.db.GarbageDataDao;
import com.example.littlegarbage.utils.HttpUtil;
import com.example.littlegarbage.utils.JsonParser;

import org.json.JSONException;

import java.net.MalformedURLException;

public class ShowDetailActivityPresenter implements ShowDetailActivityContract.Presenter{

    ShowDetailActivityContract.View mView;

    GarbageDataDao garbageDataDao;


    public ShowDetailActivityPresenter(ShowDetailActivityContract.View mView,GarbageDataBase garbageDataBase) {
        this.mView = mView;
        this.garbageDataDao = garbageDataBase.getGarbageDataDao();
    }

    @Override
    public void loadData(String garbage,String citydaima) {
        // 启用网络线程
        HttpThread ht = new HttpThread(garbage,citydaima);
        ht.start();
    }

    @Override
    public void loadData(GarbageBean.ResultBean.GarbageInfoBean garbageInfoBean) {


        Thread thread = new Thread(()->{
//            Looper.prepare();
//
//            loadDB(garbageInfoBean.getGarbage_name(), garbageInfoBean.toString());
//
//            Looper.loop();
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
