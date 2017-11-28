package com.zhongan.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.umeng.analytics.MobclickAgent;
import com.zhongan.demo.http.OkHttpRequestManager;
import consumer.fin.rskj.com.library.login.ReqCallBack;
import com.zhongan.demo.module.BannerItem;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.ToastUtils;
import com.zhongan.demo.view.TopNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import consumer.fin.rskj.com.library.activitys.WebViewActivity;
import consumer.fin.rskj.com.library.utils.Constants;

import static consumer.fin.rskj.com.library.utils.Constants.BASE_URL;


public class HotPActivity extends BaseActivity implements OnClickListener {

    public static final String TAG = "HotPActivity";
    private TopNavigationView navigationView;
    private List<BannerItem> itemList01 = new ArrayList<>();
    private LinearLayout activity_main;

    private Intent intent;
    private HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_hotp);

        getProductListMore();
        setUI();
    }

    protected void setUI() {
        navigationView = (TopNavigationView) findViewById(R.id.topbar);
        navigationView.setClickListener(new TopNavigationView.NavigationViewClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });

        activity_main = (LinearLayout) findViewById(R.id.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", BASE_URL + MyApplication.getSP(this).getApplyLoan());
                intent.putExtra("title", "贷款");
                startActivity(intent);
                break;
        }
    }


    //获取产品列表
    public void getProductListMore() {

        showProgressDialog(null);
        paramsMap.clear();

        okHttpRequestManager.requestAsyn("/product/getProductListMore/AD_YOUYU_RM_2",
                OkHttpRequestManager.TYPE_RESTFUL_GET, paramsMap,
                new ReqCallBack<String>() {

                    @Override
                    public void onReqSuccess(String result) {
                        progressDialogDismiss();
                        LogUtils.Log(TAG, "getProductListMore = " + result);

                        try {
                            JSONObject object = new JSONObject(result);

                            if (null != object) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                if (null != jsonArray && jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object22 = jsonArray.getJSONObject(i);
                                        BannerItem item = new BannerItem();

                                        if(object22.isNull("fundId")){
                                            item.setAdvertTitle(object22.getString("advertTitle"));
                                            item.setAdvertPic(object22.getString("advertPic"));
                                            item.setAdvertUrl(object22.getString("advertUrl"));
                                        }else {
                                            item.setAdvertTitle("xiaojin15");
                                            item.setAdvertPic(object22.getString("appLogoUrl"));
//                                            MyApplication.getSP(HotPActivity.this).setId15(jsonArray.getJSONObject(i).getString("id"));
//                                            MyApplication.getSP(HotPActivity.this).setFundId15(jsonArray.getJSONObject(i).getString("fundId"));
                                            item.setAdvertUrl(jsonArray.getJSONObject(i).getString("fundId"));
                                            item.setProductId(jsonArray.getJSONObject(i).getString("id"));
                                        }
//                                        item.setAdvertPic(jsonArray.getJSONObject(i).getString("advertPic"));
//                                        item.setAdvertTitle(jsonArray.getJSONObject(i).getString("advertTitle"));
//                                        item.setAdvertUrl(jsonArray.getJSONObject(i).getString("advertUrl"));
//                                        item.setProductId(jsonArray.getJSONObject(i).getString("productId"));

                                        itemList01.add(item);
                                    }

                                    LogUtils.Log(TAG, "getProductListMore = " + itemList01.size());

                                    if (null != itemList01 && !itemList01.isEmpty()) {
                                        refreshUI(itemList01);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        progressDialogDismiss();
                        LogUtils.Log(TAG, "onReqFailed getProductListMore = " + errorMsg);

                        ToastUtils.showCenterToast(errorMsg, HotPActivity.this);
                    }
                });

    }

    private void refreshUI(List<BannerItem> itemList) {
        for (final BannerItem qPojo : itemList) {
            final View convertView = inflater.inflate(R.layout.banner_layout, null);

            ImageView image = (ImageView) convertView.findViewById(R.id.image);

            Picasso.with(this).
                    load(qPojo.getAdvertPic()).
                    placeholder(R.mipmap.defaul_pgt).
                    error(R.mipmap.defaul_pgt).
                    transform(new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            //对source实现自定义裁剪

                            LogUtils.Log(TAG, "source.getHeight()=" + source.getHeight() + ",source.getWidth()=" + source.getWidth());

                            if (source.getWidth() == 0) {
                                return source;
                            }

                            //如果图片小于设置的宽度，则返回原图
                            if (source.getWidth() == screenWidth) {
                                return source;
                            } else {
                                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                                int targetHeight = (int) (screenWidth * aspectRatio);
                                if (targetHeight != 0 && screenWidth != 0) {
                                    Bitmap result = Bitmap.createScaledBitmap(source, screenWidth, targetHeight, false);
                                    if (result != source) {
                                        // Same bitmap is returned if sizes are the same
                                        source.recycle();
                                    }
                                    return result;
                                } else {
                                    return source;
                                }
                            }
                        }

                        @Override
                        public String key() {
                            return null;
                        }
                    }).
                    into(image);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ("mashanghuan".equals(qPojo.getAdvertTitle())) {
                        intent = new Intent(HotPActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else if("xiaojin15".equals(qPojo.getAdvertTitle())){
                        intent = new Intent(HotPActivity.this, WebViewActivity.class);
                        intent.putExtra("url", Constants.BASE_URL + MyApplication.getSP(HotPActivity.this).getApplyLoan());
                        intent.putExtra("title","贷款");
                        intent.putExtra("id",qPojo.getProductId());
                        intent.putExtra("fundId",qPojo.getAdvertUrl());
                        startActivity(intent);
                    } else {
                        map.clear();
                        map.put("memId", MyApplication.getSP(HotPActivity.this).getMemId());
                        map.put("phone", MyApplication.getSP(HotPActivity.this).getPhone());
                        MobclickAgent.onEvent(HotPActivity.this, "click_" + qPojo.getAdvertTitle(), map);
                        intent = new Intent(HotPActivity.this, HtmlActivity.class);
                        intent.putExtra("url", qPojo.getAdvertUrl());
                        intent.putExtra("title", "加载中...");
                        startActivity(intent);
                    }

                }
            });

            activity_main.addView(convertView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

        }
    }


}
