package com.example.circle;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class CircleWidget extends View {

	private final int END_ANGLE = 360;
	private final int START_ANGLE = 0;
	private final int LOADING_START_ANGLE = -90;

	private int BORDER_STROKE_WIDTH = 5;

	private Paint mLoadingPaint, mWhitePaint;
	private Bitmap mPlayBitmap, mPauseBitmap;

	private int mLoadingColor;
	private int mAudioDuaration;

	private RectF mImageBoundingRect, mBoarderRect;
	private float x1, x2, y1, y2;
	private float angle = 0, pausedAngle;

	private boolean mPlayState = false;

	private onCircleWidgetListener mListener;

	// private GestureFilter mFilter;

	public interface onCircleWidgetListener {

		public void onAudioPaused();

		public void onAudioPlay();
	}

	public CircleWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// mFilter = new GestureFilter((Activity) context);
		initAttributeset(context, attrs);
		init();
	}

	public CircleWidget(Context context) {
		super(context);
		// mFilter = new GestureFilter((Activity) context);
		mLoadingColor = Color.BLUE;
		init();
	}

	public CircleWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// mFilter = new GestureFilter((Activity) context);
		initAttributeset(context, attrs);
		init();
	}

	public void setCircleWidgetListener(onCircleWidgetListener listener) {
		mListener = listener;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mPlayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_img_play_btn);
		mPauseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_img_pause_btn);

		x1 = getWidth() / 2f - mPlayBitmap.getWidth() / 2;
		x2 = getWidth() / 2f + mPlayBitmap.getWidth() / 2;
		y2 = getHeight() / 2f + mPlayBitmap.getHeight() / 2;
		y1 = getHeight() / 2f - mPlayBitmap.getHeight() / 2;

		mImageBoundingRect = new RectF(x1, y1, x2, y2);

		BORDER_STROKE_WIDTH = (int) (getResources().getDisplayMetrics().density * BORDER_STROKE_WIDTH);

		mBoarderRect = new RectF(x1 - BORDER_STROKE_WIDTH, y1 - BORDER_STROKE_WIDTH, x2 + BORDER_STROKE_WIDTH, y2
				+ BORDER_STROKE_WIDTH);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);

		setMeasuredDimension(chosenWidth, chosenHeight);
	}

	private int getPreferredSize() {
		return 300;
	}

	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { // (mode == MeasureSpec.UNSPECIFIED)
			return getPreferredSize();
		}
	}

	private void initAttributeset(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Circle, 0, 0);
		mLoadingColor = a.getColor(R.styleable.Circle_loadingColor, android.R.color.holo_blue_light);
		a.recycle();
	}

	private void init() {

		mLoadingPaint = new Paint();
		mWhitePaint = new Paint();
		mWhitePaint.setColor(Color.WHITE);
		mLoadingPaint.setColor(mLoadingColor);
		mLoadingPaint.setAntiAlias(true);
		mWhitePaint.setAntiAlias(true);

		/*
		 * setOnClickListener(this); setOnTouchListener(this);
		 */
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		canvas.drawArc(mBoarderRect, START_ANGLE, END_ANGLE, true, mWhitePaint);
		// canvas.drawArc(mImageBoundingRect, START_ANGLE, END_ANGLE, true,
		// mWhitePaint);
		canvas.drawArc(mImageBoundingRect, LOADING_START_ANGLE, angle, true, mLoadingPaint);
		// canvas.drawBitmap(mPauseBitmap, x1, y1, null);

	}

	/*
	 * public void setState(boolean state) { mPlayState = state; pausedAngle =
	 * 0; angle = 0;
	 * 
	 * Log.d("ANGLE", "setState PAUSED:" + pausedAngle);
	 * 
	 * invalidate(); }
	 */

	// To set the audio duration and it is in milliseconds
	public void setAudioDuration(int duration) {
		mAudioDuaration = duration;
	}

	// Getting a call to this method is nothing but view is in play state
	public void drawArc(int miliseconds) {
		if (mAudioDuaration != 0) {
			this.angle = (float) ((360.0 / mAudioDuaration) * miliseconds);
			if (this.angle <= 360) {
				Log.d("onDraw", "onDraw angle" + angle);

				invalidate();
			} else {
				completed();
				this.setVisibility(View.GONE);
			}
		}
	}

	public void completed() {
		// if (mPlayState) {
		// mPlayState = false;
		// pausedAngle = 0;
		// angle = 0;
		// invalidate();
		// }
	}
	//
	// public void play() {
	// mPlayState = true;
	// }
	//
	// public void pause() {
	// mPlayState = true;
	// }
	//
	// public boolean isPlaying() {
	// return mPlayState;
	// }
	//
	// public void onClick(View v) {
	// }

	/*
	 * private boolean updateViewState(MotionEvent event) { float touchX =
	 * event.getX(); float touchY = event.getY(); // if the touch lies inside
	 * the rect then only changes the state
	 * 
	 * RectF rec = new RectF(mImageBoundingRect.left - 30,
	 * mImageBoundingRect.top - 30, mImageBoundingRect.right + 30,
	 * mImageBoundingRect.bottom + 30); if (rec.contains(touchX, touchY)) { //
	 * if (touchX > x1 && touchX < x2 && touchY > y1 && touchY < y2) {
	 * mPlayState = !mPlayState; pausedAngle = angle;
	 * 
	 * Log.d("ANGLE", "PAUSED:" + pausedAngle);
	 * 
	 * if (mListener != null) { if (mPlayState) { mListener.onAudioPlay(); }
	 * else { mListener.onAudioPaused(); } } invalidate(); return true; } return
	 * false; }
	 * 
	 * public boolean onTouch(View v, MotionEvent event) { switch
	 * (event.getAction()) { case MotionEvent.ACTION_DOWN: boolean status =
	 * updateViewState(event); Log.d("CIRCLE", "STATUS:" + status); if (status)
	 * return status; break; default: break; } return false; }
	 */

}
