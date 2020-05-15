package com.example.littlegarbage.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Base64;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.Toast;

import com.example.littlegarbage.json.GarbageBean;
import com.example.littlegarbage.Util.GetHttpData;
import com.example.littlegarbage.Util.HttpUtil;
import com.example.littlegarbage.json.JsonParser;
import com.example.littlegarbage.R;
import com.example.littlegarbage.Adapter.SearchHistoryAdapter;
import com.example.littlegarbage.db.DBManeger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    AutoCompleteTextView seachnameATV;

    /*显示联想词*/
    GridView hot_historyGv;
    private ArrayAdapter<String> arrayAdapter;
    Handler hd;
    String Imagename;
    String imageUrl;

    /*拍照用*/
    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    String imgBase;

    /*获取相册图片用*/
    public static final int CHOOSE_PHOTO = 2;

    /*录音用*/
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO};
    boolean isFirst = true;//判断是第一次点击录音，第二次点击停止录音

    private static int REQUEST_PERMISSION_CODE = 3;


    MediaRecorder recorder;
    File audioFile; //录音保存的文件
    boolean isRecoding=false;// true 表示正在录音


    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    ImageView seachIv,soundIv,photoIv,takepictureIv;
    ImageView shezhiIv;
    ListView historyLv;
    String garbage;
    SearchHistoryAdapter historyAdapter;
    List<String> garbagenameList;

    /*选择地区*/
    Map<String,Integer> city = new HashMap<>();
    static String cityname = null;
    static String citydaima = null;

    final static String hotSearchHistoryURL = "https://api.tianapi.com/txapi/hotlajifenlei/index?key=2fb9da721d164cdc0a45b990545796fa";
    final static String imageNameURL = "https://api.zhetaoke.com:10001/api/api_suggest.ashx?appkey=3982f6785fcd4b54a214c69f4c167477";

    final static List<String> newdata= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        Intent intent=getIntent();
        cityname=intent.getStringExtra("city");

        iniDetail();


        hot_historyGv=findViewById(R.id.hot_history_Gridview);


       hd = new Handler();

        // 启用网络线程
        HttpThreadToGetData ht = new HttpThreadToGetData();
        ht.start();


        historyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                String garbage = (String) historyAdapter.getItem(index);
                getTheGarbageMessageToIntent(garbage);
            }
        });


        iniEdt();

    }


    /*获取热门搜索数据*/
    public class HttpThreadToGetData extends Thread{

        @Override
        public void run() {
            super.run();
            try {
                String data = GetHttpData.GetHotData(hotSearchHistoryURL);
                final String finalWi = data;
                // 多线程更新 UI
                hd.post(new Runnable() {
                    @Override
                    public void run() {
                        setData(finalWi);
                    }
                });


            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*解析获取的垃圾分类最多搜索记录的json数据*/
    public void setData(String data)  {

        if(data!=null){
            JSONObject joname = null;
            try {
                joname = new JSONObject(data);
                if(joname.getInt("code")==200){
                    JSONArray listArray = joname.getJSONArray("newslist");
                    for(int i = 0;i<listArray.length();i++){
                        JSONObject  jsonArray= listArray.getJSONObject(i);
                        String name =jsonArray.getString("name");
                        Integer type = jsonArray.getInt("type");
                        Integer index = jsonArray.getInt("index");
                        if(name.length()<5&&newdata.size()<16&&!newdata.contains(name)){
                            newdata.add(name);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }


        arrayAdapter = new ArrayAdapter<>(this,R.layout.item_hotgarbage,newdata);
        hot_historyGv.setAdapter(arrayAdapter);
        setListener();
    }

    /*热门搜索的点击事件*/
    private void setListener() {

        hot_historyGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                garbage = newdata.get(position);
                getTheGarbageMessageToIntent(garbage);
            }
        });


    }

    /*根据传进garbage到展示界面*/
    private void getTheGarbageMessageToIntent(String garbage) {

        Intent intent = new Intent(this, ShowGarbageDetailActivity.class);
        intent.putExtra("garbage",garbage);
        intent.putExtra("citydaima",citydaima);
        startActivity(intent);

    }


    /*初始化AutoCompeleteTextView，设置适配器*/
    public  void iniEdt() {

        seachnameATV = findViewById(R.id.garbage_search_autoCompelete);
        seachnameATV.setThreshold(1);


        /*获取输入框监听  联想词可操作*/
        seachnameATV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String name=seachnameATV.getText().toString();
         //       String name = String.valueOf(s);
                try {
                    Imagename = java.net.URLEncoder.encode(name,"UTF-8");
                    GetImageData(Imagename);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }



            }

        });


    }

    /*根据输入的文本 传入content参数*/
    private void GetImageData(String name) {

        imageUrl  = imageNameURL+"&content="+name;
        //网络请求
        HttpThreadToGetImageData httpThreadToGetImageData = new HttpThreadToGetImageData();
        httpThreadToGetImageData.start();

    }

    /*获取联想词数据*/
    public class HttpThreadToGetImageData extends Thread{

        @Override
        public void run() {
            super.run();
            try {
                String imageData = GetHttpData.GetHotData(imageUrl);
                final String finalImageData =imageData;
                // 多线程更新 UI
                hd.post(new Runnable() {
                    @Override
                    public void run() {
                        setImageData(finalImageData);
                    }
                });


            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /*解析联想词数据*/
    private void setImageData(String finals) {

        List<String> ImageNameList = new ArrayList<>();
        if(finals!=null){
            JSONObject joname = null;
            try {
                joname = new JSONObject(finals);

                JSONArray listArray = joname.getJSONArray("result");
                for(int i = 0;i<listArray.length();i++){
                    JSONArray NameArray = listArray.getJSONArray(i);
                    String name = NameArray.getString(0);
                    ImageNameList.add(name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> atvArrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line,ImageNameList);

            seachnameATV.setAdapter(atvArrayAdapter);
            atvArrayAdapter.notifyDataSetChanged();

        }

    }

    private void iniDetail()  {

        seachIv = findViewById(R.id.garbage_search);
        soundIv = findViewById(R.id.search_sound);
        photoIv = findViewById(R.id.search_photo);
        takepictureIv = findViewById(R.id.search_takepicture);
        shezhiIv = findViewById(R.id.shezhi);
        historyLv = findViewById(R.id.search_history);
        historyLv.setAdapter(historyAdapter);


        seachIv.setOnClickListener(this);
        soundIv.setOnClickListener(this);
        photoIv.setOnClickListener(this);
        takepictureIv.setOnClickListener(this);
        shezhiIv.setOnClickListener(this);

        garbagenameList = DBManeger.queryAllGarbageName();


        historyAdapter = new SearchHistoryAdapter(this,garbagenameList);
        historyLv.setAdapter(historyAdapter);

        //310000(上海市)、330200(宁波市)、610100(西安市)、440300(深圳市)、北京市(110000)
        //垃圾分类api支持的城市
        city.put("上海",310000);
        city.put("宁波",330200);
        city.put("西安",610100);
        city.put("深圳",440300);
        city.put("北京",110000);

        if(cityname!=null){
            citydaima = String.valueOf(city.get(cityname));
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.shezhi:

                Intent intent = new Intent(this, MoreChooseActivity.class);
                startActivity(intent);

                break;

            case R.id.garbage_search:

                garbage = seachnameATV.getText().toString();

                if(!TextUtils.isEmpty(garbage)){

                    getTheGarbageMessageToIntent(garbage);

                }else{
                    Toast.makeText(this,"输入信息不能为空",Toast.LENGTH_LONG).show();
                }

                break;

                /*拍照*/
            case R.id.search_takepicture:

                startTakePicture();

                break;

                /*获取相册图片*/
            case R.id.search_photo:

                if(ContextCompat.checkSelfPermission
                        (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
                break;

                /*录音，获取音频文件*/
            case R.id.search_sound:

             //   Toast.makeText(this,"功能尚在开发中，敬请期待...",Toast.LENGTH_SHORT).show();

                open(this);//动态获取权限

                if(isFirst){
                    soundIv.setImageResource(R.mipmap.yuyinzanting);

                    startin();

                    isFirst=false;
                }else{
                    soundIv.setImageResource(R.mipmap.yuyin);


                    stopin();
//                    HttpThreadToGetSoundName getsoundName = new HttpThreadToGetSoundName();
//                    getsoundName.start();

                    Toast.makeText(this,"音频文件存于->"+audioFile.getAbsolutePath()+"\n该功能尚在开发中，敬请期待...",Toast.LENGTH_LONG).show();
                  //  Toast.makeText(this,"该功能尚在开发中，敬请期待...",Toast.LENGTH_SHORT).show();
                    isFirst=true;
                }


                break;

        }
    }




    private void startVoiceRecognitionActivity() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,

                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");

        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /*初始化MediaRecorder*/
    public void init(){

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置播放源 麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB); //设置输入格式 3gp
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); //设置编码 AMR


    }

    /*实现录音功能*/
    public void recod(){

        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this, "SD卡不存在，请插入SD卡！", Toast.LENGTH_LONG).show();
            return;
        }

        //这里为文件保存路径
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/BitmapTest");
        init();
        if(!path.exists())
        {
            path.mkdirs();
        }

        try {
            //这个地方写文件名，可以利用时间来保存为不同的文件名
            audioFile=new File(path,"test.amr");
            if(audioFile.exists())
            {
                audioFile.delete();
            }
            audioFile.createNewFile();//创建文件

        } catch (Exception e) {
            throw new RuntimeException("Couldn't create recording audio file", e);
        }


        recorder.setOutputFile(audioFile.getAbsolutePath());

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            throw new RuntimeException("IllegalStateException on MediaRecorder.prepare", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException on MediaRecorder.prepare", e);
        }
        isRecoding=true;
        recorder.start();
    }

    /*开始录音*/
    public void startin(){
        Toast.makeText(this,"开始录音",Toast.LENGTH_SHORT).show();
        recod();
    }

    /*停止录音*/
    public void stopin(){
        if(isRecoding)
        {
            Toast.makeText(this,"停止录音",Toast.LENGTH_SHORT).show();
            if (recorder != null){
                try {

                    recorder.stop();
                } catch (IllegalStateException e) {

                    //e.printStackTrace();
                    recorder = null;
                    recorder = new MediaRecorder();
                }
                recorder.release();
                recorder = null;

            }
        }
    }




    /*网络请求，获取语音识别的数据*/
    public class HttpThreadToGetSoundName extends Thread{

        @Override
        public void run() {
            super.run();

            GarbageBean garbageBean = null;
            JsonParser jp = new JsonParser();

            // 城市代码
            String garbageString = null;
            try {

                String model = android.os.Build.MODEL;
                String version_release = android.os.Build.VERSION.RELEASE;
                Integer packagecode = packageCode(SearchActivity.this);

                garbageString = HttpUtil.sendOkHttpSoundRequest(SearchActivity.this,audioFile,model,version_release,packagecode,citydaima);

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }

            if (garbageString == null) {
                garbageString="数据获取错误";

            }else{

                // 多线程更新 UI
                final String finalGarbageString = garbageString;
                hd.post(new Runnable() {
                    @Override
                    public void run() {

                        getTheGarbageMessage(finalGarbageString);
                    }
                });

            }




        }
    }

    /*获取客户端版本号*/
    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /*动态获取权限*/
    public static void open(Activity obj){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0 ; i < PERMISSIONS_STORAGE.length ; i++){
                if (ActivityCompat.checkSelfPermission(obj,
                        PERMISSIONS_STORAGE[i])!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(obj, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                }
            }
        }
    }


    private void openAlbum() {

        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"您拒绝了相册访问",Toast.LENGTH_LONG).show();
                }
                break;

            default:break;
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
        if(outputImage.exists())
        {
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();//创建文件
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT>=24){
            imageUri = FileProvider.getUriForFile
                    (this,"com.example.littlegarbage.fileprovider",outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }
        /*启动相机程序*/
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
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

//                case VOICE_RECOGNITION_REQUEST_CODE:
//                    ArrayList matches = data.getStringArrayListExtra(
//
//                            RecognizerIntent.EXTRA_RESULTS);
//
//                    seachnameATV.setText((Integer) matches.get(0));
//                    //移动光标至最后一个字符，使识别语音后显示联想词
//                    seachnameATV.setSelection(((String) matches.get(0)).length());

                default:
                    break;
            }
        }


    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private void handleImageOnKitKat(Intent data) {
        String imagePath=null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse(
                        "content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                //如果是content类型的uri，则使用普通方法处理
                imagePath=getImagePath(uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                //如果是file类型的uri，直接获取图片路径即可
                imagePath=uri.getPath();
            }
            displayImage(imagePath);
        }
    }

    /*获取图片路径*/
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片的路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if(imagePath!=null){
            Bitmap bitmap =BitmapFactory.decodeFile(imagePath);
            getThePictureName(bitmap);

        }else{
            Toast.makeText(this,"获取照片失败",Toast.LENGTH_LONG).show();
        }
    }

    private void getThePictureName(Bitmap bitmap) {

        Bitmap bm = compressScale(bitmap);

        imgBase = bitmaptoString(bm);

        Toast.makeText(this,"获取信息中...请耐心等待\n超过10s不跳转界面视为获取该信息失败",Toast.LENGTH_LONG).show();
        // 启用网络线程
        HttpThreadToGetPictureName ht = new HttpThreadToGetPictureName();
        ht.start();

    }

    /*网络请求，获取图像识别的数据*/
    public class HttpThreadToGetPictureName extends Thread{

        @Override
        public void run() {
            super.run();

            GarbageBean garbageBean = null;
            JsonParser jp = new JsonParser();

            // 城市代码
            String garbageString = null;
            try {

                garbageString = HttpUtil.sendOkHttpPictureRequest(imgBase,citydaima);

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }

            if (garbageString == null) {
                garbageString="数据获取错误";

            }else{

                // 多线程更新 UI
                final String finalGarbageString = garbageString;
                hd.post(new Runnable() {
                    @Override
                    public void run() {

                        getTheGarbageMessage(finalGarbageString);
                    }
                });

            }




        }
    }

    /*解析图片识别的json数据*/
    public void getTheGarbageMessage(String finalstring){
        List<GarbageBean.ResultBean.GarbageInfoBean> NameList = new ArrayList<>();
        Double confidence_max=0.0;
        String garbageName = null;
        if(finalstring!=null) {
            JSONObject joname = null;
            try {
                joname = new JSONObject(finalstring);
                String code = joname.getString("code");
                if(code.equals("10000")){
                    String result = joname.getString("result");

                    JSONObject resultGarbage = new JSONObject(result);

                    JSONArray listArray = resultGarbage.getJSONArray("garbage_info");

                    String cate_name = null;
                    String city_id = null;
                    String city_name = null;
                    double confidence = 0;
                    String garbage_name = null;
                    String ps = null;

                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject jsonArray = listArray.getJSONObject(i);
                        cate_name = jsonArray.getString("cate_name");
                        city_id = jsonArray.getString("city_id");
                        city_name = jsonArray.getString("city_name");
                        confidence = jsonArray.getDouble("confidence");
                        garbage_name = jsonArray.getString("garbage_name");
                        ps = jsonArray.getString("ps");
                        if (confidence >= confidence_max) {
                            //找到可信度最大的
                            confidence_max = confidence;
                            GarbageBean.ResultBean.GarbageInfoBean garbageInfoBean = new GarbageBean.ResultBean.GarbageInfoBean
                                    (cate_name, city_id, city_name, confidence, garbage_name, ps);
                            NameList.add(garbageInfoBean);
                        }

                    }

                    for (int i = 0; i < NameList.size(); i++) {
                        if (confidence_max == NameList.get(i).getConfidence()) {
                            GarbageBean.ResultBean.GarbageInfoBean gib = NameList.get(i);
                            Intent intent = new Intent(this,ShowGarbageDetailActivity.class);
                            intent.putExtra("bean", gib);
                            intent.putExtra("citydaima",citydaima);
                           // intent.putExtra("garbage",gib.getGarbage_name());
                            startActivity(intent);
                            break;
                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* 压缩图片(确保图片小于2M)*/
    public static Bitmap compressScale(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为

        float hh = 512f;
        float ww = 512f;

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }

        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了

        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }


    /*质量压缩*/
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 700) { // 循环判断如果压缩后图片是否大于700kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;

    }


    /*将图像进行Base64编码*/
    public String bitmaptoString(Bitmap bitmap) {

        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }
}
