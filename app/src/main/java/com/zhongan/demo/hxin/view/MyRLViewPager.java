package com.zhongan.demo.hxin.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class MyRLViewPager extends ViewPager {
	public MyRLViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	private boolean scrollble=true;
    public MyRLViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
