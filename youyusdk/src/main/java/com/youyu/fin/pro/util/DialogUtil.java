package com.youyu.fin.pro.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogUtil {

	private Activity activity;

	public DialogUtil(Activity activity) {
		this.activity = activity;
	}

	public void showDialog(String message) {
		if(null == activity){
			return;
		}
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
				.setTitle(message)
				.setNegativeButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
					}
				}).setCancelable(false).create();
		alertDialog.show();
	}

	public void onDestory() {
		activity = null;
	}
}