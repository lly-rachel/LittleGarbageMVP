package com.example.littlegarbage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.littlegarbage.db.DBManeger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShowGarbageDetailActivity extends AppCompatActivity {

    ImageView garbageIv,justpictureIv;
    TextView justpsTv;
    TextView garbagenametext,camenametext,citynametext,confidencetext,ps_detailtext;
    TextView garbagenameTv,camenameTv,citynameTv,confidenceTv,ps_detailTv;

    Handler hd=null;
    String garbage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_garbage_detail);

        Intent intent=getIntent();
        garbage=intent.getStringExtra("garbage");

        iniView();

        hd = new Handler();

        // 启用网络线程
        HttpThread ht = new HttpThread();
        ht.start();
    }

    private void iniView() {

        garbageIv=findViewById(R.id.picture_laji);
        justpictureIv = findViewById(R.id.just_picture);
        justpsTv= findViewById(R.id.text_ps);

        garbagenametext=findViewById(R.id.text_garbage_name);
        camenametext=findViewById(R.id.text_came_name);
        citynametext=findViewById(R.id.text_city_name);
        confidencetext=findViewById(R.id.text_confidence);
        ps_detailtext=findViewById(R.id.text_ps_detail);

        garbagenameTv=findViewById(R.id.garbage_name);
        camenameTv=findViewById(R.id.came_name);
        citynameTv=findViewById(R.id.city_name);
        confidenceTv=findViewById(R.id.confidence);
        ps_detailTv=findViewById(R.id.ps_detail);
    }

    public class HttpThread extends Thread {

        @Override
        public void run() {
            super.run();
            GarbageBean garbageBean = null;
            JsonParser jp = new JsonParser();

            // 城市代码
            String garbageString = HttpUtil.sendOkHttpRequest(garbage);

            //获取数据成功

            // 调用自定义的 JSON 解析类解析获取的 JSON 数据
            garbageBean = jp.GarbageParse(garbageString);

            //更新数据库信息
            int i = DBManeger.updateInfoByGarbage(garbage,garbageString);
            //更新数据库失败，说明数据库没有这个信息，添加这个城市天气信息
            if (i <= 0) {
                DBManeger.addGarbageInfo(garbage,garbageString);
            }

            final GarbageBean finalGb = garbageBean;
            // 多线程更新 UI
            hd.post(new Runnable() {
                @Override
                public void run() {

                    setDataText(finalGb);
                }
            });





        }

        private void setDataText(GarbageBean finalGb) {

            if(finalGb!=null){

                List<GarbageBean.ResultBean.GarbageInfoBean> garbageInfoBean = finalGb.getResult().getGarbage_info();



                garbagenameTv.setText(garbageInfoBean.get(0).getGarbage_name());
                camenameTv.setText(garbageInfoBean.get(0).getCate_name());
                citynameTv.setText(garbageInfoBean.get(0).getCity_name());
                confidenceTv.setText((int) garbageInfoBean.get(0).getConfidence());
                ps_detailTv.setText(garbageInfoBean.get(0).getPs());
            }

            garbageIv.setImageResource(R.mipmap.laji);
            justpictureIv.setImageResource(R.mipmap.bg);
            justpsTv.setText("注意：识别置信度，可以用来衡量识别结果，该值越大越好，建议采用值为0.7以上的结果");

            garbagenametext.setText("垃圾名称：");
            camenametext.setText("垃圾类型：");
            citynametext.setText("城市：");
            confidencetext.setText("识别置信度：");
            ps_detailtext.setText("具体信息：");


        }
    }
}
