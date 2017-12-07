package com.zhongan.demo.impl;

/**
 * Created by gxj on 2016/8/26.
 */
public interface ReqCallBack<T> {

  /**
   * 响应成功
   */
  void onReqSuccess(T result);

  /**
   * 响应失败
   */
  void onReqFailed(String errorMsg);
}
