package com.king.tanbaishuo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by KingLee on 2018/4/10.
 */

public class BitMapUtil {
    public static Bitmap getBitmao(String url){
        URL imageURL = null;
        Bitmap bitmap = null;
        Log.e("类BitMapUtil","URL"+url);
        try{
            imageURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageURL.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
