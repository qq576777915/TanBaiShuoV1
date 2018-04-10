package com.king.tanbaishuo.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by KingLee on 2018/4/10.
 */

public class SetImageViewUtil {
    public static void setImageToImageView(final ImageView imageView, final String imgURL) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.e("类SetImageViewUtil下", "设置图片成功");
                super.handleMessage(msg);
                Bitmap bitmap = (Bitmap) msg.obj;
                if (imageView.getWidth() == 0 || imageView.getHeight() == 0) {
                } else {
                    Bitmap bitmap1 = zoomBitmap(bitmap, imageView.getWidth(), imageView.getHeight());
                    imageView.setImageBitmap(bitmap1);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitMapUtil.getBitmao(imgURL);
                Message msg = new Message();
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }
}
