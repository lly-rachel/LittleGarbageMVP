package com.example.littlegarbage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
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

    GridView hot_historyGv;
    private ArrayAdapter<String> arrayAdapter;

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

        historyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });

        iniEdt();

    }

    private void onListItemClick(int index) {

        Intent intent=new Intent(this,ShowGarbageDetailActivity.class);
        String garbage = (String) historyAdapter.getItem(index);
        intent.putExtra("garbage",garbage);
        this.startActivity(intent);
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
        historyLv.setAdapter(historyAdapter);


        seachIv.setOnClickListener(this);
        soundIv.setOnClickListener(this);
        photoIv.setOnClickListener(this);


        garbagenameList = DBManeger.queryAllGarbageName();


        historyAdapter = new SearchHistoryAdapter(this,garbagenameList);
        historyLv.setAdapter(historyAdapter);

        hot_historyGv=findViewById(R.id.hot_history_Gridview);
        arrayAdapter=new ArrayAdapter<>(this,R.layout.);
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
