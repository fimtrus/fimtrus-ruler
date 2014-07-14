package com.fimtrus.rule;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {

	private AdView mAdMobView;
	private boolean flag = false;
	private Handler mBackHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// setContentView(new RuleSurfaceView(this));
		getActionBar().hide();
		initialize();
		
	}
	
	// Init
	protected void initialize() {

		initializeFields();
		initializeListeners();
		initializeView();
	}

	protected void initializeFields() {
		
		mAdMobView = (AdView) findViewById(R.id.adView);
		
		mBackHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					flag = false;
				}
			}
		};
	}

	protected void initializeListeners() {
	}

	protected void initializeView() {
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdMobView.loadAd(adRequest);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (mAdMobView != null) {
			mAdMobView.pause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (mAdMobView != null) {
			mAdMobView.resume();
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// Main 화면에서만 종료가 가능하도록...
		if (!flag) {
			Toast.makeText(this, R.string.exit_text, Toast.LENGTH_SHORT).show();
			flag = true;
			mBackHandler.sendEmptyMessageDelayed(0, 3000);
		} else {
			super.onBackPressed();
		}
	}
}
