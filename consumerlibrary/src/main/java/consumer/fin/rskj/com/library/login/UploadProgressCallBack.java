package consumer.fin.rskj.com.library.login;

/**
 * Created by gxj on 2016/11/14.
 */
public interface UploadProgressCallBack<T> extends ReqCallBack<T> {
  /**
   * 响应进度更新
   */
  void onProgress(long total, long current);
}