package com.zhongan.demo;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.SharedPreferenceUtils;
import com.zhongan.finance.common.FinanceInitor;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import consumer.fin.rskj.com.library.message.NetMessage;
import consumer.fin.rskj.com.library.okhttp.OkHttpUtil;
import consumer.fin.rskj.com.library.okhttp.annotation.CacheType;
import consumer.fin.rskj.com.library.okhttp.annotation.Encoding;
import consumer.fin.rskj.com.library.okhttp.cookie.PersistentCookieJar;
import consumer.fin.rskj.com.library.okhttp.cookie.cache.SetCookieCache;
import consumer.fin.rskj.com.library.okhttp.cookie.persistence.SharedPrefsCookiePersistor;
import consumer.fin.rskj.com.library.utils.HttpInterceptor;

/**
 * Created by dfqin on 2016/12/22.
 */

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public static MyApplication instance;
    public HttpUtils dataHttp;
    public static SharedPreferenceUtils preferUtils;
    protected ConnectionChangeReceiver myReceiver;
    private int timeOut = 300;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                handler.removeCallbacksAndMessages(null);
                if(timeOut <= 0){
                    getSP(getApplicationContext()).setLogin(false);
//                    Intent intent = new Intent(getApplicationContext(), MenuListActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                    handler.removeCallbacksAndMessages(null);
                    return;
                }else {
                    timeOut --;
                }
                //LogUtils.Log("MyApplication","timeOut == " + timeOut);
                handler.sendEmptyMessageDelayed(0,1000);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        //开启log日志
        initImageLoader(getApplicationContext());
        dataHttp = new HttpUtils("5*60 * 1000");

      //第三个参数0表示⽣产环境，1表示验收测试环境，2表示开发测试环境   众安初始化
        FinanceInitor.initFinance(this, BuildConfig.DEBUG, 0);
//        JSONObject json = new JSONObject();
//        try {
//            json.put("partnerNo", "8016112151261010");
//            json.put("channel", "XMQQ");
//            //TODO other data
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        FinanceInitor.setBusinessInfo(json.toString());

        initOkHttpUtil();

        registerReceiver();

        //initCallback();
        registerActivityLifecycleCallbacks(this);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        handler.removeCallbacksAndMessages(null);
        timeOut = 300;

    }

    @Override
    public void onActivityPaused(Activity activity) {
        if(getSP(getApplicationContext()).iSLogin()){
            handler.sendEmptyMessageDelayed(0,1000);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    //网络监听
    class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager=(ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                Toast.makeText(getApplicationContext(),"当前网络不可以用,请开启网络数据",Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new NetMessage(false));
            }else {
                //改变背景或者 处理网络的全局变量
                //ToastUtils.showCenterToast("网络正常",context);
                EventBus.getDefault().post(new NetMessage(true));
            }
        }
    }


    public static SharedPreferenceUtils getSP(Context context){

        if(null == preferUtils){

            preferUtils = new SharedPreferenceUtils( context);
        }

        return preferUtils;
    }


    // 初始化imageLaoder
    public static void initImageLoader(Context context) {
        // 缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,"RongShu/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))//建议内存设在5-10M,可以有比较好的表现
                .memoryCacheSize(5 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
                // (5
                // s),
                // readTimeout
                // (30
                // s)超时时间
                // .writeDebugLogs() // 显示log日志
                .build();
        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
    }


    public static DisplayImageOptions getOptions(){
        //自定义加载图片的配置信息
        DisplayImageOptions option = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher)// 设置图片加载或解码过程中发生错误显示的图片
                .resetViewBeforeLoading(false)// default 设置图片在加载前是否重置、复位
                //        .delayBeforeLoading(1000)// 下载前的延迟时间
                .cacheInMemory(true)// default  设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// default  设置下载的图片是否缓存在SD卡中
                .considerExifParams(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)//设置图片的显示比例
                .bitmapConfig(Bitmap.Config.RGB_565)// default 设置图片的解码类型
                //        .displayer(new RoundedBitmapDisplayer(75))//设置图片的圆角半径
                .displayer(new FadeInBitmapDisplayer(8000))//设置图片显示的透明度过程时间
                .build();

        return option;
    }


    //初始化相关信息
    private void initOkHttpUtil() {
        //OkHttp初始化
        String downloadFileDir = Environment.getExternalStorageDirectory().getPath() + "/okHttp_download/";
        String cacheDir = Environment.getExternalStorageDirectory().getPath();
        if (getExternalCacheDir() != null) {
            //缓存目录，APP卸载后会自动删除缓存数据
            cacheDir = getExternalCacheDir().getPath();
        }

        OkHttpUtil.init(getApplicationContext())
                .setConnectTimeout(10)//连接超时时间 单位：秒
                .setWriteTimeout(15)//写超时时间 单位：秒
                .setReadTimeout(15)//读超时时间 单位：秒
                .setMaxCacheSize(10 * 1024 * 1024)//缓存空间大小
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setHttpLogTAG("HttpLog")//设置请求日志标识
                .setIsGzip(false)//Gzip压缩，需要服务端支持
                .setShowHttpLog(false)//显示请求日志
                .setShowLifecycleLog(false)//显示Activity销毁日志
                .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setCachedDir(new File(cacheDir, "yy_cache"))//缓存目录
                .setDownloadFileDir(downloadFileDir)//文件下载保存目录
                .setResponseEncoding(Encoding.UTF_8)//设置全局的服务器响应编码
                .addResultInterceptor(HttpInterceptor.ResultInterceptor)//请求结果拦截器
                .addExceptionInterceptor(HttpInterceptor.ExceptionInterceptor)//请求链路异常拦截器
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this)))//持久化cookie
                .build();

    }


    protected   void registerReceiver(){
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        myReceiver=new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }


    protected  void unregisterReceiver(){
        if(myReceiver != null){
            this.unregisterReceiver(myReceiver);
        }
    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();

        unregisterReceiver();
        if(null != handler){
            handler.removeCallbacksAndMessages(null);
        }
    }

}
