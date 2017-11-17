package consumer.fin.rskj.com.library.okhttp.callback;

import java.io.IOException;

import consumer.fin.rskj.com.library.okhttp.HttpInfo;


/**
 * 异步请求回调接口
 */
public interface Callback extends BaseCallback{

    /**
     * 请求成功：该回调方法已切换到UI线程
     */
    void onSuccess(HttpInfo info) throws IOException;

    /**
     * 请求失败：该回调方法已切换到UI线程
     */
    void onFailure(HttpInfo info) throws IOException;

}
