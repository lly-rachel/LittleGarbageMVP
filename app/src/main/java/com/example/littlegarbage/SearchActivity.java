package com.example.littlegarbage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.littlegarbage.db.DBManeger;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    EditText seachnameET;
    ImageView seachIv,soundIv,photoIv;
    ListView historyLv;
    String garbage;
    SearchHistoryAdapter historyAdapter;
    List<String> garbagenameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        iniDetail();

    }

    private void iniDetail() {
        seachnameET = findViewById(R.id.garbage_search_editview);
        seachIv = findViewById(R.id.garbage_search);
        soundIv = findViewById(R.id.search_sound);
        photoIv = findViewById(R.id.search_photo);
        historyLv = findViewById(R.id.search_history);

        seachIv.setOnClickListener(this);
        soundIv.setOnClickListener(this);
        photoIv.setOnClickListener(this);

        garbagenameList = DBManeger.queryAllGarbageName();

        /*测试数据*/
        if(garbagenameList.size()==0){
         garbagenameList.add("西瓜皮");
        }

        historyAdapter = new SearchHistoryAdapter(this,garbagenameList);
        historyLv.setAdapter(historyAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.garbage_search:

                garbage = seachnameET.getText().toString();

                if(!TextUtils.isEmpty(garbage)){




                }else{
                    Toast.makeText(this,"输入垃圾不能为空",Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.search_sound:

                break;

            case R.id.search_photo:

                break;
        }
    }
}
