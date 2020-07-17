package com.example.littlegarbage.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.littlegarbage.Adapter.CityAdapter;
import com.example.littlegarbage.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreChooseActivity extends AppCompatActivity {


    boolean isFirst = true;
    @BindView(R.id.surechange_Iv)
    ImageView surechooseIv;
    @BindView(R.id.delete_text)
    TextView deleteTv;
    @BindView(R.id.city_choose)
    TextView citychooseTv;
    @BindView(R.id.listview)
    ListView citylist;

    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_choose);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.surechange_Iv, R.id.delete_text, R.id.city_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.surechange_Iv:
                Intent intent = new Intent(this, SearchActivity.class);

                if (citychooseTv.getText().equals("选择地区")) {

                } else {
                    intent.putExtra("city", citychooseTv.getText());
                }

                startActivity(intent);
                break;

            case R.id.delete_text:

                deleteFile();//删除文件

                break;

            case R.id.city_choose:

                ShowDialog();//弹框操作

                break;
        }
    }

    public void deleteFile() {

        String basepath = Environment.getExternalStorageDirectory() + "/BitmapTest";

        File file = new File(basepath + "/yinfu.wav");
        if (file.exists()) {
            file.delete();
        }

        File soundfile = new File(basepath + "/yinfu.pcm");
        if (soundfile.exists()) {
            soundfile.delete();
        }

        File share_picture = new File(basepath + "/share.png");
        if (share_picture.exists()) {
            share_picture.delete();
        }

        File picture = new File(basepath + "/output_image.png");
        if (picture.exists()) {
            picture.delete();
        }

        File path = new File(basepath+ "/BitmapTest");
        if (path.exists()) {
            path.delete();
        }

        if (!file.exists() && !picture.exists() && !share_picture.exists() && !path.exists()) {
            Toast.makeText(this, "已清理所有数据文件", Toast.LENGTH_SHORT).show();
        }

    }

    public void ShowDialog() {

        //避免连续点击，list连续添加,只在第一次点击时添加
        if (isFirst) {
            //310000(上海市)、330200(宁波市)、610100(西安市)、440300(深圳市)、北京市(110000)
            list.add("上海");
            list.add("宁波");
            list.add("西安");
            list.add("深圳");
            list.add("北京");
            isFirst = false;
        }

        CityAdapter adapter = new CityAdapter(list, this);
        adapter.notifyDataSetChanged();
        citylist.setAdapter(adapter);
        citylist.setOnItemClickListener((arg0, arg1, positon, id) -> citychooseTv.setText(list.get(positon)));

    }


}
