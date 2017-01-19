package com.treaso.libm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import static com.treaso.libm.R.id.imageView;

public class ProfileActivity extends AppCompatActivity {
    public Bitmap transform(Bitmap source) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView iv = (ImageView)findViewById(imageView);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.sherly);
        Bitmap bmcircle = transform(bm);
        iv.setImageBitmap(bmcircle);
    }
}
