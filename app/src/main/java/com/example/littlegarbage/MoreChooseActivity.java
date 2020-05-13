package com.example.littlegarbage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoreChooseActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView surechooseIv;
    TextView citychooseTv;
    ListView citylist;

    boolean isFirst = true;

    private List<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_choose);

        surechooseIv= findViewById(R.id.surechange_Iv);
        citychooseTv=findViewById(R.id.city_choose);
        citylist = findViewById(R.id.listview);


        surechooseIv.setOnClickListener(this);
        citychooseTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.surechange_Iv:

                Intent intent = new Intent(this,SearchActivity.class);

                if(citychooseTv.getText().equals("选择地区")){

                }else{
                    intent.putExtra("city",citychooseTv.getText());
                }

                startActivity(intent);
                break;


            case R.id.city_choose:

                ShowDialog();//弹框操作

                break;


        }




    }

    public void ShowDialog() {

        //避免连续点击，list连续添加 只在第一次点击时添加
        if(isFirst){
            //310000(上海市)、330200(宁波市)、610100(西安市)、440300(深圳市)、北京市(110000)
            list.add("上海");
            list.add("宁波");
            list.add("西安");
            list.add("深圳");
            list.add("北京");
            isFirst=false;
        }



        CityAdapter adapter = new CityAdapter(list,this);
        adapter.notifyDataSetChanged();
        citylist.setAdapter(adapter);
        citylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
                //在这里面就是执行点击后要进行的操作,这里只是做一个显示
                citychooseTv.setText(list.get(positon));

            }
        });


    }
}
