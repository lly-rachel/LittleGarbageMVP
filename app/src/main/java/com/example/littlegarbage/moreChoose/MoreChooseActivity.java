package com.example.littlegarbage.moreChoose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.littlegarbage.search.SearchActivity;
import com.example.littlegarbage.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreChooseActivity extends AppCompatActivity implements MoreChooseContract.View {


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
    private MoreChoosePresenter moreChoosePresenter ;
    String basepath = Environment.getExternalStorageDirectory() + "/BitmapTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_choose);
        ButterKnife.bind(this);

        moreChoosePresenter = new MoreChoosePresenter(this);

    }

    @OnClick({R.id.surechange_Iv, R.id.delete_text, R.id.city_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.surechange_Iv:

                moreChoosePresenter.clickSure();

                break;

            case R.id.delete_text:
                //删除文件
                moreChoosePresenter.deleteFile(this);

                break;

            case R.id.city_choose:
                //弹框操作
                moreChoosePresenter.ShowDialog(this,isFirst,list,citychooseTv,citylist);

                break;
        }
    }

    @Override
    public void deleteFileFinished() {

        File file = new File(basepath);
        if(!file.exists()){
            showToastShort(this,"数据已清理完毕");
        }
    }

    @Override
    public void clickSureFinished() {
        Intent intent = new Intent(this, SearchActivity.class);

        if (citychooseTv.getText().equals("选择地区")) {

        } else {
            intent.putExtra("city", citychooseTv.getText());
        }

        startActivity(intent);
    }

    @Override
    public void ShowDialogFinished() {

    }

    void showToastShort(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
