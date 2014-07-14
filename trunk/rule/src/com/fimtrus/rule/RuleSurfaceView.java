package com.fimtrus.rule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class RuleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private final int FONT_SIZE = 20;
	private final int START_POSITION = 20;
	private boolean isInitialized = false;
	
	//변추 초기화 멤버
	private final int INIT_INTEGER = 9999;
	

	public RuleThread mThread;
	// 터치 좌표점
	public int mTouchedX, mTouchedY, mTouched2X, mTouched2Y;
	
	
	// 선택된 아이템의 최초 좌표..
	public int mTempX = INIT_INTEGER, mTempY = INIT_INTEGER;

	//화면이 터치 및 멀티터치 유무.
	public boolean isTouched;
	private int mCanvasWidth;

	private int mCanvasHeight;

	private DisplayMetrics mMetrics;

	private double mHeightCm;

	private double mPixelPerCm;

	public RuleSurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	public RuleSurfaceView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    getHolder().addCallback(this);
	}

	public RuleSurfaceView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    getHolder().addCallback(this);
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

		// mCarList.add( CarModel.newInstance(
		// BitmapFactory.decodeResource(getResources(), R.drawable.botonpress) )
		// );

		mCanvasWidth = holder.getSurfaceFrame().width();
		mCanvasHeight = holder.getSurfaceFrame().height();
		
		mMetrics = getResources().getDisplayMetrics();
//		  totalDIP_X = metrics.xdpi;
//		  totalDIP_Y = metrics.ydpi;
		Log.d( "", "X : " + mMetrics.xdpi + " Y : " + mMetrics.ydpi );
		Log.d( "", "X : " + mMetrics.widthPixels + " Y : " + mMetrics.heightPixels );
		
		mHeightCm = mMetrics.heightPixels / mMetrics.ydpi * 2.54;
		mPixelPerCm = mMetrics.heightPixels / mHeightCm;
		
		Log.d( "", "높이 : " + mHeightCm + " 1cm 당 pixel : " + mPixelPerCm);
		
		mThread = new RuleThread(getHolder(), this);
		mThread.setRunning(true);
		mThread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.e("surfaceDestroyed", "surfaceDestroyed");

		boolean retry = true;
		mThread.setRunning(false);
		while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage( msg );
			Toast.makeText(getContext(), "testest : " + msg.what, Toast.LENGTH_SHORT).show();
		}
		
	};

	private Paint mCmPaint;

	private Paint mMmPaint;

	private Paint mTextPaint;
	
	
	
	@Override
	public void onDraw(Canvas canvas) {

		//최초 화면 그릴때 필요한 필드들을 초기화 한다. * 최초 한번만 불려짐.
		
		if ( !isInitialized ) {

			mCanvasWidth = canvas.getWidth();
			mCanvasHeight = canvas.getHeight();
			
			isInitialized = true;
			//#18 137 98
			mCmPaint = new Paint();
			mCmPaint.setColor(Color.rgb(43, 153, 166));
			mMmPaint = new Paint();
			mMmPaint.setColor(Color.BLUE);
			mTextPaint = new Paint();
			mTextPaint.setColor(Color.parseColor("#00ddff"));
			mTextPaint.setTextSize(FONT_SIZE * mMetrics.density );
		}
		
		 int cx = this.getMeasuredWidth() / 2;
	      int cy = this.getMeasuredHeight() / 2;
		
		//화면 그리는 부분
		if (canvas != null) {
//			canvas.drawColor(Color.GRAY);
			//AliceBlue - #F0F8FF 
			canvas.drawColor( Color.parseColor("#F0F8FF") );

			float tempCmY = 0.0f, tempMmY = 0.0f, tempTextY = 0.0f;
			
			//cm
			for ( int i = 0; tempCmY < mMetrics.heightPixels; i++ ) {
				tempCmY = (float) (( i ) * mPixelPerCm);
				canvas.drawLine( (float) (mMetrics.widthPixels - (mPixelPerCm / 2) ), tempCmY + START_POSITION, mMetrics.widthPixels, tempCmY + START_POSITION, mCmPaint);
//				canvas.drawLine( 0.0f, tempCmY, (float) mPixelPerCm, tempCmY, mCmPaint);
			}
			
			for ( int i = 0; tempMmY < mMetrics.heightPixels; i++ ) {

				if ( i % 10 != 0 ) { 
					tempMmY = (float) ( i * mPixelPerCm / 10);
					
//					canvas.drawLine( 0.0f, tempMmY, (float) mPixelPerCm / 5, tempMmY, mCmPaint);
					
					if ( i% 5 == 0 ) {
						canvas.drawLine( (float) (mMetrics.widthPixels - mPixelPerCm / 3), tempMmY + START_POSITION, mMetrics.widthPixels, tempMmY + START_POSITION, mCmPaint);

					} else {
						
						canvas.drawLine( (float) (mMetrics.widthPixels - mPixelPerCm / 5), tempMmY + START_POSITION, mMetrics.widthPixels, tempMmY + START_POSITION, mCmPaint);
					}
					
				}
				
			}
			canvas.save();
			canvas.rotate(90, mMetrics.widthPixels, 0);
			for ( int i = 0; tempTextY < mMetrics.heightPixels; i++ ) {
				tempTextY = (float) (( i ) * mPixelPerCm);
				
				canvas.drawText( i + "",  mMetrics.widthPixels + tempTextY + START_POSITION - ( FONT_SIZE * mMetrics.density / 3 ), (float) (mPixelPerCm / 2 + FONT_SIZE * mMetrics.density ), mTextPaint);
				
			}
			canvas.restore();
		}
	}
}