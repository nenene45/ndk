package com.android.ndk;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private TextView txt_result;
    static {
        //System.loadLibrary("jniCalculator");
    }
    public native int getSum(int num1, int num2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_result = (TextView) findViewById(R.id.txt_result);

        int num1 = 10;
        int num2 = 20;

        //int sum = getSum(num1, num2);
        //txt_result.setText("JNI Sample :: 합계 : " + sum);

        File imgFile = new  File("/sdcard/test.jpg");

        if(imgFile.exists()){
            Log.e("이미지", "있음");
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageView);

            myImage.setImageBitmap(myBitmap);

        }else{
            Log.e("이미지", "ㄴㄴ");

        }

    }

}