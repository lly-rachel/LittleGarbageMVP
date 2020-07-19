package com.example.littlegarbage.search;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.littlegarbage.show.ShowDetailActivity;
import com.example.littlegarbage.adapter.SearchHistoryAdapter;
import com.example.littlegarbage.model.db.GarbageDataBase;
import com.example.littlegarbage.model.db.GarbageDataDao;
import com.example.littlegarbage.R;
import com.example.littlegarbage.utils.AudioUtil;
import com.example.littlegarbage.utils.PictureUtil;
import com.example.littlegarbage.model.bean.GarbageBean;
import com.example.littlegarbage.moreChoose.MoreChooseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements SearchActivityContract.View {

    @BindView(R.id.garbage_search_autoCompelete)
    AutoCompleteTextView searchnameATV;
    @BindView(R.id.search_sound)
    ImageView soundIv;

    /*显示联想词*/
    @BindView(R.id.hot_history_Gridview)
    GridView hot_historyGv;

    /*显示搜索历史*/
    @BindView(R.id.search_history)
    GridView searchHistoryGv;

    private ArrayAdapter<String> arrayAdapter;

    String Imagename;
    String imageUrl;

    Handler hd;

    /*拍照用*/
    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    String imgBase;

    /*获取相册图片用*/
    public static final int CHOOSE_PHOTO = 2;

    /*录音用*/
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};
    boolean isFirst = true;//判断是第一次点击录音，第二次点击停止录音

    private static int REQUEST_PERMISSION_CODE = 3;

    /*利用AudioRecorder录制wav文件*/
    AudioUtil audioUtil = AudioUtil.getInstance();

    String garbage;
    SearchHistoryAdapter historyAdapter;
    List<String> garbagenameList;

    /*选择地区*/
    Map<String, Integer> city = new HashMap<>();
    static String cityname = null;
    static String citydaima = null;

    final static String hotSearchHistoryURL = "https://api.tianapi.com/txapi/hotlajifenlei/index?key=2fb9da721d164cdc0a45b990545796fa";
    final static String imageNameURL = "https://api.zhetaoke.com:10001/api/api_suggest.ashx?appkey=3982f6785fcd4b54a214c69f4c167477";

    final static List<String> newdata = new ArrayList<>();

    public GarbageDataDao garbageDataDao;

    private SearchActivityPresenter searchActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchActivityPresenter = new SearchActivityPresenter(this);

        GarbageDataBase garbageDataBase = Room.databaseBuilder(
                this, GarbageDataBase.class, "garbage_database").build();
        garbageDataDao = garbageDataBase.getGarbageDataDao();

        //隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        hd = new Handler();


        Intent intent = getIntent();
        cityname = intent.getStringExtra("city");

        initViews();

        searchActivityPresenter.getHotSearchData(hotSearchHistoryURL);


    }

    @OnClick({R.id.shezhi, R.id.garbage_search, R.id.search_sound, R.id.search_takepicture, R.id.search_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.shezhi:

                searchActivityPresenter.clickshezhi();

                break;

            case R.id.garbage_search:

                searchActivityPresenter.clickSearch();

                break;

            /*录音，获取音频文件*/
            case R.id.search_sound:

                open(this);//动态获取权限

                if (isFirst) {
                    Glide.with(this).load(R.mipmap.yuyinzanting).into(soundIv);

                    audioUtil.startRecord();
                    audioUtil.recordData();

                    showToastShort(this, "开始录音");

                    isFirst = false;
                } else {
                    Glide.with(this).load(R.mipmap.yuyin).into(soundIv);

                    audioUtil.stopRecord();
                    audioUtil.convertWaveFile();
                   showToastShort(this, "停止录音，获取信息中...");

                    searchActivityPresenter.getSoundData(this,citydaima);


                    isFirst = true;
                }

                break;

            /*拍照*/
            case R.id.search_takepicture:

                startTakePicture();

                break;

            /*获取相册图片*/
            case R.id.search_photo:

                //动态获取内存
                if (ContextCompat.checkSelfPermission
                        (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }

                break;
        }
    }

    @Override
    public void clickshezhiFinished() {
        Intent intent = new Intent(this, MoreChooseActivity.class);
        startActivity(intent);
    }

    @Override
    public void clickSearchFinished() {
        garbage = searchnameATV.getText().toString();
        if (!TextUtils.isEmpty(garbage)) {
            getTheGarbageMessageToIntent(garbage);//讲获取到的垃圾名称发送到展示界面
        } else {
           showToastShort(this, "输入信息不能为空");
        }
    }

    @Override
    public void getDataOnSucceed(GarbageBean garbageBean) {
        Thread thread = new Thread(()->{
            // 多线程更新 UI
            hd.post(() -> getTheGarbageMessage(garbageBean));
        });
        thread.start();
    }

    @Override
    public void getDataOnSucceed(String data) {
        Thread thread = new Thread(()->{
            // 多线程更新 UI
            hd.post(() -> setData(data));
        });
        thread.start();

    }

    @Override
    public void getImageDataOnSucceed(String data) {

        Thread thread = new Thread(()->{
            // 多线程更新 UI
            hd.post(() -> setImageData(data));
        });
        thread.start();

    }

    @Override
    public void getDataOnFailed() {
        showToastLong(this, "获取数据失败");
    }

    void showToastShort(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    void showToastLong(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /*解析图片识别的json数据*/
    public void getTheGarbageMessage(GarbageBean garbageBean) {

        List<GarbageBean.ResultBean.GarbageInfoBean> NameList  =
                garbageBean.getResult().garbage_info;
        Double confidence = NameList.get(0).getConfidence();
        int maxindex=0;

        for(int i = 1;i < NameList.size();i++){
            if(confidence<NameList.get(i).getConfidence()){
                confidence=NameList.get(i).getConfidence();
                maxindex=i;
            }
        }

        GarbageBean.ResultBean.GarbageInfoBean gib = NameList.get(maxindex);
        Intent intent = new Intent(this, ShowDetailActivity.class);
        intent.putExtra("bean", gib);//利用序列化将bean传入显示界面
        intent.putExtra("citydaima", citydaima);
        startActivity(intent);


    }

    /*动态获取权限*/
    public static void open(Activity obj) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < PERMISSIONS_STORAGE.length; i++) {
                if (ActivityCompat.checkSelfPermission(obj,
                        PERMISSIONS_STORAGE[i]) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(obj, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                }
            }
        }
    }

    private void startTakePicture() {

        /*创建File对象，存储拍照后的图片*/
        String sdCardDir = Environment.getExternalStorageDirectory() + "/BitmapTest";
        File dirFile = new File(sdCardDir);  //目录转化成文件夹
        if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
            dirFile.mkdirs();
        }
        File outputImage = new File(sdCardDir, "output_image.png");
        if (outputImage.exists()) {
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();//创建文件
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile
                    (this, "com.example.littlegarbage.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        /*启动相机程序*/
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {

                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream
                                (getContentResolver().openInputStream(imageUri));

                        getThePictureName(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下系统
                        handleImageBeforeKitKat(data);
                    }
                }

            default:
                break;
        }
    }

    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse(
                        "content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的uri，则使用普通方法处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是file类型的uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private void getThePictureName(Bitmap bitmap) {

        Bitmap bm = PictureUtil.compressScale(bitmap);//压缩图片

        imgBase = PictureUtil.bitmaptoString(bm);//获取图像的Base64编码
        showToastShort(this, "获取信息中...请耐心等待");

        searchActivityPresenter.getTakePictureData(imgBase, citydaima);

    }

    /*获取图片路径*/
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片的路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /*解析获取的垃圾分类最多搜索记录的json数据*/
    public void setData(String data) {

        if (data != null) {
            JSONObject joname = null;
            try {
                joname = new JSONObject(data);
                if (joname.getInt("code") == 200) {
                    JSONArray listArray = joname.getJSONArray("newslist");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject jsonArray = listArray.getJSONObject(i);
                        String name = jsonArray.getString("name");
                        if (name.length() < 5 && newdata.size() < 16 && !newdata.contains(name)) {
                            newdata.add(name);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item_hotgarbage, newdata);
        hot_historyGv.setAdapter(arrayAdapter);

        /*热门搜索的点击事件*/
        hot_historyGv.setOnItemClickListener((parent, view, position, id) -> {
            garbage = newdata.get(position);
            getTheGarbageMessageToIntent(garbage);
        });

    }

    /*根据传进garbage到展示界面*/
    private void getTheGarbageMessageToIntent(String garbage) {

        Intent intent = new Intent(this, ShowDetailActivity.class);
        intent.putExtra("garbage", garbage);
        intent.putExtra("citydaima", citydaima);
        startActivity(intent);

    }

    /*根据输入的文本 传入content参数*/
    private void GetImageData(String name) {

        imageUrl = imageNameURL + "&content=" + name;
        searchActivityPresenter.getImageData(imageUrl);

    }

    /*解析联想词数据*/
    private void setImageData(String finals) {

        List<String> ImageNameList = new ArrayList<>();
        if (finals != null) {
            JSONObject joname = null;
            try {
                joname = new JSONObject(finals);

                JSONArray listArray = joname.getJSONArray("result");
                for (int i = 0; i < listArray.length(); i++) {
                    JSONArray NameArray = listArray.getJSONArray(i);
                    String name = NameArray.getString(0);
                    ImageNameList.add(name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> atvArrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, ImageNameList);

            searchnameATV.setAdapter(atvArrayAdapter);
            atvArrayAdapter.notifyDataSetChanged();

        }

    }

    private void initViews() {

        Thread thread = new Thread(() -> {
            garbagenameList = garbageDataDao.queryAllGarbageName();
            if (garbagenameList != null) {
                historyAdapter = new SearchHistoryAdapter(this, garbagenameList);
                searchHistoryGv.setAdapter(historyAdapter);
                searchHistoryGv.setOnItemClickListener((arg0, v, index, arg3) -> {
                    String garbage = (String) historyAdapter.getItem(index);
                    getTheGarbageMessageToIntent(garbage);
                });
            }
        });
        thread.start();

        //310000(上海市)、330200(宁波市)、610100(西安市)、440300(深圳市)、北京市(110000)
        //垃圾分类api支持的城市
        city.put("上海", 310000);
        city.put("宁波", 330200);
        city.put("西安", 610100);
        city.put("深圳", 440300);
        city.put("北京", 110000);

        if (cityname != null) {//从设置里接收到城市名字
            citydaima = String.valueOf(city.get(cityname));//转换城市id
        }


        /*初始化AutoCompeleteTextView，设置适配器*/
        searchnameATV.setThreshold(1);//输入一个字符就开始展示联想词


        /*获取输入框监听  联想词可操作*/
        searchnameATV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String name = searchnameATV.getText().toString();

                try {
                    Imagename = URLEncoder.encode(name, "UTF-8");
                    GetImageData(Imagename);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

        });

    }

    private void openAlbum() {

        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    showToastLong(this, "您拒绝了相册访问");
                }
                break;

            default:
                break;
        }
    }



    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            getThePictureName(bitmap);

        } else {
           showToastLong(this, "获取照片失败");
        }
    }












}
