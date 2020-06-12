package com.example.ebook;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BtoDUtil {
    public Drawable BtoD(Bitmap bitmap){

        Drawable drawable =new BitmapDrawable(Resources.getSystem(),bitmap);
//        Bitmap bitmap1 = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
//                drawable.getIntrinsicHeight(),
//                drawable.getOpacity() !=
//                PixelFormat.OPAQUE?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap1);
////canvas.setBitmap(bitmap);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        drawable.draw(canvas);
return drawable;
    }
}
