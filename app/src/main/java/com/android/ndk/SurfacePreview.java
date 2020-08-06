package com.android.ndk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;

/**
 * 서피스뷰를 상속받은 클래스
 * 이곳에서 카메라 프리뷰를~
 * @author Ans
 *
 */
public class SurfacePreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback{

	SurfaceHolder holder;	//서피스홀더
	Camera cam=null;		//카메라


	public SurfacePreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public SurfacePreview(Context context) {
		super(context);
		init();
	}
	public void init(){

		holder=getHolder();
		holder.addCallback(this);

	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		cam.setDisplayOrientation(90);		//카메라 각도를 포트레이트로(90도)
		cam.startPreview();					//프리뷰 시작

	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		cam=Camera.open();					//카메라 객체를 오픈(퍼미션 되어있어야 됨)

		try{
			cam.setPreviewDisplay(holder);	//프리뷰를 홀더로
			cam.setPreviewCallback(this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		cam.setPreviewCallback(null);
		cam.stopPreview();
		cam.release();						//카메라 죽이기
		cam=null;

	}


	@Override
	public void onPreviewFrame(final byte[] data, Camera camera) {

		Camera.Parameters params = camera.getParameters();
		int w = params.getPreviewSize().width;
		int h = params.getPreviewSize().height;
		int format = params.getPreviewFormat();
		YuvImage image = new YuvImage(data, format, w, h, null);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Rect area = new Rect(0, 0, w, h);
		image.compressToJpeg(area, 100, out);
		Bitmap bm = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());

		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0,w, h, matrix, true);
		MainActivity.shareBitmap=rotatedBitmap;

	}
}

