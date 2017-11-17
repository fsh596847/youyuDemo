package com.zhongan.demo.hxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;


import com.zhongan.demo.R;

import java.lang.reflect.Field;


@SuppressLint("NewApi")
public class NumberPickerView extends NumberPicker {

	public NumberPickerView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public NumberPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NumberPickerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addView(View child) {
		this.addView(child, null);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		this.addView(child, -1, params);
	}

	@Override
	public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		setNumberPicker(child);
	}

	/**
	 * 设置NumberPickerView的属性 颜色 大小
	 * 
	 * @param view
	 */
	public void setNumberPicker(View view) {
		if (view instanceof EditText) {
			((EditText) view).setTextColor(0xff333333);
			((EditText) view).setTextSize(16);
		}
	}

	/**
	 * 设置分割线的颜色值
	 * 
	 * @param numberPicker
	 */
	public void setNumberPickerDividerColor(NumberPicker numberPicker) {
		NumberPicker picker = numberPicker;
		Field[] pickerFields = NumberPicker.class.getDeclaredFields();
		for (Field pf : pickerFields) {
			if (pf.getName().equals("mSelectionDivider")) {
				pf.setAccessible(true);
				try {
					pf.set(picker,new ColorDrawable(this.getResources().getColor(R.color.color_e5e5e5)));
				} catch (Exception e) {
				}
				break;
			}
		}
	}

}
