package consumer.fin.rskj.com.library.okhttp.callback;

import java.io.IOException;

import consumer.fin.rskj.com.library.okhttp.HttpInfo;


/**
 * 异步请求回调接口
 */
public interface CallbackOk  extends BaseCallback{
    /**
     * 该回调方法已切换到UI线程
     */
    void onResponse(HttpInfo info) throws IOException;
}
