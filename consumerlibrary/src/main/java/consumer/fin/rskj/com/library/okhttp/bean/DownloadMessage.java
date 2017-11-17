package consumer.fin.rskj.com.library.okhttp.bean;


import consumer.fin.rskj.com.library.okhttp.HttpInfo;
import consumer.fin.rskj.com.library.okhttp.callback.ProgressCallback;

/**
 * 下载响应回调信息体
 */
public class DownloadMessage extends OkMessage{

    public String filePath;
    public HttpInfo info;
    public ProgressCallback progressCallback;

    public DownloadMessage(int what, String filePath, HttpInfo info, ProgressCallback progressCallback, String requestTag) {
        this.what = what;
        this.filePath = filePath;
        this.info = info;
        this.progressCallback = progressCallback;
        super.requestTag = requestTag;
    }
}