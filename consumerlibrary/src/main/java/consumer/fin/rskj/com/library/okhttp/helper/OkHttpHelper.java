package consumer.fin.rskj.com.library.okhttp.helper;

import java.util.ArrayList;
import java.util.List;

import consumer.fin.rskj.com.library.okhttp.HttpInfo;
import consumer.fin.rskj.com.library.okhttp.annotation.BusinessType;
import consumer.fin.rskj.com.library.okhttp.annotation.Encoding;
import consumer.fin.rskj.com.library.okhttp.annotation.RequestMethod;
import consumer.fin.rskj.com.library.okhttp.bean.DownloadFileInfo;
import consumer.fin.rskj.com.library.okhttp.bean.UploadFileInfo;
import consumer.fin.rskj.com.library.okhttp.callback.BaseCallback;
import consumer.fin.rskj.com.library.okhttp.callback.ProgressCallback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * OkHttpUtil业务类：负责处理所有业务
 * @author zhousf
 */
public class OkHttpHelper {

    //** 请求参数定义**/
    private HttpInfo httpInfo;

    //** 系统辅助参数定义**/
    private HttpHelper httpHelper;
    private DownUpLoadHelper downUpLoadHelper;
    private DownloadFileInfo downloadFileInfo;
    private List<UploadFileInfo> uploadFileInfoList = new ArrayList<>();
    private OkHttpClient.Builder clientBuilder;
    private @RequestMethod
    int requestMethod;
    private BaseCallback callback;
    private ProgressCallback progressCallback;
    private Request request;
    private OkHttpClient httpClient;
    private @BusinessType
    int businessType;//业务类型
    private @Encoding
    String responseEncoding;//服务器响应编码


    private OkHttpHelper(Builder builder) {
        httpInfo = builder.httpInfo;
        downloadFileInfo = builder.downloadFileInfo;
        uploadFileInfoList = builder.uploadFileInfoList;
        clientBuilder = builder.clientBuilder;
        requestMethod = builder.requestMethod;
        callback = builder.callback;
        progressCallback = builder.progressCallback;
        businessType = builder.businessType;
        responseEncoding = builder.helperInfo.getResponseEncoding();
        builder.helperInfo.setHttpInfo(httpInfo);
        httpHelper = new HttpHelper(builder.helperInfo);
        if(null != downloadFileInfo || !uploadFileInfoList.isEmpty())
            downUpLoadHelper = new DownUpLoadHelper(builder.helperInfo);
    }

    public HttpInfo doRequestSync(){
        return httpHelper.doRequestSync(this);
    }

    public void doRequestAsync(){
        httpHelper.doRequestAsync(this);
    }

    public void downloadFile(){
        downUpLoadHelper.downloadFile(this);
    }

    public void uploadFile(){
        downUpLoadHelper.uploadFile(this);
    }


    public static Builder Builder(){
        return new Builder();
    }

    public static final class Builder {
        private HttpInfo httpInfo;
        private HelperInfo helperInfo;
        private DownloadFileInfo downloadFileInfo;
        private List<UploadFileInfo> uploadFileInfoList = new ArrayList<>();
        private OkHttpClient.Builder clientBuilder;
        private @RequestMethod  int requestMethod;
        private BaseCallback callback;
        private ProgressCallback progressCallback;
        private @BusinessType int businessType;

        public Builder() {
        }

        public OkHttpHelper build(){
            if(!this.uploadFileInfoList.isEmpty()){
                this.businessType = BusinessType.UploadFile;//文件上传
            } else if(downloadFileInfo != null){
                this.businessType = BusinessType.DownloadFile;//文件下载
            } else{
                this.businessType = BusinessType.HttpOrHttps;//http/https请求
            }
            return new OkHttpHelper(this);
        }

        public Builder httpInfo(HttpInfo httpInfo){
            this.httpInfo = httpInfo;
            return this;
        }

        public Builder helperInfo(HelperInfo helperInfo){
            this.helperInfo = helperInfo;
            return this;
        }

        public Builder downloadFileInfo(DownloadFileInfo downloadFileInfo){
            this.downloadFileInfo = downloadFileInfo;
            return this;
        }

        public Builder uploadFileInfo(UploadFileInfo uploadFileInfo){
            uploadFileInfoList.add(uploadFileInfo);
            return this;
        }

        public Builder uploadFileInfoList(List<UploadFileInfo> uploadFiles){
            uploadFileInfoList.addAll(uploadFiles);
            return this;
        }


        public Builder clientBuilder(OkHttpClient.Builder clientBuilder){
            this.clientBuilder = clientBuilder;
            return this;
        }

        public Builder requestMethod(@RequestMethod  int requestMethod){
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder callback(BaseCallback callback){
            this.callback = callback;
            return this;
        }

        public Builder progressCallback(ProgressCallback progressCallback){
            this.progressCallback = progressCallback;
            return this;
        }
    }

    HttpInfo getHttpInfo() {
        return httpInfo;
    }

    HttpHelper getHttpHelper() {
        return httpHelper;
    }

    DownUpLoadHelper getDownUpLoadHelper() {
        return downUpLoadHelper;
    }

    int getBusinessType() {
        return businessType;
    }

    String getResponseEncoding() {
        return responseEncoding;
    }

    DownloadFileInfo getDownloadFileInfo() {
        return downloadFileInfo;
    }

    List<UploadFileInfo> getUploadFileInfoList() {
        return uploadFileInfoList;
    }

    OkHttpClient.Builder getClientBuilder() {
        return clientBuilder;
    }

    @RequestMethod int getRequestMethod() {
        return requestMethod;
    }

    BaseCallback getCallback() {
        return callback;
    }

    ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    Request getRequest() {
        return request;
    }

    void setRequest(Request request) {
        this.request = request;
    }

    OkHttpClient getHttpClient() {
        return httpClient;
    }

    void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
