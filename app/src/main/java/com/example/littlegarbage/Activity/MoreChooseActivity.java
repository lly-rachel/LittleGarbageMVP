package com.example.littlegarbage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.littlegarbage.Adapter.CityAdapter;
import com.example.littlegarbage.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MoreChooseActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView surechooseIv;
    TextView citychooseTv,deleteTv;
    ListView citylist;

    boolean isFirst = true;

    private List<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_choose);

        surechooseIv= findViewById(R.id.surechange_Iv);
        citychooseTv=findViewById(R.id.city_choose);
        deleteTv = findViewById(R.id.delete_text);
        citylist = findViewById(R.id.listview);


        surechooseIv.setOnClickListener(this);
        citychooseTv.setOnClickListener(this);
        deleteTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.surechange_Iv:

                Intent intent = new Intent(this, SearchActivity.class);

                if(citychooseTv.getText().equals("选择地区")){

                }else{
                    intent.putExtra("city",citychooseTv.getText());
                }

                startActivity(intent);
                break;


            case R.id.city_choose:

                ShowDialog();//弹框操作

                break;

            case R.id.delete_text:


                deleteFile();//删除文件



                break;

        }




    }


    public void deleteFile() {

        File file = new File(Environment.getExternalStorageDirectory()+ "/BitmapTest"+"/test.amr");
        if (file.exists()) {
            file.delete();
        }


        File share_picture = new File(Environment.getExternalStorageDirectory() + "/BitmapTest"+"/share.png");
        if (share_picture.exists()) {
            share_picture.delete();
        }

        File picture = new File(Environment.getExternalStorageDirectory() + "/BitmapTest"+"/output_image.png");
        if (picture.exists()) {
            picture.delete();
        }

        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/BitmapTest");
        if(path.exists())
        {
            path.delete();
        }

        if(!file.exists()&&!picture.exists()&&!share_picture.exists()&&!path.exists()){
            Toast.makeText(this,"已清理所有数据文件",Toast.LENGTH_SHORT).show();
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
