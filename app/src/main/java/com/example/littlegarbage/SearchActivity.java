package com.example.littlegarbage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.littlegarbage.db.DBManeger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        iniEdt();

    }

    private void iniEdt() {
        seachnameET = findViewById(R.id.garbage_search_editview);

        /*获取输入框监听  联想词可操作*/
        seachnameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    private void iniDetail() {

        seachIv = findViewById(R.id.garbage_search);
        soundIv = findViewById(R.id.search_sound);
        photoIv = findViewById(R.id.search_photo);
        historyLv = findViewById(R.id.search_history);

        seachIv.setOnClickListener(this);
        soundIv.setOnClickListener(this);
        photoIv.setOnClickListener(this);


        garbagenameList = DBManeger.queryAllGarbageName();


        historyAdapter = new SearchHistoryAdapter(this,garbagenameList);


//        final String[] textView = {null};
//
//        historyAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                textView[0] = (String) ((TextView) view).getText();
//
//            }
//        });
//        if(textView[0]!=null){
//            Intent intent = new Intent(this,ShowGarbageDetailActivity.class);
//            intent.putExtra("garbage",textView[0]);
//            startActivity(intent);
//        }

        historyLv.setAdapter(historyAdapter);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.garbage_search:

                garbage = seachnameET.getText().toString();

                if(!TextUtils.isEmpty(garbage)){

                    Intent intent = new Intent(this,ShowGarbageDetailActivity.class);
                    intent.putExtra("garbage",garbage);
                    startActivity(intent);

                }else{
                    Toast.makeText(this,"输入信息不能为空",Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.search_sound:

                break;

            case R.id.search_photo:

                break;
        }
    }



}
