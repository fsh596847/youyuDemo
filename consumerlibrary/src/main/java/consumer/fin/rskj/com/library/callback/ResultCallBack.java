package consumer.fin.rskj.com.library.callback;

/**
 * 数据回调接口
 */

public interface ResultCallBack {
    //成功回调
    void onSuccess(String data);
    //失败回调
    void onFailure(String errorMsg);
    //错误回调
    void onError(String retrunCode, String errorMsg);
}
