package com.android.ndk;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity implements View.OnClickListener {
    static Bitmap shareBitmap;
    Button share,exit;
    LinearLayout shareLayout;
    final int shareintent=1001;
    static String shareFile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        share=(Button)findViewById(R.id.share);
        exit=(Button)findViewById(R.id.exit);
        shareLayout=(LinearLayout)findViewById(R.id.shareLayout);
        share.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share:
                capture();
                break;
            case R.id.exit:
                finish();
                break;
            default:
                break;
        }
    }

    public void capture(){

        Bitmap overlay=Bitmap.createBitmap(shareBitmap.getWidth(),shareBitmap.getHeight(),shareBitmap.getConfig());
        Canvas canvas=new Canvas(overlay);
        canvas.drawBitmap(shareBitmap, 0,0, null);
        shareLayout.buildDrawingCache();
        Bitmap bm=shareLayout.getDrawingCache();
        canvas.drawBitmap(bm,0,0,null);
        FileOutputStream out;


        String filename = "/" + System.currentTimeMillis() + ".jpg";
        filename = Environment.getExternalStorageDirectory().toString() + filename;
        if(filename==null){
            Log.e("filename","null");
        }else{
            Log.e("filename",filename);
        }
        try{
            out=new FileOutputStream(filename);
            overlay.compress(Bitmap.CompressFormat.JPEG,100, out);
            Toast.makeText(getApplicationContext(), filename+"에 저장되었습니다", Toast.LENGTH_SHORT).show();
            sharintent(filename);
        }catch(Exception e){
            Log.e("screenshot", e.toString());
            e.printStackTrace();

        }

    }

    /*
     *공유 인텐트 부분
     * */
    protected void sharintent(String filename){

        File media=new File(filename);
        Uri uri=Uri.fromFile(media);
        PackageManager pm = getPackageManager();    //패키지 매니저
        Intent shareIntent = new Intent();    //공유할 컨텐츠에 대한 정보
        shareIntent.setAction(Intent.ACTION_SEND);//
        shareIntent.setType("image/*");//
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));//
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name));//
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filename));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);//
        Intent openInChooser = Intent.createChooser(shareIntent, getResources().getText(R.string.share));//앱선택하고 보낼 인텐트
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(shareIntent, 0); //켄텐츠 정보로 필터링 된 앱리스트
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>(); //필터링해서 컨텐츠보낼 앱리스트
        int sharecnt = 0;
        if (!resInfo.isEmpty()) {    //보낼수 있는 컨텐츠이면
            for (int i = 0; i < resInfo.size(); i++) {    //컨텐츠들 for문으로 검색
                ResolveInfo temp = resInfo.get(i);    //공유가능한 앱
                String tempApp = temp.activityInfo.packageName;    //공유 가능한 앱의 패키지이름
                if (tempApp.contains("kakao") || tempApp.contains("naver.line.android")
                        || tempApp.contains("nhn.android.blog") || tempApp.contains("nhn.android.navercafe")
                        || tempApp.contains("com.twitter.android")||tempApp.contains("com.facebook.katana")
                        || tempApp.contains("com.instagram.android")) {    //앱 필터링
                    sharecnt++;
                    shareIntent.setComponent(new ComponentName(tempApp, temp.activityInfo.name));//공유할 앱들정보 넣기
                    intentList.add(new LabeledIntent(shareIntent, tempApp, temp.loadLabel(pm), temp.icon));//앱리스트에 앱 추가
                }
            }
            if (sharecnt > 0) {
                LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);//인텐트 리스트에 앱리스트 추가
                openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);//인텐트리스트를 공유 앱선택인텐트에 넣기
                shareFile=filename;
                startActivityForResult(openInChooser, shareintent); //인텐트 시작
            } else {
                Toast.makeText(this, getString(R.string.dontshare), Toast.LENGTH_SHORT).show();
            }

        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){

            case shareintent:
                File file;
                file=new File(shareFile);
                if(file.exists()){
                    Log.e("file","file exist");
                    file.delete();
                }else{
                    Log.e("file","file not exist");
                }

                break;
            default:
                break;

        }


    }

}
