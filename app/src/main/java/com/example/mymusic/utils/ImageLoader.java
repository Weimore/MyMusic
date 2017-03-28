package com.example.mymusic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mymusic.model.Song;

/**
 * Created by wn123 on 2017/3/24.
 */

public class ImageLoader {

    public Bitmap miniBitmap(String imgPath){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=3;  //缩放为三分之一
        Bitmap bitmap=BitmapFactory.decodeFile(imgPath, options);
        return bitmap;
    }
}
