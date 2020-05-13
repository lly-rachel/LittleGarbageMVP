package com.example.littlegarbage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShotShareUtil {

    final static String BitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/BitmapTest"+"/share.png";

    /**截屏分享，供外部调用**/
    public static void shotShare(Context context,Bitmap bitmap){

        //保存
        saveBitmap(context,bitmap);

        //分享

        ShareImage(context,BitmapPath);

    }


    //保存bitmap至sd卡
    public static void saveBitmap(Context context,Bitmap bmp) {

        try { // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/BitmapTest";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦
            File file = new File(sdCardDir, "share.png");// 在SDcard的目录下创建图片文,以当前时间为其命名
            if(file.exists())
            {
                file.delete();
            }
            file.createNewFile();//创建文件


            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**分享**/
    private static void ShareImage(Context context,String imagePath){
        if (imagePath != null){
            Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
            File file = new File(imagePath);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));// 分享的内容
            intent.setType("image/*");// 分享发送的数据类型
            Intent chooser = Intent.createChooser(intent, "Share screen shot");
            if(intent.resolveActivity(context.getPackageManager()) != null){
                context.startActivity(chooser);
            }
        } else {

            Toast.makeText(context,"先截屏，再分享",Toast.LENGTH_SHORT).show();
        }
    }
}
