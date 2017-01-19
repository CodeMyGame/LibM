package com.treaso.libm.tabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.treaso.libm.R;

import java.util.ArrayList;

import static com.treaso.libm.R.id.imageView;

public class tab1NextActivity extends AppCompatActivity {
    TextView name,author,publisher,price,copy,id;

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
        setContentView(R.layout.activity_tab1_next);
        name = (TextView)findViewById(R.id.textName);
        author = (TextView)findViewById(R.id.textAuthor);
        publisher = (TextView)findViewById(R.id.textPublisher);
        price = (TextView)findViewById(R.id.textPrice);
        copy = (TextView)findViewById(R.id.textCopy);
        id = (TextView)findViewById(R.id.textIssued);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<String> information = getIntent().getStringArrayListExtra("bookinformation");
        name.setText("Book name : - "+information.get(0));
        author.setText("Book author name : - "+information.get(1));
        publisher.setText("Book publisher name : - "+information.get(2));
        price.setText("Book price : - "+information.get(5));
        copy.setText("Book total copy : - "+information.get(3));
        id.setText("Book issued : - "+information.get(4));
        ImageView iv = (ImageView)findViewById(R.id.imageView2);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.sherly);
        Bitmap bmcircle = transform(bm);
        iv.setImageBitmap(bmcircle);

    }
}
