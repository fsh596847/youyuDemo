package com.zhongan.demo.hxin.impl;

import com.lidroid.xutils.exception.HttpException;

public interface ResultCallBack {
	// 请求开始
	void onStart();

	// 请求数据失败
	void onFailure(HttpException exception, String msg);

	// 请求数据成功
	void onSuccess(String data);

	// 请求数据成功，但是resultCode != "000000"
	void onError(String returnCode, String msg);
	
}
