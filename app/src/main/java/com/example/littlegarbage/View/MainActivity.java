package com.example.littlegarbage.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.littlegarbage.R;
import com.example.littlegarbage.db.DBManeger;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity  {

    @BindView(R.id.earth) ImageView earth;
    @BindView(R.id.main_search) TextView mainSearch;
    private int recLen = 0;//展示时间3秒
    Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Glide.with(this).load(R.mipmap.start).into(earth);

        DBManeger.initDB(this);
        mainSearch.setText("跳过 " + recLen);//最开始显示

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() { // UI thread
                    @Override
                    public void run() {
                        recLen--;
                        mainSearch.setText("跳过 " + recLen);
                        if (recLen <= 0) {
                            timer.cancel();
                            mainSearch.setVisibility(View.GONE);//倒计时到0隐藏字体
                        }
                    }
                });
            }
        };

        timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒

        //正常情况下不点击跳过
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                //从闪屏界面跳转到首界面
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);//延迟3s后发送handler信息

    }


    @OnClick(R.id.main_search)
    public void onViewClicked() {
        Intent intent = new Intent(this, SearchActivity.class);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        startActivity(intent);
    }
}
