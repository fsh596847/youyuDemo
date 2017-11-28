package com.zhongan.demo.http;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhongan.demo.BuildConfig;
import com.zhongan.demo.LoginActivity;
import com.zhongan.demo.contant.HttpContent;
import consumer.fin.rskj.com.library.login.ReqCallBack;
import com.zhongan.demo.impl.UploadProgressCallBack;
import com.zhongan.demo.module.CommonResponse;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.SharedPreferenceUtils;
import com.zhongan.demo.util.SystemTool;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


/**
 * Created by gxj on 2016/9/26.
 */
public class OkHttpRequestManager {

    //mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_JSON =
            MediaType.parse("application/json; charset=utf-8");

    private static final String TAG = OkHttpRequestManager.class.getSimpleName();

    //mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN =
            MediaType.parse("text/x-markdown; charset=utf-8");

    //mdiatype 这个需要和服务端保持一致 你需要看下你们服务器设置的ContentType
    private static final MediaType MEDIA_OBJECT_STREAM =
            MediaType.parse("application/octet-stream");

    private static volatile OkHttpRequestManager mInstance;//单利引用
    public static final int TYPE_RESTFUL_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    public static final int TYPE_POST_FORM = 2;//post请求参数为表单
    public static final int TYPE_OTHER = 3;//外来url

    public static final int TYPE_PUT_JSON = 4;//put请求参数为json


    public static final int TYPE_PUT = 5;// PUT请求
    public static final int TYPE_DELETE = 6;// DELETE请求

    public static final int TYPE_COMMON_GET = 7;// 普通get请求


    public OkHttpClient mOkHttpClient;//okHttpClient 实例
    private Handler okHttpHandler;//全局处理子线程和M主线程通信

    SharedPreferenceUtils preferUtils;
    Context context;

    private Gson gson;
    private CommonResponse requestResult;
    Intent intent;

    public static final String URL_UploadImage =
            "http://www.xmqq99.com:8888/private_upload/";//上传路径

    /**
     * 初始化RequestManager
     */
    private OkHttpRequestManager(Context context) {
        //初始化Handler
        okHttpHandler = new Handler(context.getMainLooper());
        preferUtils = new SharedPreferenceUtils(context);
        this.context = context;
        gson = new GsonBuilder().create();

        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                /*.cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        Log.e(TAG, "response --saveFromResponse--->" );
                        cookieStore.put(httpUrl, list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        Log.e(TAG, "response --loadForRequest--->" );
                        List<Cookie> cookies = cookieStore.get(httpUrl);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })*/.connectTimeout(30, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//设置写入超时时间
                .build();

    }


    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }


    public class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

