package com.example.littlegarbage.View.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.littlegarbage.Model.db.GarbageData;
import com.example.littlegarbage.Model.db.GarbageDataBase;
import com.example.littlegarbage.Model.db.GarbageDataDao;
import com.example.littlegarbage.R;
import com.example.littlegarbage.Util.HttpUtil;
import com.example.littlegarbage.Util.ShotShareUtil;
import com.example.littlegarbage.Model.GarbageBean;
import com.example.littlegarbage.Util.JsonParser;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowGarbageDetailActivity extends AppCompatActivity {


    public GarbageDataDao garbageDataDao;

    Handler hd;
    String garbage = null;
    GarbageBean.ResultBean.GarbageInfoBean garbageInfoBean = null;
    String citydaima;
    @BindView(R.id.detail_sure) ImageView sureIv;
    @BindView(R.id.detail_share) ImageView shareIv;
    @BindView(R.id.picture_laji) ImageView garbageIv;
    @BindView(R.id.text_status) TextView statusTv;
    @BindView(R.id.text_garbage_name) TextView garbagenametext;
    @BindView(R.id.garbage_name) TextView garbagenameTv;
    @BindView(R.id.text_came_name) TextView camenametext;
    @BindView(R.id.came_name) TextView camenameTv;
    @BindView(R.id.text_city_name) TextView citynametext;
    @BindView(R.id.city_name) TextView citynameTv;
    @BindView(R.id.text_confidence) TextView confidencetext;
    @BindView(R.id.confidence) TextView confidenceTv;
    @BindView(R.id.text_ps_detail) TextView ps_detailtext;
    @BindView(R.id.ps_detail) TextView ps_detailTv;
    @BindView(R.id.just_picture) ImageView justpictureIv;
    @BindView(R.id.text_ps) TextView justpsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_garbage_detail);
        ButterKnife.bind(this);

        GarbageDataBase garbageDataBase= Room.databaseBuilder(
                this,GarbageDataBase.class,"garbage_database").build();
        garbageDataDao=garbageDataBase.getGarbageDataDao();


        SearchActivity.open(ShowGarbageDetailActivity.this);

        Intent intent = getIntent();
        garbage = intent.getStringExtra("garbage");
        garbageInfoBean = (GarbageBean.ResultBean.GarbageInfoBean) intent.getSerializableExtra("bean");
        citydaima = intent.getStringExtra("citydaima");//得到的城市代码

        if (garbage != null) {
            hd = new Handler();

            // 启用网络线程
            HttpThread ht = new HttpThread();
            ht.start();
        } else if (garbageInfoBean != null) {
            setDataBeanText(garbageInfoBean);
            Thread thread = new Thread(()->{
                int i = garbageDataDao.updateInfoByGarbage(garbageInfoBean.getGarbage_name(), garbageInfoBean.toString());
                if(i<=0){
                    GarbageData garbageData = new GarbageData(garbageInfoBean.getGarbage_name(), garbageInfoBean.toString());
                    garbageDataDao.insertGarbageInfo(garbageData);
                }
            });
            thread.start();

        }

    }

    /*根据传入的garbagebean数据，将数据放入展示界面*/
    private void setDataBeanText(GarbageBean.ResultBean.GarbageInfoBean garbageInfoBean) {

        garbagenameTv.setText(garbageInfoBean.getGarbage_name());
        camenameTv.setText(garbageInfoBean.getCate_name());
        citynameTv.setText(garbageInfoBean.getCity_name());
        confidenceTv.setText(doubleToString(garbageInfoBean.getConfidence()));
        ps_detailTv.setText(garbageInfoBean.getPs());
        statusTv.setText("--获取数据成功--");
        justpsTv.setText("注意：识别置信度，可以用来衡量识别结果，该值越大越好，建议采用值为0.7以上的结果");

        switch (garbageInfoBean.getCate_name()) {

            case "厨余垃圾":
                Glide.with(this).load(R.mipmap.chuyulaji).into(garbageIv);
                break;

            case "湿垃圾":
                Glide.with(this).load(R.mipmap.shilaji).into(garbageIv);
                break;

            case "其他垃圾":
                Glide.with(this).load(R.mipmap.qitalaji).into(garbageIv);
                break;

            case "有害垃圾":
                Glide.with(this).load(R.mipmap.youhailaji).into(garbageIv);
                break;

            case "可回收物":
                Glide.with(this).load(R.mipmap.kehuishouwu).into(garbageIv);
                break;

            case "干垃圾":
                Glide.with(this).load(R.mipmap.ganlaji).into(garbageIv);
                break;

            default:
                Glide.with(this).load(R.mipmap.laji).into(garbageIv);
        }

        Glide.with(this).load(R.mipmap.bg).into(justpictureIv);


        garbagenametext.setText("垃圾名称：");
        camenametext.setText("垃圾类型：");
        citynametext.setText("城市：");
        confidencetext.setText("识别置信度：");
        ps_detailtext.setText("具体信息：");

    }

    /*点击确定按钮，返回搜索界面*/
    @OnClick({R.id.detail_sure, R.id.detail_share})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.detail_sure:

                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);

                break;

            //分享
            case R.id.detail_share:

                // View是全屏截图
                View getView = this.getWindow().getDecorView();
                getView.setDrawingCacheEnabled(true);
                getView.buildDrawingCache();
                Bitmap b1 = getView.getDrawingCache();

                // 获取状态栏高度
                Rect frame = new Rect();
                this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;

                // 获取屏幕长和高
                int width = this.getWindowManager().getDefaultDisplay().getWidth();
                int height = this.getWindowManager().getDefaultDisplay().getHeight();
                // 去掉标题栏
                Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                        - statusBarHeight);
                getView.destroyDrawingCache();

                ShotShareUtil.shotShare(this, b);//将该图片分享

                break;
        }
    }


    /*根据garbage获取具体信息*/
    public class HttpThread extends Thread {

        @Override
        public void run() {
            super.run();
            GarbageBean garbageBean = null;
            JsonParser jp = new JsonParser();

            // 城市代码
            String garbageString = null;
            try {

                garbageString = HttpUtil.sendOkHttpRequest(garbage, citydaima);

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }

            if (garbageString == null) {
                //获取失败 也保存到数据库，确保历史记录也有这条非法输入，但点击时只显示空信息界面
                garbageString = "数据获取错误";

            } else {
                //获取数据成功

                // 调用自定义的 JSON 解析类解析获取的 JSON 数据
                garbageBean = jp.GarbageParse(garbageString);
                final GarbageBean finalGb = garbageBean;
                // 多线程更新 UI
                hd.post(() -> setDataText(finalGb));

            }

            int i = garbageDataDao.updateInfoByGarbage(garbage, garbageString);

            if(i<=0){
                GarbageData garbageData = new GarbageData(garbage,garbageString);
                garbageDataDao.insertGarbageInfo(garbageData);
            }
        }

        private void setDataText(GarbageBean finalGb) {

            if (finalGb != null) {

                List<GarbageBean.ResultBean.GarbageInfoBean> garbageInfoBean = finalGb.getResult().getGarbage_info();

                setDataBeanText(garbageInfoBean.get(0));

            }
        }
    }

    /*将confidence这个double类型参数转为两位小数展示*/
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }
}
