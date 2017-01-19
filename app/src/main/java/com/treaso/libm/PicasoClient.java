package com.treaso.libm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by Kapil Malviya on 7/24/2016.
 */
public class PicasoClient {
    public static Bitmap transformation(Bitmap source) {
        int size = Math.min(source.getWidth(),source.getHeight());
        int x = (source.getWidth()-size)/2;
        int y = (source.getHeight()-size)/2;
        Bitmap bitmap = Bitmap.createBitmap(source,x,y,size,size);
        if(bitmap!=source){
            source.recycle();

        }
        Bitmap nbitmap = Bitmap.createBitmap(size,size,source.getConfig());
        Canvas c = new Canvas(nbitmap);
        Paint p = new Paint();
        BitmapShader bs = new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        p.setShader(bs);
        p.setColor(Color.BLACK);
        p.setShadowLayer(4.0f,0.0f,2.0f, Color.GRAY);
        p.setAntiAlias(true);
        float r = size/2f;
        c.drawCircle(r,r,r,p);
        bitmap.recycle();
        return nbitmap;
    }
    public static void downLoadImg(Context c, String url, ImageView imageView){
        if(url!=null && url.length()>0){
            Picasso.with(c).load(url).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                  return transformation(source);
                }

                @Override
                public String key() {
                    return null;
                }
            }).placeholder(R.drawable.dp_default).into(imageView);

        }else{
            Picasso.with(c).load(R.drawable.dp_default).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    return transformation(source);
                }

                @Override
                public String key() {
                    return null;
                }
            }).into(imageView);
        }
    }
}