    /**
     * 获取单例引用
     *  OkHttpRequestManager
     * @return
     */
    public static OkHttpRequestManager getInstance(Context context) {
        OkHttpRequestManager inst = mInstance;
        if (inst == null) {
            synchronized (OkHttpRequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkHttpRequestManager(context.getApplicationContext());
                    mInstance = inst;
                }
            }
        }
        return inst;
    }


    /**
     * okHttp异步请求统一入口
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     **/
    public <T> Call requestAsyn(String actionUrl, int requestType,
                                HashMap<String, String> paramsMap,
                                ReqCallBack<T> callBack) {
        Call call = null;
        switch (requestType) {
            case TYPE_COMMON_GET:
                requestGetSync(actionUrl, paramsMap, callBack);
                break;

            case TYPE_RESTFUL_GET:
                call = requestRestFulGet(actionUrl, paramsMap, callBack);
                break;

            case TYPE_PUT:
                call = requestRestFulPut(actionUrl, paramsMap, callBack);
                break;

            case TYPE_DELETE:
                call = requestRestFulDelete(actionUrl, paramsMap, callBack);
                break;

            case TYPE_POST_JSON:
                call = requestPostJson(actionUrl, paramsMap, callBack);
                break;

            case TYPE_PUT_JSON:
                call = requestPutJson(actionUrl, paramsMap, callBack);
                break;

            case TYPE_POST_FORM:
                call = requestPostByAsynWithForm(actionUrl, paramsMap, callBack);
                break;
            case TYPE_OTHER:
                call = urlPostWithForm(actionUrl, paramsMap, callBack);
                break;

        }
        return call;
    }

    /**
     * okHttp get异步请求
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     * @return
     */
    private <T> Call requestGetSync(String actionUrl,
                                       HashMap<String, String> paramsMap,
                                       final ReqCallBack<T> callBack) {
        StringBuilder tempParams = new StringBuilder();
        try {
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key,
                        URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String requestUrl = String.format("%s?%s", actionUrl,
                    tempParams.toString());
            LogUtils.Log(TAG,"requestUrl = " + requestUrl);


            LogUtils.Log(TAG,"requestUrl = " + requestUrl);

            final Request request = addHeaders().url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();

                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * okHttp get异步请求
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     * @return
     */
    private <T> Call requestRestFulGet(String actionUrl,
                                       HashMap<String, String> paramsMap,
                                       final ReqCallBack<T> callBack) {
        try {
            String requestUrl = String.format("%s/%s", HttpContent.BASE_URL, actionUrl);
            LogUtils.Log(TAG,"requestUrl = " + requestUrl);

            final Request request = addHeaders().url(requestUrl).build();

            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, e.toString());
                    failedCallBack("网络异常:" + e.getMessage(), callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();

                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * put
     */
    private <T> Call requestRestFulPut(String actionUrl,
                                       HashMap<String, String> paramsMap,
                                       final ReqCallBack<T> callBack) {
        try {
            JSONObject jsonObj = new JSONObject();

            Iterator<Map.Entry<String, String>> iter = paramsMap.entrySet().iterator();
            while (iter.hasNext()) {
              HashMap.Entry<String, String> entry = iter.next();

                jsonObj.put(entry.getKey(), entry.getValue());
            }


//            LogUtils.Log(TAG,"log = " + jsonObj.toString());

            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, jsonObj.toString());

            String requestUrl = String.format("%s/%s", HttpContent.BASE_URL, actionUrl);
            LogUtils.Log(TAG,"requestUrl = " + requestUrl);


            final Request request = addHeaders().url(requestUrl).put(body).build();

            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        String string = response.body().string();

                        try {
                            requestResult = gson.fromJson(string,CommonResponse.class);
                            if("unlogin".equals(requestResult.getCode() )){
                                preferUtils.setLogin(false);
                                intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                context.startActivity(intent);
                            }
                        }catch (Exception e){
                            LogUtils.Log(TAG,"Exception = " +e.getMessage());
                        }



                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }



    /**
     * delete
     */
    private <T> Call requestRestFulDelete(String actionUrl,
                                       HashMap<String, String> paramsMap,
                                       final ReqCallBack<T> callBack) {
        StringBuilder tempParams = new StringBuilder();
        try {
            String requestUrl = String.format("%s/%s", HttpContent.BASE_URL, actionUrl);
            LogUtils.Log(TAG,"requestUrl = " + requestUrl);


            final Request request = addHeaders().url(requestUrl).delete().build();

            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();

                        try {
                            requestResult = gson.fromJson(string,CommonResponse.class);
                            if("unlogin".equals(requestResult.getCode() )){
                                preferUtils.setLogin(false);
                                intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                context.startActivity(intent);
                            }
                        }catch (Exception e){
                            LogUtils.Log(TAG,"Exception = " +e.getMessage());
                        }

                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * okHttp post异步请求
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     * @return
     */
    private <T> Call requestPostJson(String actionUrl,
                                     HashMap<String, String> paramsMap,
                                     final ReqCallBack<T> callBack) {
        try {
            JSONObject jsonObj = new JSONObject();

            Iterator<Map.Entry<String, String>> iter = paramsMap.entrySet().iterator();
            while (iter.hasNext()) {
              HashMap.Entry<String, String> entry = iter.next();

                jsonObj.put(entry.getKey(), entry.getValue());
            }

            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, jsonObj.toString());
            String requestUrl = String.format("%s/%s", HttpContent.BASE_URL, actionUrl);

            LogUtils.Log(TAG,"------- requestUrl = " + requestUrl);

            final Request request = addHeaders().url(requestUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("网络异常:"+e.getMessage(), callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {

                    if (response.isSuccessful()) {
                        String string = response.body().string();

                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        if(SystemTool.isNetworkAvailable(context)){
                            failedCallBack("服务器正在升级...", callBack);
                        }else {
                            failedCallBack("数据请求失败", callBack);
                        }
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * okHttp post异步请求
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     * @return
     */
    private <T> Call requestPutJson(String actionUrl,
                                    HashMap<String, String> paramsMap,
                                    final ReqCallBack<T> callBack) {
        try {
            JSONObject jsonObj = new JSONObject();

            Iterator<Map.Entry<String, String>> iter = paramsMap.entrySet().iterator();
            while (iter.hasNext()) {
              HashMap.Entry<String, String> entry = iter.next();

                jsonObj.put(entry.getKey(), entry.getValue());
            }


            LogUtils.Log(TAG,"log = " + jsonObj.toString());
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, jsonObj.toString());
            String requestUrl = String.format("%s/%s", HttpContent.BASE_URL, actionUrl);

            LogUtils.Log(TAG,"requestUrl = " + requestUrl);

            final Request request = addHeaders().url(requestUrl).put(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, "onFailure = " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {

                    if (response.isSuccessful()) {
                        String string = response.body().string();

                        try {
                            requestResult = gson.fromJson(string,CommonResponse.class);
                            if("unlogin".equals(requestResult.getCode() )){
                                preferUtils.setLogin(false);
                                intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                context.startActivity(intent);
                            }
                        }catch (Exception e){
                            LogUtils.Log(TAG,"Exception = " +e.getMessage());
                        }

                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, "Exception = " +  e.toString());
        }
        return null;
    }


    /**
     * okHttp post异步请求表单提交
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     * @return
     */
    private <T> Call requestPostByAsynWithForm(String actionUrl,
                                               HashMap<String, String> paramsMap,
                                               final ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();
            String requestUrl = String.format("%s/%s", HttpContent.BASE_URL, actionUrl);

            LogUtils.Log(TAG,"requestUrl = " + requestUrl);

            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {
                    if (response.isSuccessful()) {
                        //String cookie  = response.header("Set-Cookie","");


                        String string = response.body().string();
                        try {
                            requestResult = gson.fromJson(string,CommonResponse.class);
                            try {
                                requestResult = gson.fromJson(string,CommonResponse.class);
                                if("unlogin".equals(requestResult.getCode() )){
                                    preferUtils.setLogin(false);
                                    intent = new Intent(context, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                    context.startActivity(intent);
                                }
                            }catch (Exception e){
                                LogUtils.Log(TAG,"Exception = " +e.getMessage());
                            }

                        }catch (Exception e){
                            LogUtils.Log(TAG,"Exception = " +e.getMessage());
                        }

                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack(response.body().string(), callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            failedCallBack(e.getMessage(), callBack);
            Log.e(TAG, e.toString());
        }
        return null;
    }


    private <T> Call urlGetWithForm(String url,
                                     HashMap<String, String> paramsMap,
                                     final ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();

            LogUtils.Log(TAG,"requestUrl = " + url);

            final Request request = addHeaders().url(url).build();

            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();

                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    private <T> Call urlPostWithForm(String url,
                                     HashMap<String, String> paramsMap,
                                     final ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();

            LogUtils.Log(TAG,"requestUrl = " + url);

            final Request request = addHeaders().url(url).post(formBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();

                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * 统一为请求添加头信息
     * @return
     */
    private Request.Builder addHeaders() {
        LogUtils.Log("getToken",preferUtils.getToken());
        LogUtils.Log(TAG,"version =" + SystemTool.getVersionName(context));

        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("channel", "android")
                .addHeader("phoneType", "phoneType")
                .addHeader("deviceCode", "deviceCode")
                .addHeader("deviceType", "android")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("X-Requested-With","XMLHttpRequest")
                .addHeader("token",preferUtils.getToken())
                .addHeader("Content-Type", "application/json")
                .addHeader("version", /*SystemTool.getVersionName(context)*/BuildConfig.VERSION_NAME);
        return builder;
    }



    /**
     * 创建带进度的RequestBody
     * @param contentType MediaType
     * @param file  准备上传的文件
     * @param callBack 回调
     * @param <T>
     * @return
     */
    public <T> RequestBody createProgressRequestBody(final MediaType contentType,
                                                     final File file,
                                                     final UploadProgressCallBack<T> callBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        Log.e(TAG, "current------>" + current);
                        progressCallBack(remaining, current, callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    /**
     * 统一处理进度信息
     * @param total    总计大小
     * @param current  当前进度
     * @param callBack
     * @param <T>
     */
    private <T> void progressCallBack(final long total, final long current,
                                      final UploadProgressCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onProgress(total, current);
                }
            }
        });
    }

    /**
     * 统一同意处理成功信息
     * @param result
     * @param callBack
     * @param <T>
     */
    private <T> void successCallBack(final T result,
                                     final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqSuccess(result);
                }
            }
        });
    }


    /**
     * 统一处理失败信息
     * @param errorMsg
     * @param callBack
     * @param <T>
     */
    private <T> void failedCallBack(final String errorMsg,
                                    final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqFailed(errorMsg);
                }
            }
        });
    }


}
