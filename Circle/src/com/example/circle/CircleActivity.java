package com.example.circle;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class CircleActivity extends Activity {

	private CircleWidget mCircle;

	private int increment = 0;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			increment += 50;
			mCircle.drawArc(increment);
			mHandler.sendEmptyMessageDelayed(0, 1000);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCircle = new CircleWidget(getApplicationContext());
		mCircle.setAudioDuration(1000);
		setContentView(mCircle);
		
		mHandler.sendEmptyMessageDelayed(0, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.circle, menu);
		return true;
	}

}
