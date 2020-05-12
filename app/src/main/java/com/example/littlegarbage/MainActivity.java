package com.example.littlegarbage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.littlegarbage.db.DBManeger;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView search;
    private int recLen = 3;//展示时间3秒
    Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBManeger.initDB(this);

        search=findViewById(R.id.main_search);
        search.setOnClickListener(this);
        search.setText("跳过 "+recLen);//最开始显示


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() { // UI thread
                    @Override
                    public void run() {
                        recLen--;
                        search.setText("跳过 " + recLen);
                        if (recLen <=0) {
                            timer.cancel();
                            search.setVisibility(View.GONE);//倒计时到0隐藏字体
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
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);//延迟5S后发送handler信息

    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){

            case R.id.main_search:
                intent=new Intent(this,SearchActivity.class);
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

        startActivity(intent);
    }
}
