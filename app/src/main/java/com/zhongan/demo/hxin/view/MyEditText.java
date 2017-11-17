package com.zhongan.demo.hxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {

	private OnImputCompleteListener mOnImputCompleteListener;

	public MyEditText(Context context) {
		super(context);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressLint("NewApi")
	public MyEditText(Context context, AttributeSet attrs, int defStyleAttr,
                      int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);

		if (!focused) {
			mOnImputCompleteListener.onImputComplete();
		}
	}

	public void setOnImputCompleteListener(OnImputCompleteListener l) {
		this.mOnImputCompleteListener = l;
	}
}

interface OnImputCompleteListener {
	void onImputComplete();
}
