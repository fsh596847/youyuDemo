package com.zhongan.demo.hxin.util;

import android.view.Gravity;

import com.zhongan.demo.R;


public class PickerViewAnimateUtil
{
	private static final int INVALID = -1;

	/**
	 * Get default animation resource when not defined by the user
	 * 
	 * @param gravity
	 *            the gravity of the dialog
	 * @param isInAnimation
	 *            determine if is in or out animation. true when is is
	 * @return the id of the animation resource
	 */
	public static int getAnimationResource(int gravity, boolean isInAnimation)
	{
		switch (gravity)
		{
		case Gravity.BOTTOM:
      return isInAnimation ? R.anim.rskj_slide_in_bottom : R.anim.rskj_slide_out_bottom;
    }
		return INVALID;
	}
}
