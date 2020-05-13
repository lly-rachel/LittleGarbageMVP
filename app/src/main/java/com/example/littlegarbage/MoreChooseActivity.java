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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MoreChooseActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView surechooseIv;
    RelativeLayout diqu_layout;

    private List<String> list = new ArrayList<String>();
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_choose);

        surechooseIv= findViewById(R.id.surechange_Iv);
        diqu_layout = findViewById(R.id.diqu_layout);

        surechooseIv.setOnClickListener(this);
        diqu_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.surechange_Iv:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;


            case R.id.diqu_layout:
            //310000(上海市)、330200(宁波市)、610100(西安市)、440300(深圳市)、北京市(110000)
                list.add("上海");
                list.add("宁波");
                list.add("西安");
                list.add("深圳");
                list.add("北京");
                ShowDialog();//弹框操作

                break;
        }




    }

    public void ShowDialog() {

        Context context = this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout., null);
        ListView myListView = (ListView) layout.findViewById(R.id.listview);
        CityAdapter adapter = new CityAdapter(list,this);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
                //在这里面就是执行点击后要进行的操作,这里只是做一个显示
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();
    }
}
