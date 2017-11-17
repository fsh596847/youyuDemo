package consumer.fin.rskj.com.library.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.fragments.PLFragment;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.TopNavigationView2;

/**
 * Created by HP on 2017/7/26.
 * 借还款记录列表
 */

public class RecordListActivity extends BaseActivity {

    private static final String TAG = "RecordListActivity";

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private TopNavigationView2 topbar;
    private ViewPager viewPager;
    private TextView loan_record;
    private TextView payment_record;
    private MyAdapter pageAdapter;
    private int mPosition = 0;//位置
    private String data;
    //M100607 客户还款列表
    //M100602 客户借款列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = getIntent().getStringExtra("data");

        LogUtils.d(TAG,"data = " + data);

        try {
            JSONObject jsonObject = new JSONObject(data);
            data = jsonObject.getString("productId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        initFragments();
        setContentView(R.layout.activity_recordlist);

    }

    @Override
    public void init() {
        topbar = (TopNavigationView2) findViewById(R.id.topbar);
        topbar.setClickListener(new TopNavigationView2.NavigationViewClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        loan_record = (TextView) findViewById(R.id.loan_record);
        payment_record = (TextView) findViewById(R.id.payment_record);

        initViewPager();
    }


    private void initViewPager() {

        if(mPosition ==1){
            loan_record.setBackgroundColor(getResources().getColor(R.color.white));
            payment_record.setBackground(getResources().getDrawable(R.mipmap.pop));
        }else {
            loan_record.setBackground(getResources().getDrawable(R.mipmap.pop));
            payment_record.setBackgroundColor(getResources().getColor(R.color.white));
        }


        loan_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0,false);
            }
        });

        payment_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1,false);
            }
        });


        pageAdapter = new MyAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pageAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        loan_record.setBackground(getResources().getDrawable(R.mipmap.pop));
                        payment_record.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        loan_record.setBackgroundColor(getResources().getColor(R.color.white));
                        payment_record.setBackground(getResources().getDrawable(R.mipmap.pop));
                        break;
                    default:
                        loan_record.setBackground(getResources().getDrawable(R.mipmap.pop));
                        payment_record.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                }
            }


            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }


//    private void initFragments() {
//
//        if (fragments == null) {
//            return;
//        }
//
//        fragments.clear();
//
//
//        //借款记录
//        PLFragment fragment1 = new PLFragment();
//        Bundle b1 = new Bundle();
//        b1.putString("type","M100602");
//        fragment1.setArguments(b1);
//        fragments.add(fragment1);
//
//        //还款记录
//        PLFragment fragment2 = new PLFragment();
//        Bundle b2 = new Bundle();
//        b2.putString("type","M100602");
//        fragment2.setArguments(b2);
//        fragments.add(fragment2);
//
//    }



    class MyAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> sparseArray = new SparseArray();

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public  Fragment getItem(int position) {
//            return fragments.get(position);
            switch (position) {
                case 0:
                    return PLFragment.newInstance(position);
                case 1:
                    return PLFragment.newInstance(position);
                default:
                    return PLFragment.newInstance(0);
            }
        }

        @Override
        public int getCount() {
            return /*fragments.size()*/2;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (PLFragment) super.instantiateItem(container, position);
            sparseArray.put(position,fragment);
            return fragment;
        }
    }



    //M100602 app借款结果接口
    public void getCurrentRecordList( final PLFragment.DataCallBack dataCallBack){
        requestParams.clear();
        requestParams.put("transCode", "M100602");//接口标识
        requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
        requestParams.put("clientToken", sharePrefer.getToken());//登录后token
        requestParams.put("legalPerNum", "00001");
//        requestParams.put("fundId", sharePrefer.getXJFundId());
//        requestParams.put("productId", sharePrefer.getXJProductId());

        requestParams.put("indexNo", "0");
        requestParams.put("pageSize", "10");
        requestParams.put("productId", data);

        showLoading("正在加载...");


        sendPostRequest(requestParams, new ResultCallBack() {
            @Override
            public void onSuccess(String data) {
                dismissLoading();
                LogUtils.d(TAG, "借款记录: data--->" + data);

                dataCallBack.onSuccess(data);
            }

            @Override
            public void onError(String retrunCode, String errorMsg) {
                dismissLoading();

                dataCallBack.onError(retrunCode,errorMsg);
                LogUtils.d(TAG, "借款记录: errorMsg--->" + errorMsg);
            }

            @Override
            public void onFailure(String errorMsg) {
                dismissLoading();

                dataCallBack.onFailure(errorMsg);
                LogUtils.d(TAG, "借款记录: errorMsg--->" + errorMsg);
            }
        });
    }



    //M100607 app借款结果接口
    public void getPayRecordList( final PLFragment.DataCallBack dataCallBack){
        requestParams.clear();
        requestParams.put("transCode", "M100607");//接口标识
        requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
        requestParams.put("clientToken", sharePrefer.getToken());//登录后token
        requestParams.put("legalPerNum", "00001");
//        requestParams.put("fundId", sharePrefer.getXJFundId());
//        requestParams.put("productId", sharePrefer.getXJProductId());

        requestParams.put("indexNo", "0");
        requestParams.put("pageSize", "10");
        requestParams.put("productId", data);

        showLoading("正在加载...");

        LogUtils.d(TAG, "还款记录: requestParams--->" + requestParams.toString());
        sendPostRequest(requestParams, new ResultCallBack() {
            @Override
            public void onSuccess(String data) {
                dismissLoading();
                LogUtils.d(TAG, "还款记录: data--->" + data);

                dataCallBack.onSuccess(data);
            }

            @Override
            public void onError(String retrunCode, String errorMsg) {
                dismissLoading();

                dataCallBack.onError(retrunCode,errorMsg);
                LogUtils.d(TAG, "还款记录: errorMsg--->" + errorMsg);
            }

            @Override
            public void onFailure(String errorMsg) {
                dismissLoading();

                dataCallBack.onFailure(errorMsg);
                LogUtils.d(TAG, "还款记录: errorMsg--->" + errorMsg);
            }
        });
    }

}