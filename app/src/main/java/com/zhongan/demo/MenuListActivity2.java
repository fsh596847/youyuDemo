package com.zhongan.demo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhongan.demo.contant.HttpContent;
import com.zhongan.demo.fragments.FinacalFragment15;
import com.zhongan.demo.fragments.HenXFragment;
import com.zhongan.demo.fragments.PersonCenterFragment;
import com.zhongan.demo.fragments.RightFragment;
import com.zhongan.demo.fragments.UnLoginFragment;
import com.zhongan.demo.fragments.YouYUFragment;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.activitys.HXBasicSelfInfoActivity;
import com.zhongan.demo.hxin.activitys.HXDealBankCardActivity;
import com.zhongan.demo.hxin.activitys.HXFaceIDCardInfoUploadActivity;
import com.zhongan.demo.hxin.activitys.HXFaceStartActivity;
import com.zhongan.demo.hxin.activitys.HXResultActivity;
import com.zhongan.demo.hxin.activitys.HXResultingActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.ActivityStackManagerUtils;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.ToastUtils;
import com.zhongan.demo.view.FragmentTabHost;
import com.zhongan.demo.view.Tab;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by HP on 2017/8/7.
 * 功能菜单列表
 */

public class MenuListActivity2 extends HXBaseActivity  {

    protected static final String TAG = "MenuListActivity2";
//    private Fragment leftFragment;//首页
//    private Fragment rightFragment;//金融超市
//    private Fragment pcFragment;//个人中心

    private Dialog mDialog;
    private LayoutInflater mInflater;
    private FragmentTabHost mTabHost;
  private ArrayList<Tab> mTabs = new ArrayList<Tab>();

  private String idCard = "", userName = "";

//    private Dialog updateDialog;//更新提示框
//    private String fileUrl;//文件下载路径
//    private String uopdateMsg;
//    private int isrelease = -1;

    private long curMillions = 0;

//    private Handler handler=new Handler(){
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    getUpdateInfo();
//                    break;
//
//                case 2:
//                    String text22 =(String) msg.obj;
//                    Log.d(TAG,"text22 = " + text22);
//
//                    JSONObject update_info;
//                    String code;
//                    try {
//                        update_info = new JSONObject(text22);
//                        //数据返回成功
//                        code = update_info.getString("code");
//                        Log.d(TAG,"code = " + code);
//
//                        if(!TextUtils.isEmpty(code) && "success".equals(code)){
//                            JSONObject updateItem = update_info.getJSONObject("data");
//                            if(null != updateItem){
//
//                                uopdateMsg = updateItem.getString("content");//发布内容
//                                fileUrl = updateItem.getString("fileUrl");//发布内容
//                                isrelease = updateItem.getInt("isrelease");//是否强制更新
//
//                                if(2 == isrelease){
//                                    showVersionDialog(true);//强制更新
//                                }else if(1 == isrelease){
//                                    showVersionDialog(false);//普通更新
//                                }else {
//                                    //没有更新
//                                    //ToastUtils.showCenterToast("没有更新",mActivity);
//                                    Log.d(TAG,"没有更新");
//                                }
//
//                            }
//
//                        }else {
//                            Log.d(TAG,"no update info");
//                        }
//
//                    } catch (Exception e) {
//                        Log.d(TAG,"Exception = " + e.getMessage());
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menulist2);
        ActivityStackManagerUtils.getInstance().addActivity(this);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        screenHeight = outMetrics.heightPixels;
        screenWidth = outMetrics.widthPixels;

        initView();

        mDialog = Util.createLoadingDialog(this, "数据加载中,请稍等...");

//        handler.sendEmptyMessageDelayed(0,1500);

    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getFragmentManager(), R.id.fragment_container);

        initTabHost();

        //去掉纵向分割线 SHOW_DIVIDER_NONE
        //mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //解决侧滑菜单 滑动是底部分割线 绘制
        //mTabHost.getTabWidget().setDividerDrawable(getResources().getDrawable(R.drawable.tab_divider));
        mTabHost.setCurrentTab(0);
    }

    private void initTabHost() {
        mTabs.clear();
        if(getSharePrefer().iSLogin()){
            if(getSharePrefer().isShowTab()){
                Tab num1 = getHomeTab();

                Tab num2 = new Tab(R.string.tap_market, R.drawable.m_tab2selector, RightFragment.class);
                Tab num3 = new Tab(R.string.tap_center, R.drawable.m_tab3selector, PersonCenterFragment.class);

                mTabs.add(num1);
                mTabs.add(num2);
                mTabs.add(num3);
            }else {
//                Tab num1 = new Tab(R.string.tap_market, R.drawable.m_tab2selector, RightFragment.class);
                Tab num1 = getHomeTab();
                Tab num2 = new Tab(R.string.tap_center, R.drawable.m_tab3selector, PersonCenterFragment.class);

                mTabs.add(num1);
                mTabs.add(num2);
            }


        }else {
            Tab num1 = new Tab(R.string.tap_home, R.drawable.m_tab1selector, UnLoginFragment.class);
            Tab num2 = new Tab(R.string.tap_center, R.drawable.m_tab3selector, PersonCenterFragment.class);
            mTabs.add(num1);
            mTabs.add(num2);
        }

        mTabHost.cleanTabs();
        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }

        /*mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Toast.makeText(MenuListActivity2.this,"onTabChanged",Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @NonNull
    private Tab getHomeTab() {
        Tab num1;
        if("mashanghuan".equals(getSharePrefer().getIdentity())){
            num1 = new Tab(R.string.tap_home, R.drawable.m_tab1selector, YouYUFragment.class);
        } else if("xinyongdai".equals(getSharePrefer().getIdentity())){
            num1 = new Tab(R.string.tap_home, R.drawable.m_tab1selector, /*XiaoJFragment.class*/FinacalFragment15.class);
        }else if("hengxinxiaodai".equals(getSharePrefer().getIdentity())){
            num1 = new Tab(R.string.tap_home, R.drawable.m_tab1selector, HenXFragment.class);
        }else {
            //默认 看有余
            num1 = new Tab(R.string.tap_home, R.drawable.m_tab1selector, YouYUFragment.class);
        }
        return num1;
    }

    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return view;
    }


    public void s2008Post(){
        /**
         * {
         "transCode": "S200008",
         "version": "1.0",
         "legalPerNum": "00006",
         "channelNo": "3",
         "clientToken": "7d2cc78d-460e-4a23-a70a-0fcd3024bbef"
         }
         */

        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", "S200008");
        //params.addBodyParameter("legalPerNum", "00009");// 法人编号
        params.addBodyParameter("channelNo", "3");
        params.addBodyParameter("version", "1.0");
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        params.addBodyParameter("legalPerNum", "00006");// 法人编号

        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                LogUtils.Log(TAG, "onSuccess = " + data);
                mDialog.cancel();

                try {
                    JSONObject jsonObject = new JSONObject(data);

                    String userId = jsonObject.getString("userId");
                    String custNum = jsonObject.getString("userNum");
                    String productId = jsonObject.getString("productId");

                    getSharePrefer().setUserId(userId);
                    getSharePrefer().setCustNum(custNum);
                    getSharePrefer().setProductId(productId);

                    try{
                        idCard = jsonObject.getString("idCard");
                        userName = jsonObject.getString("userName");
                    }catch (Exception e){

                    }

                    if(!TextUtils.isEmpty(idCard)){
                        getSharePrefer().setIdCardNum(idCard);
                    }

                    if(!TextUtils.isEmpty(userName)){
                        getSharePrefer().setUserName(userName);
                    }

                    //判断授信状态
                    nextStep(jsonObject.getString("userStateInfo"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStart() {
                mDialog.show();
            }

            @Override
            public void onFailure(HttpException exception, String msg) {
                mDialog.cancel();
                LogUtils.Log(TAG, "onFailure = " + msg);
            }

            @Override
            public void onError(String returnCode, String msg) {
                mDialog.cancel();
                LogUtils.Log(TAG, "onError = " + msg);
            }
        });

    }


    //判断授信状态
    private void nextStep(String userStateInfo) {
        Intent intent1 = new Intent(this, HXResultActivity.class);
        // 我要贷款
        if ("aced".equals(userStateInfo)) {
            //授信申请中
            //Toast.makeText(LoginActivity.this, "授信申请中，请稍后...", Toast.LENGTH_SHORT).show();
//            intent1.putExtra("message","授信申请中，请稍后...");
//            startActivity(intent1);

            Intent intent0 = new Intent(this,HXResultingActivity.class);
            startActivity(intent0);

        } else if ("cedbad".equals(userStateInfo)) {
            //授信申请被拒绝
            //Toast.makeText(this, "授信申请被拒绝，请重新申请...", Toast.LENGTH_SHORT).show();
/*            ToastUtils.showCenterToast( "授信申请被拒绝，请重新申请..." ,this);
            *//*Intent uploadIntent = new Intent(LoginActivity.this, FaceIDCardInfoUploadActivity.class);
            LoginActivity.this.startActivity(uploadIntent);*//*
            intent1.putExtra("message","这不是我要的结果");
            intent1.putExtra("isSuccess",false);

            startActivity(intent1);*/

            Intent intent0 = new Intent(this,HXResultingActivity.class);
            startActivity(intent0);

        } else if ("0".equals(userStateInfo)
                || "registered".equals(userStateInfo)) {
            //状态为0 、已注册、身份证已上传 跳转到身份证上传页面
            Intent uploadIntent = new Intent(this, HXFaceIDCardInfoUploadActivity.class);
            this.startActivity(uploadIntent);
        }else if("realfied".equals(userStateInfo)){
            Intent intent2 = new Intent(this, HXDealBankCardActivity.class);
            intent2.putExtra("selfName", userName);
            intent2.putExtra("selfIdcard", idCard);
            intent2.putExtra("option", "check");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
            startActivity(intent2);
        }

        else if ("saveinfo".equals(userStateInfo)) {
            //完成完善客户信息 跳转银行卡验证页面
            Intent intent = new Intent(this, HXDealBankCardActivity.class);
            intent.putExtra("selfName", userName);
            intent.putExtra("selfIdcard", idCard);
            intent.putExtra("option", "check");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
            startActivity(intent);
        } else if("ced".equals(userStateInfo)){
            //受信成功 跳转结果页面
            /*intent1.putExtra("message","审批通过啦");
            intent1.putExtra("isSuccess",true);
            startActivity(intent1);*/

            Intent intent0 = new Intent(this,HXResultingActivity.class);
            startActivity(intent0);

        } else if ("00".equals(userStateInfo)) {//verifi4 = 00
            //完成银行卡验证 跳转结果页面
            Intent faceIntent = new Intent(this,
                    HXFaceStartActivity.class);
            startActivity(faceIntent);
        }else if("face++".equals(userStateInfo)){
//            Intent intent = new Intent(this,ResultActivity.class);
//            intent.putExtra("message","测试人员已达上限");
//            intent.putExtra("isSuccess",false);
//            startActivity(intent);
            /*JSONObject jsonObject = new JSONObject();
            String resultString = getResources().getString(R.string.verify_success);
            try {
                jsonObject.put("result", resultString);
                jsonObject.put("resultcode", R.string.verify_success);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HXFaceResultActivity.startActivity(this,  jsonObject.toString());*/

            Intent uploadIntent = new Intent(this,   HXBasicSelfInfoActivity.class);
            uploadIntent.putExtra("name", userName);
            uploadIntent.putExtra("idnumber", idCard);
            startActivity(uploadIntent);

        }else if("verifi4".equals(userStateInfo)){
            //完成银行卡验证 跳转结果页面
            Intent faceIntent = new Intent(this,
                    HXFaceStartActivity.class);
            startActivity(faceIntent);
        }else {//其他情况都返回结果页面
            Intent intent0 = new Intent(this,HXResultingActivity.class);
            startActivity(intent0);
        }
    }



    //获取产品列表
    public void getMenuList(final RightFragment.FinishCallBackImpl finishCallBack){

        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/json");
        params.addHeader("token", getSharePrefer().getToken());
        //params.addBodyParameter("legalPerNum", "00009");// 法人编号
        params.addBodyParameter("channelNo", "3");
        params.addBodyParameter("version", BuildConfig.VERSION_NAME);
        params.addBodyParameter("legalPerNum", "00009");// 法人编号
        //  /product/getProduct
        MyApplication.instance.dataHttp.send(HttpMethod.GET, HttpContent.BASE_URL + "/product/getProductList",
                params, new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LoggerUtil.debug(TAG, "HttpException--------------->" + arg1 + arg0.getMessage());
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (responseInfo != null && responseInfo.result != null) {
                            LoggerUtil.debug(TAG, "onSuccess---->" + responseInfo.result );
                            finishCallBack.finishCallBack(responseInfo.result);

                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                });

    }



    //获取版本信息
//    private void getUpdateInfo() {
//
//
//        RequestParams params = new RequestParams("utf-8");
//        params.addHeader("Content-Type", "application/json");
//        params.addHeader("deviceType", "android");
//        params.addHeader("version", BuildConfig.VERSION_NAME);
//        params.addHeader("code", "youyu");
//        LoggerUtil.debug(TAG, "----getUpdateInfo----------->" + params.toString());
//        MyApplication.instance.dataHttp.send(HttpMethod.POST, HttpContent.VERSION_UPDATE,
//                params, new RequestCallBack<String>() {
//                    @Override
//                    public void onFailure(HttpException arg0, String arg1) {
//                        LoggerUtil.debug(TAG, "HttpException----getUpdateInfo----->" + arg1 + arg0.getMessage());
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        if (responseInfo != null && responseInfo.result != null) {
//                            LoggerUtil.debug(TAG, "onSuccess--getUpdateInfo-->" + responseInfo.result );
//                            handler.obtainMessage(2,responseInfo.result).sendToTarget();
//                        }
//                    }
//
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                    }
//                });
//
//    }


//    protected void showVersionDialog(final boolean option) {
//        if (updateDialog == null) {
//            updateDialog = new Dialog(this, R.style.myDialogTheme);
//            View view = LayoutInflater.from(this).inflate(R.layout.update_dialog_layout, null);
//            TextView title = (TextView) view.findViewById(R.id.title);
//            TextView content = (TextView) view.findViewById(R.id.content);
//            content.setText(uopdateMsg);//更新提示
//
//            TextView sureBtn = (TextView) view.findViewById(R.id.sure);
//            sureBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //执行下载
//                    if(!TextUtils.isEmpty(fileUrl)){
//                        //跳转浏览器下载
//                        Intent intent= new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse(fileUrl);
//                        intent.setData(content_url);
//                        startActivity(intent);
//                    }
//
//                }
//            });
//
//            TextView cancleBtn = (TextView) view.findViewById(R.id.cancel);
//            if(option){
//                cancleBtn.setText("退出");
//            }
//            cancleBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    updateDialog.dismiss();
//
//                    if(option){//如果是强制更新 点击返回就退出
//                        onBackPressed();
//                    }
//                }
//            });
//
//            updateDialog.setContentView(view);
//            updateDialog.setCanceledOnTouchOutside(false);
//            WindowManager.LayoutParams params = updateDialog.getWindow().getAttributes();
//            params.width = (int) (screenWidth * 0.8);
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            updateDialog.getWindow().setAttributes(params);
//        }
//        updateDialog.show();
//    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //initTabHost();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(null != handler){
//            handler.removeCallbacksAndMessages(null);
//        }
    }

        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            long millions = System.currentTimeMillis();
            if(millions-curMillions > 2000){
                curMillions = millions;
                //Toast.makeText(this, "再按一次退出程序",Toast.LENGTH_SHORT).show();
                ToastUtils.showCenterToast( "再按一次退出程序" ,this);
            }else{
                //finish();
                ActivityStackManagerUtils.getInstance().finishAllActivity();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
